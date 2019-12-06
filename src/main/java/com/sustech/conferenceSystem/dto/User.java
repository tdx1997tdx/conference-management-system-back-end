package com.sustech.conferenceSystem.dto;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import java.util.Date;

/**
 * 用户类制定一个用户对象
 */
@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private Integer role;
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
}
