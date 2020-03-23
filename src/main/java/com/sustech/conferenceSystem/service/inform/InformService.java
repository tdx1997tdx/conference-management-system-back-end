package com.sustech.conferenceSystem.service.inform;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.sustech.conferenceSystem.controler.inform.LongPullingController;
import com.sustech.conferenceSystem.controler.inform.WebSocketControler;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.service.message.MessageManagementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;

import static com.sustech.conferenceSystem.service.inform.InformConstants.*;
import static com.sustech.conferenceSystem.controler.inform.WebSocketControler.webSocketServerMAP;
import static com.sustech.conferenceSystem.controler.inform.LongPullingController.watchRequests;


@Component
public class InformService {

    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MessageManagementService messageManagementService;
    /**
     * 提醒参与会议的相关人员
     * @param meetingFull 会议对象
     * @param informReason 会议提醒理由（创建，修改等）
     * @return
     */
    public void meetingInform(MeetingFull meetingFull, InformReason informReason){
        Message message = new Message();
        message.setMessageTopic(generateMesaageTopic(informReason));
        message.setSendTime(new Date());

        String msgBody = generateMesaageBody(meetingFull, informReason);
        memberInform(meetingFull.getHost(), MeetingRole.HOST, message, msgBody);
        memberInform(meetingFull.getRecorder(), MeetingRole.RECORDER, message, msgBody);
        for (User user: meetingFull.getMembers()) {
            memberInform(user, MeetingRole.MEMBER, message, msgBody);
        }
    }

    /**
     * 提醒人员
     * @param user 人员对象
     * @param meetingRole 人员在会议中身份
     * @return
     */
    public void memberInform(User user, MeetingRole meetingRole, Message message, String msgBody) {
        // 暂定，后续可能会修改，未完成
        message.setMessageBody(generateMesaageHead(user, meetingRole) + msgBody);
        if (true) {
            try {
                messageInform(user.getUserId().toString(), user.getName(), message);
            } catch (IOException e) {
                // 日志报错， 未完成
                e.printStackTrace();
            }
        }
    }

    /**
     * 向指定ID，指定name的用户推送消息message
     * @param id 人员对象
     * @param name 人员在会议中身份
     * @param message 会议提醒理由（创建，修改等）
     * @throws IOException
     */
    public String messageInform(String id, String name, Message message) throws IOException{
        message.setReceiverName(name);

        String namespace = id + name;

        if (!messageManagementService.messageAddService(message)) {
            System.out.println("写入数据库失败");
            return "写入数据库失败";
        }
        WebSocketControler webSocketControler = webSocketServerMAP.get(namespace);
        if(webSocketControler != null){
            webSocketControler.sendMessage(message);
            return "Websocket success";
        }
        System.out.println("该用户未与服务器建立websocket连接 id:" + id + " name: " + name);
        if (watchRequests.containsKey(namespace)) {
            DeferredResult<Message> deferredResult = watchRequests.get(namespace);
            LongPullingController.sendMessage(message, deferredResult);
            return "LongPulling success";
        }
        System.out.println("该用户未与服务器建立long pulling连接 id:" + id + " name: " + name);
        return "未建立连接 id:" + id + " name: " + name + " namespace: " + namespace;
//        webSocketServer.session.getBasicRemote().sendText(message);
    }

    /**
     * 每1分钟执行一次
     * 检查每个会议，是否到需要通知与会人员 / 操作会议设备 （未实现）
     */
    @Scheduled(cron="0 * *  * * ? ")
    public void meetingCheck(){
        if (INFORM_TEST_ON) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("meetingCheck: 当前时间为："+sdf.format(dateNow));
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(BEFORE_MEETING_OPEN_INFORM, dateNow, START_TIME)) {
            // 会议开始前20分钟，不可更改会议信息，通知与会人员
            MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meetingSimple.getMeetingId());
            meetingInform(meetingFull, InformReason.OPENBEFORE);
        }
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(BEFORE_MEETING_OPEN, dateNow, START_TIME)) {
            // 会议开始前10分钟，自动开灯，电视机，空调，音响等设备（未完成）
            System.out.println("BEFORE_MEETING_OPEN "+sdf.format(dateNow));
        }
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(BEFORE_MEETING_CLOSE_INFORM, dateNow, END_TIME)) {
            // 会议结束之前15分钟，在会议平板（电视机）上显示提示信息（未完成）
        }
    }


    public String informAll(Message message) {
        int websocketNum = 0;
        StringBuilder websocketString = new StringBuilder("");
        Collection<WebSocketControler> websocketCollection = webSocketServerMAP.values();

        for (WebSocketControler item : websocketCollection) {
            try {
                messageInform(item.getId(), item.getName(), message);
                websocketNum++;
                websocketString.append("uri: " + item.getUri() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

        int longPullingNum = 0;
        Collection<DeferredResult<Message>> longPullingCollection = watchRequests.values();
        for (DeferredResult<Message> deferredResult : longPullingCollection) {
            // 设定收信人（未实现）
            deferredResult.setResult(message);
            longPullingNum++;
        }
        String res = "websocketNum: " + websocketNum + " longPullingNum: " + longPullingNum + "\n" + websocketString;
        return res;
    }

    /**
     * 定时任务，向所有客户端用户推送当前时间（每五秒一次）
     * 模拟向多人推送消息
     */
//    @Scheduled(cron="0/5 * *  * * ? ")
    public String informAllTest(){
//        if (INFORM_TEST_ON) {
//            return;
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("当前时间为：" + sdf.format(dateNow));
        String msg = "收到群发消息:" + "当前时间为：" + sdf.format(dateNow);

        Message message = new Message();
        message.setMessageTopic("测试发送消息");
        message.setMessageBody(msg);
        return informAll(message);
    }


    /**
     * 定时任务，向第一位客户端用户推送当前时间（每五秒一次）
     * 模拟向一位用户单独推送消息
     * 此处规定第一位用户ID为001，名字为yyj
     * @throws IOException
     */
    private static final String FIRST_USER_ID = "001";
    private static final String FIRST_USER_NAME = "YYJ";
//    @Scheduled(cron="0/5 * *  * * ? ")
//    public void informFirstUser() throws IOException{
//        if (INFORM_TEST_ON) {
//            return;
//        }
    public String informFirstUser() throws IOException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("当前时间为：" + sdf.format(dateNow));
        String msg = "收到给第一位用户的消息:" + "当前时间为：" + sdf.format(dateNow);

        Message message = new Message();
        message.setReceiverName(FIRST_USER_NAME);
        message.setMessageTopic("测试发送消息");
        message.setMessageBody(msg);
        return messageInform(FIRST_USER_ID, FIRST_USER_NAME, message);
    }

    // 测试方法
    public String informMeeting(long time) {
        Date dateNow = new Date();
        StringBuilder res = new StringBuilder();
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(time, dateNow, START_TIME)) {
            res.append("start: " + meetingSimple + "\n");
        }
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(time, dateNow, END_TIME)) {
            res.append("end: " + meetingSimple + "\n");
        }
        return res.toString();
    }
}