package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * room类制定一个room对象
 */
@Data
public class Room {
    private Integer roomId; //会议室id号
    private String roomName; //会议室名称
    private String country; //国家
    private String province; //省
    private String city; //市
    private String block; //街区
    private String building; //楼
    private String floor; //层号
    private Integer roomNumber; //门牌号
    private Integer roomVolume; //会议室容量
    private String mark; //备注
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; //修改时间
}
