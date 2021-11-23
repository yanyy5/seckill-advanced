package com.seckill;

import com.seckill.dao.UserDoMapper;
import com.seckill.dataObject.UserDo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */

@SpringBootApplication(scanBasePackages = {"com.seckill"})
@RestController
@MapperScan("com.seckill.dao")
public class App 
{
    @Autowired
    private UserDoMapper userDoMapper;

    @RequestMapping("/")
    public String home(){
        UserDo userDo = userDoMapper.selectByPrimaryKey(1);
        if (userDo == null){
            return "not exist";
        }
        else{
            return userDo.getName();
        }
//        return "hello worlddddd";
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // run the spring boot application
        SpringApplication.run(App.class, args);

    }
}
