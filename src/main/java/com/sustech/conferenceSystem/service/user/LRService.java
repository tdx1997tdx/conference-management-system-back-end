package com.sustech.conferenceSystem.service.user;

import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.util.RedisUtil;
import com.sustech.conferenceSystem.util.Token;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LRService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 处理登陆的业务逻辑
     * @param user 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> loginService(User user){
        Map<String,String> res=new HashMap<>();
        List<User> users=userMapper.searchUser(user);
        if(users.size()==0){
            res.put("state","0");
            res.put("message","用户名或密码错误");
        }else {
            String token=Token.getInstance().makeToken(user.getUsername());
            redisUtil.set(token,user.getUsername(),432000);
            System.out.println(token);
            res.put("state","1");
            res.put("set_cookie",token);
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
        boolean isSucess;
        try{
            isSucess=userMapper.addUser(user);
        }catch (Exception e){
            isSucess=false;
        }
        if(isSucess){
            res.put("state","1");
            res.put("message","添加用户成功");
        }else {
            res.put("state","0");
            res.put("message","添加用户失败");
        }
        return res;
    }

    /**
     * 处理验证的业务逻辑
     * @param cookie 传入待验证的cookie
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> loginVerificationService(String cookie){
        Map<String,String> res=new HashMap<>();
        String username=(String)redisUtil.get(cookie);
        if(username!=null){
            res.put("state","1");
            res.put("username",username);
            res.put("message","验证成功");
        }else {
            res.put("state","0");
            res.put("message","验证失败");
        }
        return res;
    }

}
