package com.sustech.conferenceSystem.controler.meeting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.service.meeting.MeetingManagerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
     * /meeting/meeting_modify 接口，用于修改会议
     * @return
     */
    @RequestMapping(value = "/meeting_modify",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingModify (@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Map<String, String> result = meetingManagerService.meetingModifyService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_add 接口，用于创建会议
     * @return
     */
    @RequestMapping(value = "/meeting_add",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingAdd(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
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
        int meetingId=jsonParam.getInteger("meeting_id");
        Map<String, String> result = meetingManagerService.meetingDeleteService(meetingId);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_members_add 接口，用于添加会议成员
     * @return
     */
    @RequestMapping(value = "/meeting_members_add",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingMembersAdd(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Map<String, String> result = meetingManagerService.meetingMembersAddService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/meeting_members_delete 接口，用于删除会议成员
     * @return
     */
    @RequestMapping(value = "/meeting_members_delete",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String meetingMembersDelete(@RequestBody JSONObject jsonParam){
        MeetingFull meeting = JSON.parseObject(jsonParam.toString(), MeetingFull.class);
        Map<String, String> result = meetingManagerService.meetingMembersDeleteService(meeting);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/sign_in 接口，用于签到模块
     * @return
     */
    @RequestMapping(value = "/sign_in",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String signIn(@RequestBody JSONObject jsonParam){
        int userId=jsonParam.getInteger("user_id");
        int meetingId=jsonParam.getInteger("meeting_id");
        Map<String, String> result = meetingManagerService.signInService(userId,meetingId);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/accept 接口，用于接受会议请求
     * @return
     */
    @RequestMapping(value = "/accept",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String accept(@RequestBody JSONObject jsonParam){
        int userId=jsonParam.getInteger("user_id");
        int meetingId=jsonParam.getInteger("meeting_id");
        Map<String, String> result = meetingManagerService.acceptService(userId,meetingId);
        return JSON.toJSONString(result);
    }

    /**
     * /meeting/reject 接口，用于拒绝会议请求
     * @return
     */
    @RequestMapping(value = "/reject",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String reject(@RequestBody JSONObject jsonParam){
        int userId=jsonParam.getInteger("user_id");
        int meetingId=jsonParam.getInteger("meeting_id");
        Map<String, String> result = meetingManagerService.rejectService(userId,meetingId);
        return JSON.toJSONString(result);
    }
}
