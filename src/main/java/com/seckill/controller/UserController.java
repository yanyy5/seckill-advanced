package com.seckill.controller;

import com.seckill.controller.viewObject.UserVO;
import com.seckill.service.UserService;
import com.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public UserVO getUser(@RequestParam(name="id") Integer id){
        // use service to get the User object with the related id
        // and return it to front-end

        UserModel userModel = userService.getUserById(id);

        // convert the userModel(with all the info) to userVO(the viewObject that can be used by UI)
        return convertFromModel(userModel);

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
