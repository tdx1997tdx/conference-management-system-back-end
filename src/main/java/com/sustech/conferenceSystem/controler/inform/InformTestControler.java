package com.sustech.conferenceSystem.controler.inform;

import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.service.inform.InformService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 控制设备的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/inform")
public class InformTestControler {
    // inform功能测试模块，不会开放
    @Resource
    private InformService informService;

    /**
     * /inform/inform_all 接口，通知所有与服务器websocket相连用户
     * @return
     */
    @RequestMapping(value = "/inform_all",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String informAllTest() throws IOException {
        return informService.informAllTest();
    }

    /**
     * /inform/inform_first 接口，通知第一位用户 id:001 name:YYJ
     * @return
     */
    @RequestMapping(value = "/inform_first",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String informFirstUser() throws IOException {
        return informService.informFirstUser();
    }

    /**
     * /inform/inform_first 接口，通知第一位用户 id:001 name:YYJ
     * @return
     */
    @RequestMapping(value = "/inform_meeting",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String informMeeting(@RequestBody JSONObject jsonParam) throws IOException {
        long time=Integer.parseInt(jsonParam.getString("time"));
        System.out.println("informMeeting: " + time);
        return informService.informMeeting(time);
    }
}
