package com.sustech.conferenceSystem.dto;

import lombok.Data;

/**
 * 用户类制定一个用户和会议映射对象
 */
@Data
public class UserAndMeeting {
    private Integer userId;
    private Integer meetingId;
    private Integer state;//参会状态0 缺席状态1 空闲状态2 接受状态3 拒绝状态4
    private String message;
}
