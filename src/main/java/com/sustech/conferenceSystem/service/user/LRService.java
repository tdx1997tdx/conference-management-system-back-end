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
    public Map<String,Object> loginService(User user){
        Map<String,Object> res=new HashMap<>();
        List<User> users=userMapper.searchUser(user);
        if(users.size()==0){
            res.put("state","0");
            res.put("message","用户名或密码错误,或者权限选择错误");
        }else {
            String token;
            token = UUID.randomUUID().toString().replaceAll(";","").replaceAll("=","");
            Map<String,Object> map=new HashMap<>();
            map.put("userId",users.get(0).getUserId());
            map.put("username",users.get(0).getUsername());
            map.put("name",users.get(0).getName());
            synchronized(users){
                redisUtil.hmset(token,map,432000);
                redisUtil.set(String.valueOf(users.get(0).getUserId()),token,432000);
            }
            res.put("state","1");
            res.put("user_id",users.get(0).getUserId());
            res.put("username",users.get(0).getUsername());
            map.put("name",users.get(0).getName());
            res.put("token",token);
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
     * @param token 传入待验证的token
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> loginVerificationService(String userId,String token){
        Map<String,String> res=new HashMap<>();
        Map<Object,Object> map=redisUtil.hmget(token);
        if(map.get("userId")!=null){
            res.put("username",(String)map.get("username"));
            res.put("name",(String)map.get("name"));
            res.put("user_id",String.valueOf(map.get("userId")));
            res.put("message","验证成功");
        }else {
            res.put("message","验证失败");
        }
        return res;
    }

    /**
     * 处理退出登陆业务逻辑
     * @param token 要删除的token
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> loginExitService(String userId,String token){
        Map<String,String> res=new HashMap<>();
        try {
            synchronized (res){
                redisUtil.delete(token);
                redisUtil.delete(userId);
            }
            res.put("state","1");
            res.put("message","删除成功");
        }catch (Exception e){
            res.put("state","0");
            res.put("message",e.getMessage());
        }

        return res;
    }

}
