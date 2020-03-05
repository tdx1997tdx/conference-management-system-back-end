package com.sustech.conferenceSystem.service.user;

import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            res.put("message","用户名或密码错误,或者权限选择错误");
        }else {
            String token= UUID.randomUUID().toString();
            Map<String,Object> map=new HashMap<>();
            map.put("userId",users.get(0).getUserId());
            map.put("username",users.get(0).getUsername());
            redisUtil.hmset(token,map,432000);
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
        boolean isSuccess;
        try{
            isSuccess=userMapper.addUser(user);
        }catch (Exception e){
            isSuccess=false;
        }
        if(isSuccess){
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
        Map<Object,Object> map=redisUtil.hmget(cookie);
        System.out.println(map.get("username")+" "+map.get("userId"));
        if(map.get("userId")!=null){
            res.put("state","1");
            res.put("username",(String)map.get("username"));
            res.put("user_id",String.valueOf(map.get("userId")));
            res.put("message","验证成功");
        }else {
            res.put("state","0");
            res.put("message","验证失败");
        }
        return res;
    }

    /**
     * 处理退出登陆业务逻辑
     * @param token 要删除的token
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> loginExitService(String token){
        Map<String,String> res=new HashMap<>();
        try {
            redisUtil.delete(token);
            res.put("state","1");
            res.put("message","删除成功");
        }catch (Exception e){
            res.put("state","0");
            res.put("message",e.getMessage());
        }

        return res;
    }

}
