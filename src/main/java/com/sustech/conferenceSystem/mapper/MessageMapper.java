package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageMapper {

    /**
     * @return 所有消息集合
     */
    List<Message> getAllMessage();


    /**
     *
     * 注册一个message
     * @param message
     * @return
     */
    boolean addMessage(Message message);

    /**
     * 删除一个message
     * @param messageId 唯一的消息id
     * @return
     */
    boolean deleteMessage(int messageId);

    /**
     * 修改一个message
     * @param message 消息
     * @return
     */
    boolean updateMessage(Message message);

    /**
     * 查询符合限制的消息
     * @param message 消息
     * @return
     */
    List<Message> searchMessage(Message message);

    /**
     * 模糊查询消息
     * @param messageName 消息名字
     * @param userId 用户ID
     * @return
     */
    List<Message> fuzzySearchMessage(@Param("messageName") String messageName, @Param("userId") int userId, @Param("haveRead") int haveRead);


    /**
     * 根据id查找消息
     * @param messageId
     * @return
     */
    Message findMessageById(int messageId);

    /**
     * 更改用户ID所有信息为已读
     * @param userId 用户ID
     * @return
     */
    boolean messageALLRead(@Param("userId") int userId);

    /**
     * 更改对应消息ID状态为已读
     * @param messageIds 消息ID数组
     * @return 结果0或1
     */
    boolean messageRead(@Param("messageIds")int[] messageIds);

}
