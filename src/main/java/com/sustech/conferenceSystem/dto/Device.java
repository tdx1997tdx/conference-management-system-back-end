package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
public class Device {
    private String name; //设备名称
    private Room room; //会议室
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
    private String measures; //检修措施
}
