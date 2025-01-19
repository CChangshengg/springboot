package com.example.demo.model.domain.request;

import lombok.Data;

import java.io.Serializable;
/**
 * 用户登录请求
 *
 * @Author: changsheng
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -8892177265508543800L;

    private String userAccount;
    private String userPassword;
}
