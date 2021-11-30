package com.seckill.service.Impl;

import com.seckill.dao.UserDoMapper;
import com.seckill.dao.UserPasswordDoMapper;
import com.seckill.dataObject.UserDo;
import com.seckill.dataObject.UserPasswordDo;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import com.seckill.validator.ValidationResult;
import com.seckill.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Autowired
    private ValidatorImpl validator;

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

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        ValidationResult result = validator.validate(userModel);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        // convert userModel to userDo
        UserDo userDo = convertFromModel(userModel);
        userDoMapper.insertSelective(userDo);

        userModel.setId(userDo.getId());

        // password
        UserPasswordDo userPasswordDo = convertPasswordFromModel(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDo);

        return;

    }

    @Override
    public UserModel validateLogin(String phone, String password) throws BusinessException {

        // To get user's information by user phone
        UserDo userDo = userDoMapper.selectByPhone(phone);
        if (userDo == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAILED);
        }
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDo.getId());
        UserModel userModel = convertFromDataObject(userDo, userPasswordDo);

        // Compare if the password in DB and user input are same
        if (!StringUtils.equals(password, userModel.getEncryptPassword())){
            throw new BusinessException((EmBusinessError.USER_LOGIN_FAILED));
        }

        return userModel;

    }

    private UserDo convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel, userDo);
        return userDo;
    }

    private UserPasswordDo convertPasswordFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPasswordDo userPasswordDo = new UserPasswordDo();
        userPasswordDo.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDo.setUserId(userModel.getId());
        return userPasswordDo;
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
