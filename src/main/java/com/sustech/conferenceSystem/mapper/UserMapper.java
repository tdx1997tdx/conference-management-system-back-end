package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface UserMapper {

    /**
     * 检查数据库中是否有这个人物
     * @param user 其中有username,password,role为空代表不传
     * @return
     */
    List<User> searchUser(User user);

    /**
     * 注册人物
     * @param user 其中有username,password,role,name,email
     * @return
     */
    boolean addUser(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    boolean modify(User user);

    /**
     * 模糊查找人物
     * @param username 代表模糊查询字段
     * @return
     */
    List<User> fuzzySearchUser(String username);

    /**
     * 删除用户
     * @param user 要删除的人物
     * @return
     */
    boolean deleteUser(User user);

    /**
     * 根据id查找用户
     * @param userId
     * @return
     */
    User findUserById(int userId);

    /**
     * 模糊查找在指定时间段空闲的人物
     * @return
     */
    List<User> searchUserByTime(@Param("name")String name, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);

}
