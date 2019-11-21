package com.sustech.conferenceSystem.service;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.dto.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 处理登陆的业务逻辑
     * @param user 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> loginService(User user){
        Map<String,String> res=new HashMap<>();
        List<User> users=userMapper.isExistUser(user);
        if(users.size()==0){
            res.put("state","0");
            res.put("message","用户名或密码错误");
        }else {
            res.put("state","1");
            res.put("message","登陆成功");
        }
        return res;
    }

    /**
     * 处理注册的业务逻辑
     * @param user 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> registService(User user){
        Map<String,String> res=new HashMap<>();
        List<User> users=userMapper.isExistUser(new User(user.getUsername()));
        if(users.size()==0){
            System.out.println(user);
            userMapper.regist(user);
            res.put("state","1");
            res.put("message","用户创建成功");
        }else {
            res.put("state","0");
            res.put("message","用户名已存在");
        }
        return res;
    }
}
