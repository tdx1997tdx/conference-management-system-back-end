package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeetingMapper {


    /**
     * @return 所有会议
     */
    List<MeetingSimple> meetingGetAll();

    /**
     * 检查数据库中是否有符合要求的会议
     * @param meetingName 会议名称，模糊搜索
     * @param roomName 会议室名称
     * @param meetingState 会议状态
     * @return 符合要求会议集合
     */
    List<MeetingSimple> meetingSearch (@Param("meetingName")String meetingName, @Param("roomName")String roomName, @Param("meetingState")String meetingState);

    /**
     * 查找指定id会议
     * @param meetingId
     * @return
     */
    MeetingFull meetingSearchCertain (int meetingId);

    /**
     * @param meetingId 会议id
     * @return 参加会议的成员
     */
    List<User> findMeetingMembers(Integer meetingId);

    /**
     * @param meetingId 会议id
     * @return 到场的成员
     */
    List<User> findMeetingAttend(Integer meetingId);

    /**
     * @param meetingId 会议id
     * @return 缺席的成员
     */
    List<User> findMeetingAbsent(Integer meetingId);

    /**
     * 预约一个会议
     * @param meeting 中有meetingRoom,startTime,endTime。
     * @return 符合要求会议集合
     */
    List<MeetingFull> meetingOrder (MeetingFull meeting);


    /**
     * 查询所创建的会议是否存在 / 与其他会议冲突
     * @param meeting 中有meetingName,中有meetingName,meetingRoom,startTime,endTime。
     */
    List<MeetingFull> isMeetingExist (MeetingFull meeting);

    /**
     * 创建一个会议
     * @param meeting 其中有meeting所需所有信息
     */
    void meetingCreate (MeetingFull meeting);

    /**
     * 取消一个会议
     * @param meeting 其中有meetingName,roomName,startTime,endTime。
     */
    void meetingDelete (MeetingFull meeting);

}
