package com.seckill.service;

import com.seckill.service.model.UserModel;

public interface UserService {

    // to get user object by user id
    UserModel getUserById(Integer id);

}
