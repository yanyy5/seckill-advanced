package com.seckill.service;

import com.seckill.service.model.OrderModel;

public interface OrderService {

    OrderModel createOrder(Integer userId, Integer itemId, Integer amount);

}
