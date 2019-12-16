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
    private Integer userId; //用户编号
    private String username; //用户名
    private String password; //密码
    private Integer role; //权限，0代表普通用户，1代表管理员，2代表部门经理
    private String email; //邮箱
    private String organization; //组织
    private String department; //部门
    private String position; //职位
    private String name; //姓名
    private String phone; //电话号码
    private String gender; //性别
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; //修改时间
}
