package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MeetingMapper {


    /**
     * @return 所有会议
     */
    List<MeetingSimple> meetingGetAll();

    /**
     * 根据id查找会议
     * @param meetingId
     * @return
     */
    MeetingSimple findMeetingById(int meetingId);

    /**
     * 检查数据库中是否有符合要求的会议
     * @return 符合要求会议集合
     */
    List<MeetingSimple> meetingSearch (@Param("userId")Integer userId, @Param("roomName")String roomName, @Param("meetingSimple")MeetingSimple meetingSimple);


    /**
     * 检查指定id用户未来7天内的所有会议。
     * @return 符合要求会议集合
     */
    List<MeetingSimple> userMeeting7Search (@Param("userId")Integer userId, @Param("dateNow") Date dateNow,@Param("dateEnd") Date dateEnd);

    /**
     * 检查数据库中是否有符合要求的会议
     * @param meetingSimple 会议名称，模糊搜索
     * @return 符合要求会议集合
     */
    List<MeetingSimple> meetingSearch2(MeetingSimple meetingSimple);

    /**
     * 检查数据库中查询某个user在某个时间段是否有会议
     * @return 符合要求会议集合
     */
    List<MeetingSimple> meetingIntervalSearch(@Param("userId")Integer userId,@Param("meetingSimple")MeetingSimple meetingSimple);

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
    List<User> findMeetingUnsign(Integer meetingId);

    /**
     * @param meetingId 会议id
     * @return 缺席的成员
     */
    List<User> findMeetingAttend(Integer meetingId);

    /**
     * @param meetingId 会议id
     * @return 到场的成员
     */
    List<User> findMeetingReject(Integer meetingId);

    /**
     * @param meetingId 会议id
     * @return 缺席的成员
     */
    List<User> findMeetingNoAccept(Integer meetingId);

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
    boolean meetingCreate (MeetingFull meeting);

    /**
     * 取消一个会议
     * @param meetingId
     */
    boolean meetingDelete (Integer meetingId);

    /**
     * 修改一个会议
     * @param meeting 其中有meeting所需所有信息
     */
    boolean meetingModify (MeetingFull meeting);

    /**
     * 往会议中添加成员
     * @param meeting 其中有meeting所需所有信息
     */
    boolean meetingMembersAdd(@Param("meeting")MeetingFull meeting);

    /**
     * 往会议中删除成员
     * @param meeting 其中有meeting所需所有信息
     */
    boolean meetingMembersDelete(@Param("meeting")MeetingFull meeting);


    List<MeetingSimple> meetingTimeDiffGet(@Param("diffTime")long diffTime, @Param("dateNow") Date dateNow, @Param("startOrEnd")int startOrEnd);
}
