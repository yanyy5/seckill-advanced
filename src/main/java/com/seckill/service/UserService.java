package com.seckill.service;

import com.seckill.error.BusinessException;
import com.seckill.service.model.UserModel;

public interface UserService {

    // to get user object by user id
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

}
