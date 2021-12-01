package com.seckill.service.Impl;

import com.seckill.dao.PromoDoMapper;
import com.seckill.dataObject.PromoDo;
import com.seckill.service.PromoService;
import com.seckill.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDoMapper promoDoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {

        // get promotion info of the given itemId
        PromoDo promoDo = promoDoMapper.selectByItemId(itemId);

        // dataobject -> model
        PromoModel promoModel = convertFromPromoDo(promoDo);
        if (promoModel == null){
            return null;
        }

        // check date
        if (promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }
        else if (promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }
        else {
            promoModel.setStatus(2);
        }


        return promoModel;
    }

    private PromoModel convertFromPromoDo(PromoDo promoDo){
        if (promoDo == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo, promoModel);
        promoModel.setPromoPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDo.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDo.getEndDate()));

        return promoModel;

    }

}
