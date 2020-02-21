package com.sustech.conferenceSystem.service.inform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import com.sustech.conferenceSystem.service.inform.InformConstants.*;



@Component
public class Inform {

    @Resource
    private MeetingMapper meetingMapper;
    /**
     * 提醒参与会议的相关人员
     * @param meetingFull 会议对象
     * @param reason 会议提醒理由（创建，修改等）
     * @return
     */
    public void meetingInform(MeetingFull meetingFull, Reason reason){
        memberInform(meetingFull.getHost(), Role.HOST, reason);
        memberInform(meetingFull.getRecorder(), Role.RECORDER, reason);
        for (User user: meetingFull.getMembers()) {
            memberInform(user, Role.MEMBER, reason);
        }
    }

    /**
     * 提醒人员
     * @param user 人员对象
     * @param role 人员在会议中身份
     * @param reason 会议提醒理由（创建，修改等）
     * @return
     */
    public void memberInform(User user, Role role, Reason reason) { // 未完成
    }

    /**
     * 每1分钟执行一次
     * 检查每个会议，是否到需要通知与会人员 / 操作会议设备
     * @return
     */
    @Scheduled(cron="0 * *  * * ? ")
    public void meetingCheck(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("当前时间为："+sdf.format(dateNow));

//        for (MeetingSimple meetingSimple: meetingMapper.meetingGetAll()) {
//            // 需要在sql级别进行优化，减少检索范围（未完成）
//            if (isDateDiffMin(meetingSimple.getStartTime(), dateNow, InformConstants.BEFORE_MEETING_OPEN_INFORM)) {
//                // 会议开始前20分钟，不可更改会议信息，通知与会人员
//                MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meetingSimple.getMeetingId());
//                meetingInform(meetingFull, Reason.OPENBEFORE);
//            } else if (isDateDiffMin(meetingSimple.getStartTime(), dateNow, InformConstants.BEFORE_MEETING_OPEN)) {
//                // 会议开始前10分钟，自动开灯，电视机，空调，音响等设备（未完成）
//            } else if (isDateDiffMin(dateNow, meetingSimple.getStartTime(), InformConstants.BEFORE_MEETING_CLOSE_INFORM)) {
//                // 会议结束之前10分钟，在会议平板（电视机）上显示提示信息（未完成）
//            }
//        }
    }

    /**
     * 检测两个日期是否相差指定时间，a在b前
     * @param a 日期1,
     * @param b 日期2
     * @param min 相差多少分钟
     * @return
     */
    public boolean isDateDiffMin(Date a, Date b, long min) {
        long diff = b.getTime() - a.getTime(); // 差值是微秒级别
        long minutes  = diff / (1000 * 60);
        if(minutes == min) {
            return true;
        } else {
            return false;
        }
    }
}