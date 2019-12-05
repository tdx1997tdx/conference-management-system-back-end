package com.sustech.conferenceSystem.dto;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 用户类制定一个用户对象
 */
@Data
public class User {
    private int userId;
    private String username;
    private String password;
    private int role;
    private String email;
    private String organization;
    private String department;
    private String position;
    private String name;
    private String phone;
    private String gender;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date modifyTime;
    public User(){}
    public User(String username){
        this.username=username;
    }
    public User(String username,String password,int role){
        this.username=username;
        this.password=password;
        this.role=role;
    }
    /**
     * 将user属性中null字符串转成null
     * @param user
     * @return user
     */
    public static User attributeFilter(User user){
        try {
            // 获取obj类的字节文件对象
            Class c = user.getClass();
            // 获取该类的成员变量
            Field[] fs = c.getDeclaredFields();
            for(Field f:fs){
                // 取消语言访问检查
                f.setAccessible(true);
                // 给变量赋值
                Object o=f.get(user);
                if(o!=null&&o.toString().equals("null"))
                    f.set(user, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
