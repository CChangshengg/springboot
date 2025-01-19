package com.example.demo.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable { //Java对象的序列化 ，不然json不认识


    private static final long serialVersionUID = -8892177265508543800L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
