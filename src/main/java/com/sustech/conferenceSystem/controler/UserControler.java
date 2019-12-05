package com.sustech.conferenceSystem.controler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.service.UserService;
import com.sustech.conferenceSystem.dto.User;
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
public class UserControler {
    @Resource
    private UserService userService;

    /**
     * /user/login 接口，用于登陆验证
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String login(@RequestBody JSONObject jsonParam){
        String username=jsonParam.getString("username");
        String password=jsonParam.getString("password");
        int role=Integer.parseInt(jsonParam.getString("role"));
        Map result=userService.loginService(new User(username,password,role));
        return JSON.toJSONString(result);
    }
    @RequestMapping(value = "/regist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String registJson(@RequestBody JSONObject jsonParam){
        // 直接将json信息打印出来
        String username=jsonParam.getString("username");
        String password=jsonParam.getString("password");
        int role=Integer.parseInt(jsonParam.getString("role"));
        User user=new User(username,password,role);
        user.setEmail(jsonParam.getString("email"));
        user.setName(jsonParam.getString("name"));
        Map result=userService.registService(user);
        return JSON.toJSONString(result);
    }


}
