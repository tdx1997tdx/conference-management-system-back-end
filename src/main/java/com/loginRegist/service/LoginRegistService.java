package com.loginRegist.service;

import com.loginRegist.mapper.UserMapper;
import com.loginRegist.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginRegistService {

    @Resource
    private UserMapper userMapper;

    public String loginService(String username,String password){
        List<User> users=userMapper.selectUser(username,password);
        if(users.size()==0){
            return "登录失败";
        }else {
            return "登录成功";
        }
    }

    public String registService(String username,String password){
        List<User> users=userMapper.selectUser(username,password);
        if(users.size()==0){
            User user=new User();
            user.setName(username);
            user.setPassword(password);
            userMapper.addUser(user);
            return "注册成功";
        }else {
            return "用户已存在，注册失败";
        }

    }
}
