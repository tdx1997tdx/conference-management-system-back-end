package com.sustech.conferenceSystem.service.message;

import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制消息的增删改模块service层
 */
@Service
public class MessageManagementService {
    @Resource
    private MessageMapper messageMapper;

    /**
     * 添加消息业务逻辑
     * @param message 消息信息
     * @return 结果0或1
     */
    public boolean messageAddService(Message message){
        return messageMapper.addMessage(message);
    }

    /**
     * 删除消息业务逻辑
     * @param messageId 消息信息
     * @return 结果0或1
     */
    public boolean messageDeleteService(int messageId){
        return messageMapper.deleteMessage(messageId);
    }

    /**
     * 更改消息业务逻辑
     * @param message 消息信息
     * @return 结果0或1
     */
    public boolean messageModifyService(Message message){
        if(messageMapper.findMessageById(message.getMessageId()) != null) {
            return messageMapper.updateMessage(message);
        } else {
            return false;
        }
    }

    /**
     * 更改用户ID所有信息为已读
     * @param userId 用户ID
     * @return 结果0或1
     */
    public boolean messageALLReadService(int userId){
        return messageMapper.messageALLRead(userId);
    }

    /**
     * 更改对应消息ID状态为已读
     * @param messageIds 消息ID数组
     * @return 结果0或1
     */
    public boolean messageReadService(int[]  messageIds){
        return messageMapper.messageRead(messageIds);
    }
}
