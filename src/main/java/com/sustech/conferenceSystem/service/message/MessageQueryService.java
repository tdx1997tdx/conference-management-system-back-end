package com.sustech.conferenceSystem.service.message;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sustech.conferenceSystem.service.inform.InformConstants.HAVE_READ;

/**
 * 控制消息的查询模块service层
 */
@Service
public class MessageQueryService {
    @Resource
    private MessageMapper messageMapper;


    /**
     * 处理获取所有会议的业务逻辑
     * @return 所有会议
     */
    public List<Message> messageGetAllService() {
        return messageMapper.getAllMessage();
    }

    /**
     * 查询指定消息详细信息
     * @param message 消息信息
     * @return 结果0或1
     */
    public Message messageDetailService(Message message){
        List<Message> res = messageMapper.searchMessage(message);
        return res.get(0);
    }

    /**
     * 模糊查询业务逻辑
     * @param messageName 消息名称
     * @param page 页数
     * @param volume 容量
     * @param userId 用户ID
     * @return 结果集合
     */
    public Map<String,Object> messageSearchService(String messageName, int page, int volume, int userId, int haveRead){
        Map<String,Object> res = new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Message> list=messageMapper.fuzzySearchMessage(messageName, userId, haveRead);
        PageInfo<Message> pageInfo=new PageInfo<>(list);
        int total_read = 0;
        for (Message msg: list) {
            if (msg.getHaveRead() == HAVE_READ) {
                total_read += 1;
            }
        }
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        res.put("total_read",total_read);
        return res;
    }

    /**
     * 分页查询业务逻辑
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public Map<String,Object> messageSearchPageService(int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Message> list=messageMapper.fuzzySearchMessage("", 0, 2);
        PageInfo<Message> pageInfo=new PageInfo<>(list);
        int total_read = 0;
        for (Message msg: list) {
            if (msg.getHaveRead() == HAVE_READ) {
                total_read += 1;
            }
        }
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        res.put("total_read",total_read);
        return res;
    }


}
