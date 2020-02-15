package com.sustech.conferenceSystem.controler.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.service.user.UserManagementService;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户管理模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserManagementControler {
    @Resource
    private UserManagementService userManagementService;

    /**
     * /user/modify_info 接口，普通用户用于修改用户信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/modify_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String modifyInfo(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Filter.attributeFilter(user);
        Map result=userManagementService.modifyInfoService(user);
        return JSON.toJSONString(result);
    }

    /**
     * /user/show_info 接口，普通用户用于显示用户信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/show_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String showInfo(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        User u=userManagementService.showInfoService(user);
        return JSON.toJSONString(u,Filter.getFilter());
    }


}
