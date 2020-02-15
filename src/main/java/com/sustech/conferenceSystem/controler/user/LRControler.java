package com.sustech.conferenceSystem.controler.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.service.user.LRService;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 登录注册模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class LRControler {
    @Resource
    private LRService lrService;

    /**
     * /user/login 接口，用于登陆验证
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String login(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Map result= lrService.loginService(user);
        return JSON.toJSONString(result);
    }

    /**
     * /user/regist 接口，用于注册用户
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String regist(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Filter.attributeFilter(user);
        Map result=lrService.registService(user);
        return JSON.toJSONString(result);
    }

    /**
     * /user/login_verification 接口，用于登陆验证
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/login_verification", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String loginVerification(@RequestBody JSONObject jsonParam){
        String cookie=jsonParam.getString("cookie");
        Map result=lrService.loginVerificationService(cookie);
        return JSON.toJSONString(result);
    }

    /**
     * /user/login_exit 接口，用于退出登陆
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/login_exit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String loginExit(@RequestBody JSONObject jsonParam){
        String token=jsonParam.getString("token");
        Map result=lrService.loginExitService(token);
        return JSON.toJSONString(result);
    }
}
