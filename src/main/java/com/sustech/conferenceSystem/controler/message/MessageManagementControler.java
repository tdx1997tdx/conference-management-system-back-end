package com.sustech.conferenceSystem.controler.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.service.message.MessageManagementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制消息的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/message")
public class MessageManagementControler {
    // 此类接口均不会开放，此处仅为测试需要
    @Resource
    private MessageManagementService messageManagementService;

    /**
     * /message/message_add 接口，用于添加消息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageAdd(@RequestBody JSONObject jsonParam){
        Message message = JSON.parseObject(jsonParam.toString(), Message.class);
        boolean isSuccess = messageManagementService.messageAddService(message);

        Map<String,String> res=new HashMap<>();
        if(isSuccess){
            res.put("state","1");
            res.put("message","添加消息成功");
        }else {
            res.put("state","0");
            res.put("message","添加消息失败");
        }
        return JSON.toJSONString(res);
    }

    /**
     * /message/message_delete 接口，用于删除消息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageDelete(@RequestBody JSONObject jsonParam){
        int messageId = Integer.parseInt(jsonParam.getString("message_id"));
        boolean isSuccess = messageManagementService.messageDeleteService(messageId);

        Map<String,String> res=new HashMap<>();
        if(isSuccess){
            res.put("state","1");
            res.put("message","删除消息成功");
        }else {
            res.put("state","0");
            res.put("message","删除消息失败");
        }
        return JSON.toJSONString(res);
    }

    /**
     * /message/message_modify 接口，用于更改消息信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_modify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageModify(@RequestBody JSONObject jsonParam){
        Message message = JSON.parseObject(jsonParam.toString(), Message.class);
        boolean isSuccess = messageManagementService.messageModifyService(message);

        Map<String,String> res=new HashMap<>();
        if(isSuccess){
            res.put("state","1");
            res.put("message","更改消息成功");
        }else {
            res.put("state","0");
            res.put("message","更改消息失败");
        }
        return JSON.toJSONString(res);
    }

    /**
     * /message/message_have_read 接口，用于更改消息信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_have_read", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageRead(@RequestBody JSONObject jsonParam) {
        JSONArray messageArray = jsonParam.getJSONArray("message_id");
        int[] messageIds = new int[messageArray.size()];
        for (int i = 0; i < messageArray.size(); i++) {
            messageIds[i] = messageArray.getInteger(i);
        }
        boolean isSuccess = messageManagementService.messageReadService(messageIds);

        Map<String,String> res=new HashMap<>();
        if(isSuccess){
            res.put("state","1");
            res.put("message","更改消息成功");
        }else {
            res.put("state","0");
            res.put("message","更改消息失败");
        }
        return JSON.toJSONString(res);
    }

    /**
     * /message/message_all_have_read 接口，用于更改消息信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/message_all_have_read", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String messageALLRead(@RequestBody JSONObject jsonParam) {
        int userId = Integer.parseInt(jsonParam.getString("user_id"));
        boolean isSuccess = messageManagementService.messageALLReadService(userId);

        Map<String,String> res=new HashMap<>();
        if(isSuccess){
            res.put("state","1");
            res.put("message","更改消息成功");
        }else {
            res.put("state","0");
            res.put("message","更改消息失败");
        }
        return JSON.toJSONString(res);
    }
}
