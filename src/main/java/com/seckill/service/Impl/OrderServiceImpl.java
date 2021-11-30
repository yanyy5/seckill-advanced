package com.seckill.service.Impl;

import com.seckill.dao.OrderDoMapper;
import com.seckill.dao.SequenceDoMapper;
import com.seckill.dataObject.OrderDo;
import com.seckill.dataObject.SequenceDo;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.service.ItemService;
import com.seckill.service.OrderService;
import com.seckill.service.UserService;
import com.seckill.service.model.ItemModel;
import com.seckill.service.model.OrderModel;
import com.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDoMapper sequenceDoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {

        // 1. validate the order status: valid users, right amount, valid item
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "item not exist");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "user not exist");
        }

        if(amount <= 0 || amount > 99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "wrong item amount");
        }

        // 2. modify the stock if the order is successful
        // lock the stock in 2 cases: create an order(use this strategy) / create a payment
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 3. save the order to DB
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));

        // generate order id
        orderModel.setId(generateOrderNo());
        OrderDo orderDo = convertFromOrderModel(orderModel);
        orderDoMapper.insertSelective(orderDo);

        // add sales
        itemService.increaseSales(itemId, amount);

        // 4. return info to front-end



        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        // 16 bits = 8(yyyy-mm-dd) + 6(auto-increment) + 2(分库分表,00-99)

        // 8(yyyy-mm-dd)
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 6(auto-increment)
        int sequence = 0;
        SequenceDo sequenceDo = sequenceDoMapper.getSequenceByName("order_info");
        sequence = sequenceDo.getCurrentValue();
        sequenceDo.setCurrentValue(sequenceDo.getCurrentValue()+sequenceDo.getStep());
        sequenceDoMapper.updateByPrimaryKeySelective(sequenceDo);

        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        // 2(分库分表,00-99)
        stringBuilder.append("00");

        return stringBuilder.toString();

    }

    private OrderDo convertFromOrderModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }
        OrderDo orderDo = new OrderDo();
        BeanUtils.copyProperties(orderModel, orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDo;
    }

}
