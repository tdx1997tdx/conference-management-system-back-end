package com.loginRegist.mapper;

import com.loginRegist.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where name = #{name} and password = #{password};")
    List<User> selectUser(@Param("name")String name,
                          @Param("password") String password);

    @Insert("insert into user(name,password) values (#{name},#{password});")
    void addUser(User user);
}
