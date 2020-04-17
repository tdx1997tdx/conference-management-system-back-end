package com.sustech.conferenceSystem.service.inform;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.controler.inform.LongPullingController;
import com.sustech.conferenceSystem.controler.inform.WebSocketControler;
import com.sustech.conferenceSystem.dto.*;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.mqttService.MqttUtil;
import com.sustech.conferenceSystem.service.message.MessageManagementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;

import static com.sustech.conferenceSystem.service.inform.InformConstants.*;


@Component
public class InformService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private MqttUtil mqttUtil;
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
        message.setMessageBody(generateMesaageBody(meetingFull, informReason));
        message.setSendTime(new Date());
        User host = meetingFull.getHost();
        message.setSenderId(host.getUserId());
        message.setSenderUserName(host.getUsername());
        message.setSenderName(host.getName());

        memberInform(host, MeetingRole.HOST, message);
        memberInform(meetingFull.getRecorder(), MeetingRole.RECORDER, message);
        for (User user: meetingFull.getMembers()) {
            if (user.getUserId().equals(host.getUserId()) ||
                user.getUserId().equals(meetingFull.getRecorder().getUserId())) {
                continue;
            }
            memberInform(user, MeetingRole.MEMBER, message);
        }
    }

    /**
     * 提醒人员
     * @param user 人员对象
     * @param meetingRole 人员在会议中身份
     * @return
     */
    public void memberInform(User user, MeetingRole meetingRole, Message message) {
        System.out.println("memberInform id: " + user.getUserId() + " userName: " + user.getUsername() + " name: " + user.getName());

        if (user.getUsername() == null || user.getName() == null) {
            System.out.println("memberInform before" + user);
            user = userMapper.findUserById(user.getUserId());
            System.out.println("memberInform after" + user);
        }
        message.setMessageHeader(generateMesaageHead(user, meetingRole));
        message.setReceiverId(user.getUserId());
        message.setReceiverUserName(user.getUsername());
        message.setReceiverName(user.getName());

        if (!messageManagementService.messageAddService(message)) {
            System.out.println("写入数据库失败");
            return;
            // 失败后处理（未完成）
        }

        try {
            messageInform(user.getUserId(), user.getUsername(), message);
        } catch (IOException e) {
            // 日志报错， 未完成
            e.printStackTrace();
        }
    }

    /**
     * 向指定ID，指定name的用户推送消息message
     * @param id 人员对象
     * @param name 人员在会议中身份
     * @param message 会议提醒理由（创建，修改等）
     * @throws IOException
     */
    public String messageInform(int id, String name, Message message) throws IOException{
        String namespace = id + name;

        System.out.println("messageInform id:" + id + " name: " + name);
        WebSocketControler webSocketControler = webSocketServersMAP.get(namespace);
        if(webSocketControler != null){
            webSocketControler.sendMessage(message);
            return "Websocket success";
        }
        System.out.println("该用户未与服务器建立websocket连接 id:" + id + " name: " + name);

        if (deferredResultsMap.containsKey(namespace)) {
            DeferredResult<Message> deferredResult = deferredResultsMap.get(namespace);
            LongPullingController.sendMessage(message, deferredResult);
            return "LongPulling success";
        }
        System.out.println("该用户未与服务器建立long pulling连接 id:" + id + " name: " + name);


        return "未建立连接 id:" + id + " name: " + name + " namespace: " + namespace;
    }

    /**
     * 每1分钟执行一次
     * 检查每个会议，是否到需要通知与会人员 / 操作会议设备
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
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(BEFORE_MEETING_OPEN_SWITCH_ON, dateNow, START_TIME)) {
            // 会议开始前10分钟，自动开灯，电视机，空调，音响等设备
            System.out.println("BEFORE_MEETING_OPEN "+sdf.format(dateNow));
            //根据房间获取所有设备信息
            Device d=new Device();
            d.setRoomId(meetingSimple.getRoomId());
            List<Device> devices=deviceMapper.searchDevice(d);
            for(Device dev:devices){
                Map<String,String> message=new HashMap<>();
                message.put("devcie_id",dev.getDeviceId()+"");
                message.put("command","on");
                mqttUtil.publish(meetingSimple.getRoomId()+"", JSON.toJSONString(message));
            }
        }
        for (MeetingSimple meetingSimple: meetingMapper.meetingTimeDiffGet(BEFORE_MEETING_CLOSE_INFORM, dateNow, END_TIME)) {
            // 会议结束之前15分钟，在会议平板（电视机）上显示提示信息（未完成）
            System.out.println("BEFORE_MEETING_CLOSE_INFORM "+sdf.format(dateNow));

        }
    }

    public String informAll(Message message) throws IOException {
        System.out.println("informAll");
        int websocketNum = 0;
        int longPullingNum = 0;
        StringBuilder websocketString = new StringBuilder("");
        StringBuilder longPullingString = new StringBuilder("");

        for (Map.Entry<String, LinkState> entry: LinkStatesMap.entrySet()) {
            System.out.println("key="+entry.getKey()+"  value="+entry.getValue());
            if (entry.getValue() == LinkState.WEBSOCKET) {
                WebSocketControler webSocketControler = webSocketServersMAP.get(entry.getKey());
                webSocketControler.sendMessage(message);
                websocketNum++;
                websocketString.append("websocket namespace: " + entry.getKey() + "\n");
            } else if (entry.getValue() == LinkState.LONG_PULLING) {
                DeferredResult<Message> deferredResult = deferredResultsMap.get(entry.getKey());
                deferredResult.setResult(message);
                longPullingNum++;
                longPullingString.append("longPulling namespace: " + entry.getKey() + "\n");
            }
        }

        String res = "websocketNum: " + websocketNum + "\n" + websocketString +
                     "longPullingNum: " + longPullingNum + "\n" + longPullingString;
        return res;
    }

    public String informAll2(Message message) throws IOException {
        int websocketNum = 0;
        int longPullingNum = 0;
        StringBuilder websocketString = new StringBuilder("");
        StringBuilder longPullingString = new StringBuilder("");

        Collection<WebSocketControler> websocketCollection = webSocketServersMAP.values();
        for (WebSocketControler item : websocketCollection) {
            messageInform(item.getId(), item.getName(), message);
            websocketNum++;
            websocketString.append("uri: " + item.getUri() + "\n");
        }

        Collection<DeferredResult<Message>> longPullingCollection = deferredResultsMap.values();
        for (DeferredResult<Message> deferredResult : longPullingCollection) {
            deferredResult.setResult(message);
            longPullingNum++;
        }
        String res = "websocketNum: " + websocketNum + "\n" + websocketString +
                "longPullingNum: " + longPullingNum + "\n" + longPullingString;
        return res;
    }

    /**
     * 定时任务，向所有客户端用户推送当前时间（每五秒一次）
     * 模拟向多人推送消息
     */
