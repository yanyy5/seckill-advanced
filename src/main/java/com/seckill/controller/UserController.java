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

@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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

    // identify exception handler to fix the exception not in controller layer
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception e){

        BusinessException businessException = (BusinessException)e;

        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("errCode", businessException.getErrCode());
        responseData.put("errMsg", businessException.getErrMsg());

        commonReturnType.setData(responseData);

        return commonReturnType;
    }



}
