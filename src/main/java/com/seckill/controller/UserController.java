package com.seckill.controller;

import com.seckill.controller.viewObject.UserVO;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.response.CommonReturnType;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // interface: for a user to get otp messages
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "phone") String phone){
        // generate OTP valid code by some rules
        // random number
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);

        // bing the OTP code with phone number
        // (key, value) redis
        // HTTP session
        httpServletRequest.getSession().setAttribute(phone, optCode);

        // send the OTP code (message) to users (skip)
        System.out.println("phone = " + phone + "& optCode = " + optCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        // use service to get the User object with the related id
        // and return it to front-end

        UserModel userModel = userService.getUserById(id);

        // if user info not exists
        if (userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        // convert the userModel(with all the info) to userVO(the viewObject that can be used by UI)
        UserVO userVO = convertFromModel(userModel);

        // return common object
        return CommonReturnType.create(userVO);

    }

    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }



}
