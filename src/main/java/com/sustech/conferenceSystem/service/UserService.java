package com.sustech.conferenceSystem.service;
import com.github.pagehelper.PageHelper;
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
     * 处理修改用户信息的业务逻辑
     * @param user 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> modifyInfoService(User user){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess=userMapper.modify(user);
        if(!isSuccess){
            res.put("state","0");
            res.put("message","修改用户信息失败");
        }else {
            res.put("state","1");
            res.put("message","修改用户信息成功");
        }
        return res;
    }

    /**
     * 模糊查找相关用户信息
     * @param page 分页第几页
     * @param volume 每页的容量
     * @return 结果集合list
     */
    public List<User> adminSearchService(String username,int page,int volume){
        PageHelper.startPage(page, volume);
        List<User> res=userMapper.fuzzySearchUser(username);
        return res;
    }

    /**
     * 返回所有用户信息
     */
    public List<User> adminSearchAllService(){
        List<User> res=userMapper.fuzzySearchUser("");
        return res;
    }

}
