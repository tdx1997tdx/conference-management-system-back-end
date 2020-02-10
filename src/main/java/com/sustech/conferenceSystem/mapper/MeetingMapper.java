package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.Meeting;

import java.util.List;

public interface MeetingMapper {

    /**
     * 返回所有会议内容数据
     * @return 符合要求会议集合
     */
    List<Meeting> meetingGet ();

    /**
     * 检查数据库中是否有符合要求的会议
     * @param meeting 其中有meetingName,roomName,meetingState,startTime,endTime。为空代表不传
     * @return 符合要求会议集合
     */
    List<Meeting> meetingSearch (Meeting meeting);

    /**
     * 预约一个会议
     * @param meeting 中有meetingRoom,startTime,endTime。
     *
     *
     * @return 符合要求会议集合
     */
    List<Meeting> meetingOrder (Meeting meeting);


    /**
     * 查询所创建的会议是否存在 / 与其他会议冲突
     * @param meeting 中有meetingName,中有meetingName,meetingRoom,startTime,endTime。
     */
    List<Meeting> isMeetingExist (Meeting meeting);

    /**
     * 创建一个会议
     * @param meeting 其中有meeting所需所有信息
     */
    void meetingCreate (Meeting meeting);

    /**
     * 取消一个会议
     * @param meeting 其中有meetingName,roomName,startTime,endTime。
     */
    void meetingDelete (Meeting meeting);
}
