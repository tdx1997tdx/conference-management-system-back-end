package com.sustech.conferenceSystem.service.inform;

import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.User;

import java.text.SimpleDateFormat;

public class InformConstants {
    enum MeetingRole {HOST, RECORDER, MEMBER}
    public enum InformReason {CREATE, MODIFY, DELETE, OPENBEFORE, CLOSEBEFORE}
    public static final long BEFORE_MEETING_OPEN_INFORM = 20;
    public static final long BEFORE_MEETING_OPEN = 10;
    public static final long BEFORE_MEETING_CLOSE_INFORM = 15;
    public static final int START_TIME = 0;
    public static final int END_TIME = 1;
    public static final int HAVE_READ = 1;
    public static final int NOT_READ = 0;

    public static final boolean INFORM_TEST_ON = false;

    public static final String CREATE_MESSAGE_BODY = "您在%s有一场主题为%s的会议需要出席，请留意时间提前准备\n";
    public static final String MODIFY_MESSAGE_BODY = "您在%s出席的主题为%s的会议已被人改动，请提前查看会议修改信息并提前准备\n";
    public static final String DELETE_MESSAGE_BODY = "您在%s出席的主题为%s的会议已被删除，请留意后续通知\n";
    public static final String OPENBEFORE_MESSAGE_BODY = "您在%s出席的主题为%s的会议离开始还有20分钟，请做好会议内容准备\n";
    public static final String[] MESSAGE_BODY = {CREATE_MESSAGE_BODY, MODIFY_MESSAGE_BODY, DELETE_MESSAGE_BODY, OPENBEFORE_MESSAGE_BODY};

    public static final String MESSAGE_BODY_HEAD = "尊敬的%s先生/女士\n";

    public static final String CREATE_MESSAGE_TOPIC = "会议创建通知\n";
    public static final String MODIFY_MESSAGE_TOPIC = "会议修改通知\n";
    public static final String DELETE_MESSAGE_TOPIC = "会议删除通知\n";
    public static final String OPENBEFORE_MESSAGE_TOPIC = "会议开始20分前通知";
    public static final String[] MESSAGE_TOPIC = {CREATE_MESSAGE_TOPIC, MODIFY_MESSAGE_TOPIC, DELETE_MESSAGE_TOPIC, OPENBEFORE_MESSAGE_TOPIC};

    /**
     * 根据会议提醒理由与会议详情生成消息内容（主体）
     * @param meetingFull 会议详情
     * @param informReason 会议提醒理由（创建，修改等）
     * @return
     */
    public static String generateMesaageBody(MeetingFull meetingFull, InformReason informReason) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(meetingFull.getStartTime());
        String meetingName = meetingFull.getMeetingName();
        return String.format(MESSAGE_BODY[informReason.ordinal()], dateStr, meetingName);
    }
    /**
     * 根据会议提醒理由与会议详情生成消息内容头部（问候语）
     * @param user 会议参与者
     * @param meetingRole 参与者身份 (未使用)
     * @return
     */
    public static String generateMesaageHead(User user, MeetingRole meetingRole) {
        String userName = user.getUsername();
        return String.format(MESSAGE_BODY_HEAD, userName);
    }

    public static String generateMesaageTopic(InformReason informReason) {
        return String.format(MESSAGE_TOPIC[informReason.ordinal()]);
    }
}
