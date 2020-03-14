package com.sustech.conferenceSystem.controler.meeting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.service.meeting.MeetingQueryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 会议管理查询模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/meeting")
public class MeetingQueryControler {
    @Resource
    private MeetingQueryService meetingQueryService;

    /**
     * /meeting/meeting_search_all 接口，用于获取所有会议
     * @return
     */
    @RequestMapping(value = "/meeting_search_all",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String meetingGetAll(){
        List<MeetingSimple> result = meetingQueryService.meetingGetService();
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_search 接口，用于查找相关会议
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/meeting_search",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingSearch(@RequestBody JSONObject jsonParam){
        int page=jsonParam.getInteger("page");
        int volume=jsonParam.getInteger("volume");
        String meetingName=jsonParam.getString("meeting_name");
        String roomName=jsonParam.getString("room_name");
        String meetingState=jsonParam.getString("meeting_state");
        Integer userId=jsonParam.getInteger("user_id");
        Map<String,Object> result = meetingQueryService.meetingSearchService(userId,meetingName,roomName,meetingState,page,volume);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/user_meeting7_search 接口，获取用户7天内的会议
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/user_meeting7_search",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String userMeeting7Search(@RequestBody JSONObject jsonParam){
        Integer userId=jsonParam.getInteger("user_id");
        Map<String,Object> result = meetingQueryService.userMeeting7SearchService(userId);
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
        Integer meetingId=Integer.parseInt(jsonParam.getString("meeting_id"));
        MeetingFull result = meetingQueryService.meetingSearchCertainService(meetingId);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_search2 接口，用于查询指定房间的会议信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/meeting_search2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String meetingSearch2(@RequestBody JSONObject jsonParam){
        MeetingSimple meeting = JSON.parseObject(jsonParam.toString(), MeetingSimple.class);
        Map<String,Object> result=meetingQueryService.meetingSearch2Service(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_search3 接口，用于查询范围内会议信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/meeting_search3", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String meetingSearch3(@RequestBody JSONObject jsonParam){
        MeetingSimple meeting = JSON.parseObject(jsonParam.toString(), MeetingSimple.class);
        Room room = JSON.parseObject(jsonParam.toString(), Room.class);
        List<Map<String,Object>> result=meetingQueryService.meetingSearch3Service(meeting,room);
        return JSON.toJSONString(result);
    }
}
