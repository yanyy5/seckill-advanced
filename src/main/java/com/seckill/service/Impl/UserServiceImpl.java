package com.seckill.service.Impl;

import com.seckill.dao.UserDoMapper;
import com.seckill.dao.UserPasswordDoMapper;
import com.seckill.dataObject.UserDo;
import com.seckill.dataObject.UserPasswordDo;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Override
    public UserModel getUserById(Integer id) {
        // call the userDoMapper to get dataObject(UserDo)
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);

        if (userDo == null){
            return null;
        }

        // to get the encryptPassword by user id
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());

        return convertFromDataObject(userDo, userPasswordDo);

    }

    private UserModel convertFromDataObject(UserDo userDo, UserPasswordDo userPasswordDo){
        // to build a userModel by using UserDo and UserPasswordDo object

        // if null or not
        if (userDo == null){
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo, userModel);

        if (userPasswordDo != null){
            userModel.setEncryptPassword(userPasswordDo.getEncryptPassword());

        }



        return userModel;

    }



}
