package com.sustech.conferenceSystem.controler.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 用户查询模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserSearchControler {
    @Resource
    private UserService userService;

    /**
     * /user/admin_search 接口，用查找相关用户信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/user_name_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String adminSearch(@RequestBody JSONObject jsonParam){
        String name=jsonParam.getString("name");
        Timestamp startTime=jsonParam.getTimestamp("start_time");
        Timestamp endTime=jsonParam.getTimestamp("end_time");
        int page=jsonParam.getInteger("page");
        int volume=jsonParam.getInteger("volume");
        Map<String,Object> result= userService.userSearchService(name,startTime,endTime,page,volume);
        return JSON.toJSONString(result);
    }



}
