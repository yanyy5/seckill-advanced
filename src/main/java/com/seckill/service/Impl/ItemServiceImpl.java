package com.seckill.service.Impl;

import com.seckill.dao.ItemDoMapper;
import com.seckill.dao.ItemStockDoMapper;
import com.seckill.dataObject.ItemDo;
import com.seckill.dataObject.ItemStockDo;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.service.ItemService;
import com.seckill.service.model.ItemModel;
import com.seckill.validator.ValidationResult;
import com.seckill.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;


    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {


        // validation
        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        // convert item model to data object
        ItemDo itemDo = convertItemDoFromItemModel(itemModel);

        // write to DB
        itemDoMapper.insertSelective(itemDo);
        itemModel.setId(itemDo.getId());
        ItemStockDo itemStockDo = convertItemStockDoFromItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);

        // return object

        return getItemById(itemModel.getId());
    }

    private ItemStockDo convertItemStockDoFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setStock(itemModel.getStock());
        itemStockDo.setItemId(itemModel.getId());
        return itemStockDo;
    }

    private ItemDo convertItemDoFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel, itemDo);

        // data type of price is different
        itemDo.setPrice(itemModel.getPrice().doubleValue());

        return itemDo;
    }

    @Override
    public List<ItemModel> listItem() {

        List<ItemDo> itemDoList = itemDoMapper.listItem();

        List<ItemModel> itemModelList = itemDoList.stream().map(itemDo -> {
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            ItemModel itemModel = convertModelFromDataObject(itemDo,itemStockDo);
            return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {

        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null){
            return null;
        }
        // to get item stock
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());

        // convert data object to model
        ItemModel itemModel = convertModelFromDataObject(itemDo, itemStockDo);

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affectedRow = itemStockDoMapper.decreaseStock(itemId, amount);
        if (affectedRow > 0){
            // update successfully
            return true;
        }
        else{
            // fail
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDoMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject (ItemDo itemDo, ItemStockDo itemStockDo){

        if (itemDo == null){
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo, itemModel);
        // data type of price is different
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));

        if (itemStockDo.getStock() != null){
            itemModel.setStock(itemStockDo.getStock());
        }

        return itemModel;

    }

}
