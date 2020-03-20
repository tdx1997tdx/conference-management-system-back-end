package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 制定一个会议对象（概述版）
 */
@Data
public class MeetingSimple {
    private Integer meetingId;
    private String meetingName;
    private String roomName;
    private Integer roomId;
    private Room room;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date endTime;
    private String hostName;
    private Integer hostId;
    private String topic;
    private Integer meetingState;
    private Integer userState;
}
