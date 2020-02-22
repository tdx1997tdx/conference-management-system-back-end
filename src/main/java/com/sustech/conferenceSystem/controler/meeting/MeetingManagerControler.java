package com.sustech.conferenceSystem.controler.meeting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.service.meeting.MeetingManagerService;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 会议管理模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/meeting")
public class MeetingManagerControler {
    @Resource
    private MeetingManagerService meetingManagerService;

    /**
     * /meeting/meeting_order_search 接口，用于预约会议 (未实现)
     * @return
     */
    @RequestMapping(value = "/meeting_order_search",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingOrderSearch (@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        List<MeetingFull> result = meetingManagerService.meetingOrderService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_create 接口，用于创建会议 (未实现)
     * @return
     */
    @RequestMapping(value = "/meeting_add",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingAdd(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Filter.attributeFilter(meeting);
        System.out.println(meeting);
        Map<String, String> result = meetingManagerService.meetingAddService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_delete 接口，用于删除会议 (未实现)
     * @return
     */
    @RequestMapping(value = "/meeting_delete",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingDelete(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Filter.attributeFilter(meeting);
        Map<String, String> result = meetingManagerService.meetingDeleteService(meeting);
        return JSON.toJSONString(result);
    }
}
