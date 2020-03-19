package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.UserAndMeeting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAndMeetingMapper {
    /**
     * 添加一个用户和会议的映射
     * @param userAndMeeting 映射信息
     */
    boolean addUserAndMeeting (UserAndMeeting userAndMeeting);
    /**
     * 判断会议是否开始
     * @param meetingId 其中有meeting所需所有信息
     */
    List<MeetingSimple> isBegin(Integer meetingId);

    /**
     * 某人参加会议
     * @param meetingId 其中有meeting所需所有信息
     */
    void updateJoin(@Param("userId")Integer userId, @Param("meetingId")Integer meetingId, @Param("state")Integer state);
}
