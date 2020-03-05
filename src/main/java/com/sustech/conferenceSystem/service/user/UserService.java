package com.sustech.conferenceSystem.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 模糊查找相关用户在指定时间段的时间信息
     * @param page 分页第几页
     * @param volume 每页的容量
     * @return 结果集合list
     */
    public Map<String,Object> userSearchService(String name, Timestamp startTime, Timestamp endTime, int page, int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<User> list=userMapper.searchUserByTime(name,startTime,endTime);
        PageInfo<User> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

}
