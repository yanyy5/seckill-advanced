package com.seckill.controller;

import com.seckill.controller.viewObject.UserVO;
import com.seckill.error.BusinessException;
import com.seckill.error.EmBusinessError;
import com.seckill.response.CommonReturnType;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    @Resource
    private HttpServletRequest httpServletRequest;

    // interface:  user login
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "phone") String phone,
                                  @RequestParam(name = "password") String password) throws NoSuchAlgorithmException, BusinessException {

        // validation
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // user login
        UserModel userModel = userService.validateLogin(phone, EncodeByMD5(password));

        // add token to the session(user login successfully)
        httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);

    }

    // interface:  user sign up/register
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "phone") String phone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "usrName") String usrName,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "age") Integer age) throws BusinessException, NoSuchAlgorithmException {

        // validate telephone and otpCode
        String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(phone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "wrong OTP code");
        }

        // register
        UserModel userModel = new UserModel();
        userModel.setName(usrName);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setPhone(phone);
        userModel.setRegisterMode("by Phone");
        userModel.setEncryptPassword(EncodeByMD5(password));

        userService.register(userModel);

        return CommonReturnType.create(null);


    }

    private String EncodeByMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        String newStr = base64Encoder.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));

        Base64.Encoder encoder = Base64.getMimeEncoder();
        String newStr = encoder.encodeToString(md5.digest(str.getBytes(StandardCharsets.UTF_8)));

        return newStr;
    }


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
        System.out.println("phone = " + phone + " & optCode = " + optCode);

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
