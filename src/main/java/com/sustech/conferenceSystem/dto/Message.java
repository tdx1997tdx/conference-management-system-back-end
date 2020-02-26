package com.sustech.conferenceSystem.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Message {
    private Integer messageId;
    private String messageTopic; //消息主题
    private String messageBody; //消息内容

//    private Integer receiverId; //接收人id
    private String receiverName; //接收人名字
//    private Integer senderId; //发信人id
    private String senderName; //发信人名字

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date sendTime; //发送时间

}
