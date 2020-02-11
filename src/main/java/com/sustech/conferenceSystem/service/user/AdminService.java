package com.sustech.conferenceSystem.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Resource
    private UserMapper userMapper;

    /**
     * 模糊查找相关用户信息
     * @param page 分页第几页
     * @param volume 每页的容量
     * @return 结果集合list
     */
    public Map<String,Object> adminSearchService(String username,int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<User> list=userMapper.fuzzySearchUser(username);
        PageInfo<User> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    /**
     * 返回所有用户信息
     */
    public Map<String,Object> adminSearchPageService(int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<User> list=userMapper.fuzzySearchUser("");
        PageInfo<User> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    /**
     * 处理删除用户信息的业务逻辑
     * @param user 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> adminDeleteService(User user){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess=userMapper.deleteUser(user);
        if(!isSuccess){
            res.put("state","0");
            res.put("message","删除用户失败");
        }else {
            res.put("state","1");
            res.put("message","删除用户成功");
        }
        return res;
    }

}
