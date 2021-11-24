package com.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("user")
@RequestMapping("/user")
public class UserController {

    public void getUser(@RequestParam(name="id") Integer id){
        // use service to get the User object with the related id
        // and return it to front-end

    }

}
