package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.UserAndMeeting;

public interface UserAndMeetingMapper {
    /**
     * 添加一个用户和会议的映射
     * @param userAndMeeting 映射信息
     */
    boolean addUserAndMeeting (UserAndMeeting userAndMeeting);
}
