package com.sustech.conferenceSystem.dto;
import lombok.Data;

import java.sql.Time;

@Data
public class Device {
    private Integer deviceId; //设备编号
    private String deviceName; //设备名称
    private String brand; //品牌
    private String deviceType; //设备类型
    private Integer repairTime; //维修次数
    private Room room; //会议室
    private Integer roomId; //会议室
    private Integer state; //状态
}
