package com.example.demo.service.impl;

import com.example.demo.model.domain.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @用户服务测试
 *
 * @author Administrator
 */
@SpringBootTest
public class UserServiceTest {


    @Resource
    private UserService userService;

    @Test
    void userRegister(){
        String userAccount="changsheng";
        String userPassword="123456789";
        String checkPassword="";

        long result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount="changsheng";
        userPassword="";
        checkPassword="123456789";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount="yup";
        userPassword="123456789";
        checkPassword="123456789";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount="123456";
        userPassword="123456789";
        checkPassword="123456789";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount="y i@*(()";
        userPassword="123456789";
        checkPassword="123456789";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount="changshengg1";
        userPassword="123456789";
        checkPassword="123456789";
        result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertTrue(result>0);
    }



    @Test
    void test_addUser(){
        User user=new User();

        user.setUsername("张三");
        user.setUserAccount("123456");
        user.setAvatarUrl("https://tse3-mm.cn.bing.net/th/id/OIP-C.74sYbnMLHdr2cYx-Ci6HvgHaHG?rs=1&pid=ImgDetMain");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");



       boolean result= userService.save(user);

        System.out.println(user.getId());

        Assertions.assertTrue(result);
    }

}