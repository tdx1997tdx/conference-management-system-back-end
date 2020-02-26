package com.sustech.conferenceSystem.service.inform;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
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

import static com.sustech.conferenceSystem.service.inform.WebSocketServer.webSocketServerMAP;


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
    public void memberInform(User user, Role role, Reason reason) {
        // 暂定，后续可能会修改，未完成
        String message = generateMesaage(reason);
        try {
            inform(user.getUserId().toString(), user.getName(), message);
        } catch (IOException e) {
            // 日志报错， 未完成
            e.printStackTrace();
        }
    }

    /**
     * 根据reason生成提醒内容
     * @param reason 会议提醒理由（创建，修改等）
     * @return
     */
    public String generateMesaage(Reason reason) { // 未完成
        return "";
    }

    /**
     * 向指定ID，指定name的用户推送消息message
     * @param id 人员对象
     * @param name 人员在会议中身份
     * @param message 会议提醒理由（创建，修改等）
     * @throws IOException
     */
    public void inform(String id, String name, String message) throws IOException{
        StringBuilder receiverUri = new StringBuilder(InformConstants.WEBSOCKET_URI);
        receiverUri.append(id)
                .append("/")
                .append(name);
        WebSocketServer webSocketServer = webSocketServerMAP.get(receiverUri.toString());
//        写入数据库（未完成）
        if(webSocketServer != null){
            webSocketServer.sendMessage(message);
//            webSocketServer.session.getBasicRemote().sendText(message);
        } else {
            System.out.println("该用户未与服务器建立连接 id:" + id + " name: " + name);
        }
    }
    /**
     * 每1分钟执行一次
     * 检查每个会议，是否到需要通知与会人员 / 操作会议设备 （未实现）
     */
    @Scheduled(cron="0 * *  * * ? ")
    public void meetingCheck(){
        if (!InformConstants.INFORM_TEST_ON) {
            return;
        }
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

    /**
     * 定时任务，向所有客户端用户推送当前时间（每五秒一次）
     * 模拟向多人推送消息
     */
    @Scheduled(cron="0/5 * *  * * ? ")
    public void informAll() throws IOException{
        if (!InformConstants.INFORM_TEST_ON) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("当前时间为：" + sdf.format(dateNow));
        String message = "收到群发消息:" + "当前时间为：" + sdf.format(dateNow);
        Collection<WebSocketServer> collection = webSocketServerMAP.values();
        for (WebSocketServer item : collection) {
            try {
                inform(item.getId(), item.getName(), message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 定时任务，向第一位客户端用户推送当前时间（每五秒一次）
     * 模拟向一位用户单独推送消息
     * 此处规定第一位用户ID为001，名字为yyj
     * @throws IOException
     */
    private static final String FIRST_USER_ID = "001";
    private static final String FIRST_USER_NAME = "yyj";
    @Scheduled(cron="0/5 * *  * * ? ")
    public void informFirstUser() throws IOException{
        if (!InformConstants.INFORM_TEST_ON) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("当前时间为：" + sdf.format(dateNow));
        String message = "收到给第一位用户的消息:" + "当前时间为：" + sdf.format(dateNow);

        inform(FIRST_USER_ID, FIRST_USER_NAME, message);
    }

}