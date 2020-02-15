package com.sustech.conferenceSystem.service.user;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.dto.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserManagementService {

    @Resource
    private UserMapper userMapper;

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
     * 处理显示用户信息的业务逻辑
     * @param user 传入javabean的user对象
     * @return 查找到的user
     */
    public User showInfoService(User user){
        List<User> users=userMapper.searchUser(user);
        return users.get(0);
    }


}
