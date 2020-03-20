package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 制定一个会议对象（完整版）
 */
@Data
public class MeetingFull {
    private Integer meetingId;
    private String meetingName;
    private Room room;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
    @JSONField(format="yyyy-MM-dd HH:mm:ss")//数据库导出页面时json格式化
    private Date endTime;
    private User host;
    private User recorder;
    private List<User> members;
    private String topic;
    private String meetingAbstract;
    private List<User> noAccept;//等待接受成员
    private List<User> reject;//已拒绝成员
    private List<User> unSign;//未签到成员
    private List<User> attendance;//签到成员
    private String remark;
    private Integer meetingState;
}
