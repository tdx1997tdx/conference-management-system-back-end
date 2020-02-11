package com.sustech.conferenceSystem.controler.meeting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.service.meeting.MeetingService;
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
public class MeetingControler {
    @Resource
    private MeetingService meetingService;

    /**
     * /meeting/meeting_search 接口，用于查找相关会议
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/meeting_search",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingSearch(@RequestBody JSONObject jsonParam){
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        String meetingName=jsonParam.getString("meeting_name");
        String roomName=jsonParam.getString("room_name");
        String meetingState=jsonParam.getString("meeting_state");
        Map<String,Object> result = meetingService.meetingSearchService(meetingName,roomName,meetingState,page,volume);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_search_certain 接口，用于查找会议具体信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/meeting_search_certain",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingSearchCertain(@RequestBody JSONObject jsonParam){
        int meetingId=Integer.parseInt(jsonParam.getString("meeting_id"));
        MeetingFull result = meetingService.meetingSearchCertainService(meetingId);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_search_all 接口，用于获取所有会议
     * @return
     */
    @RequestMapping(value = "/meeting_search_all",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String meetingGetAll(){
        List<MeetingSimple> result = meetingService.meetingGetService();
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_order_search 接口，用于获取所有会议
     * @return
     */
    @RequestMapping(value = "/meeting_order_search",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingOrderSearch (@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        List<MeetingFull> result = meetingService.meetingOrderService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_create 接口，用于创建会议
     * @return
     */
    @RequestMapping(value = "/meeting_create",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingCreate(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Filter.attributeFilter(meeting);
        //需要加判断
        Map<String, String> result = meetingService.meetingCreateService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_delete 接口，用于删除会议
     * @return
     */
    @RequestMapping(value = "/meeting_delete",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingDelete(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Filter.attributeFilter(meeting);
        Map<String, String> result = meetingService.meetingDeleteService(meeting);
        return JSON.toJSONString(result);
    }
}
