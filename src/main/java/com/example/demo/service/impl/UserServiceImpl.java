package com.example.demo.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.domain.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.contant.UserContant.USER_LOGIN_STATE;

/**
 * 用户服务实线类
 *
 * @author changsheng
 * @date 2023/3/10 16:53
 */

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


    @Resource
    private UserMapper userMapper;
    /**
     * 盐值
     */
    private static final  String SALT="iwant";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //       1.校验
        if (StringUtils.isAllBlank(userAccount,userPassword,checkPassword)){
            //todo 修改为自定义异常
            return -1;}
        if (userAccount.length()<4){return -1;}
        if (userPassword.length()<8||checkPassword.length()<8){return -1;}
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){return -1;}

        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){return -1;}

        //查询账户是否已经注册
        QueryWrapper <User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if (count>0){return -1;}



//      2.密码加盐
        String overpassed =DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

//       2.1实现注册
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(overpassed);


       boolean result =this.save(user);
        if (!result){return -1;}
//       3.返回结果
        return user.getId();


    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAllBlank(userAccount,userPassword)){return null;}
        if (userAccount.length()<4){return null;}
        if (userPassword.length()<8){return null;};
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){return null;};

        //加盐
        String overpassed =DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        //2.查询
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",overpassed);
        User user = userMapper.selectOne(queryWrapper);
        //判断
        if (user==null){
            log.info("user login failed,user account or password wrong ");
            return null;
        }

        //脱敏
        User safetyUser=new User();

        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        //记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        System.out.println(request.getSession());

        return safetyUser;
    }
}