//    @Scheduled(cron="0/5 * *  * * ? ")
    public String informAllTest() throws IOException {
//        if (INFORM_TEST_ON) {
//            return;
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        System.out.println("当前时间为：" + sdf.format(dateNow));
        String msg = "收到群发消息:" + "当前时间为：" + sdf.format(dateNow);

        Message message = new Message("InformService->informAllTest");
        message.setMessageBody(msg);
        return informAll(message);
    }


    /**
     * 定时任务，向第一位客户端用户推送当前时间（每五秒一次）
     * 模拟向一位用户单独推送消息
     * 此处规定第一位用户ID为001，名字为yyj
     * @throws IOException
     */
    private static final int FIRST_USER_ID = 1;
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

        Message message = new Message("InformService->informFirstUser");
        message.setMessageBody(msg);
        message.setReceiverId(FIRST_USER_ID);
        message.setReceiverUserName(FIRST_USER_NAME);
        message.setReceiverName(FIRST_USER_NAME);
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

    public void informHost(int userId, int meetingId) {
        MeetingSimple meeting = meetingMapper.findMeetingById(meetingId);
        User user = userMapper.findUserById(userId);
        User host = userMapper.findUserById(meeting.getHostId());
        Message message = new Message();
        InformReason informReason = InformReason.REJECT;
        message.setMessageTopic(generateMesaageTopic(informReason));
        message.setMessageBody(generateRejectBody(meeting, user));
        message.setSendTime(new Date());
        message.setSenderId(user.getUserId());
        message.setSenderUserName(user.getUsername());
        message.setSenderName(user.getName());
        memberInform(host, MeetingRole.HOST, message);
    }
}