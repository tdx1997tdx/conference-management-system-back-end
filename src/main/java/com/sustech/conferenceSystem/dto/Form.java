package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Form {
    private Integer formId;
    private String formName; //表单名称
    private Device device; //设备
    private Integer deviceId; //设备id
    private String deviceName; //设备名称
    private Room room; //会议室
    private Integer roomId; //会议室id
    private String roomName; //会议室名称
    private String repairMan; //检修人员
    private String serviceMan; //报修人员
    private String verifyMan; //验证人员
    private String reason; //报修原因（设备故障现象）
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date serviceTime; //报修时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date repairTime; //上门修理时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date finishTime; //报修完成时间
    private String measure; //检修措施
}
