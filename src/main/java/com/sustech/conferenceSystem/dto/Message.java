package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.sustech.conferenceSystem.service.inform.InformConstants.*;

@Data
public class Message {
    private Integer messageId;
    private String messageHeader; //消息问候语
    private String messageTopic; //消息主题
    private String messageBody; //消息内容
    private String reason; //消息发送缘由

    private Integer senderId; //发信人ID
    private String senderName; //发信人名字
    private Integer receiverId; //接收人ID
    private String receiverName; //接收人名字

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date sendTime; //发送时间

    private Integer haveRead; // 是否已读

    public Message() {

    }

    public Message(String reason) {
        this.reason = reason;
        this.messageTopic = "测试消息";
        this.senderId = 1;
        this.senderName = "System";
        this.sendTime = new Date();
        this.haveRead = NOT_READ;
    }
}
