package com.sustech.conferenceSystem.controler.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.service.message.MessageQueryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 控制消息的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/message")
public class MessageQueryControler {
    @Resource
    private MessageQueryService messageQueryService;

    /**
     * /message/message_search_all 接口，用于获取所有消息
     * @return
     */
    @RequestMapping(value = "/message_search_all", 
            method = RequestMethod.GET, 
            produces = "application/json;charset=UTF-8")
    public String messageGetAll(){
        List<Message> result = messageQueryService.messageGetAllService();
        return JSON.toJSONString(result);
    }
    
    /**
     * /message/message_detail 接口，接收指定id, 用于显示消息相关信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageDetail(@RequestBody JSONObject jsonParam){
        Message message = JSON.parseObject(jsonParam.toString(), Message.class);
        Message result = messageQueryService.messageDetailService(message);
        return JSON.toJSONString(result);
    }

    /**
     * /message/message_search 接口，根据名称模糊分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageSearch(@RequestBody JSONObject jsonParam){
        String messageName = jsonParam.getString("message_topic");
        int userId = Integer.parseInt(jsonParam.getString("user_id"));
        int page = Integer.parseInt(jsonParam.getString("page"));
        int volume = Integer.parseInt(jsonParam.getString("volume"));
        Map<String, Object> result=messageQueryService.messageSearchService(messageName, page, volume, userId);
        return JSON.toJSONString(result);
    }

    /**
     * /message/message_search_page 接口，用于分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_search_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageSearchPage(@RequestBody JSONObject jsonParam){
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map<String, Object> result=messageQueryService.messageSearchPageService(page, volume);
        return JSON.toJSONString(result);
    }
}
