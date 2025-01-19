package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.model.domain.User;
import com.example.demo.model.domain.request.UserLoginRequest;
import com.example.demo.model.domain.request.UserRegisterRequest;
import com.example.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.contant.UserContant.USER_LOGIN_STATE;
import static com.example.demo.contant.UserContant.admin_role;

/**
 *  用户接口
 *
 *
 * @Author: changsheng
 */


@RestController  //打上这个注解，类里面所有请求的接口，相应的类型都是json格式
@RequestMapping("/user")
public class UserController {
    @Resource
    private  UserService userService;

    @PostMapping("/register")   //注册
    public long  userRegister( @RequestBody UserRegisterRequest userRegisterRequest){

        if (userRegisterRequest == null) {
            return -1;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }

        return  userService.userRegister(userAccount, userPassword, checkPassword);

    }

    @PostMapping("/login")  //登录
    public User userLogin (@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){

        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return  userService.userLogin(userAccount, userPassword, request);

    }


    @GetMapping("/search")  //搜索
    public List<User> searchUsers(String username, HttpServletRequest request){
        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isNotBlank(username)) {
            return new ArrayList<>();
        }
        queryWrapper.like("username", username);
        return userService.list(queryWrapper);

    }


    @PostMapping("delete")  //删除
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
        if (!isAdmin(request)){
            return  false;
        }
        if (id < 0) {
            return false;
        }
        return userService.removeById(id);

    }


    /**
     * 判断是否是管理员
     * @param request
     * @return
     */
    private  boolean isAdmin(HttpServletRequest request){
        Object userobject = request.getSession().getAttribute(USER_LOGIN_STATE);

        User user = (User)userobject;
        return user != null && user.getUserRole()==admin_role;
    }

}
