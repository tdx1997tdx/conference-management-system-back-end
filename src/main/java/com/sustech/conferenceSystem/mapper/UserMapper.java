package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.User;

import java.util.List;

public interface UserMapper {

    /**
     * 检查数据库中是否有这个人物
     * @param user 其中有username,password,role为空代表不传
     * @return
     */
    List<User> isExistUser(User user);

    /**
     * 注册人物
     * @param user 其中有username,password,role,name,email
     * @return
     */
    void regist(User user);

}
