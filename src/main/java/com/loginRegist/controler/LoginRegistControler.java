package com.loginRegist.controler;

import com.alibaba.fastjson.JSONObject;
import com.loginRegist.mapper.UserMapper;
import com.loginRegist.service.LoginRegistService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@ResponseBody
@RestController
@RequestMapping(value = "json")
public class LoginRegistControler {
    @Resource
    private LoginRegistService loginRegistService;
    @Resource
    private UserMapper userMapper;
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String loginJson(@RequestBody JSONObject jsonParam){
        // 直接将json信息打印出来
        System.out.println(jsonParam.toString());
        String username=jsonParam.getString("username");
        String password=jsonParam.getString("password");
        String result=loginRegistService.loginService(username,password);
        System.out.println(result);
        JSONObject rtn = new JSONObject();
        rtn.put("status", result);
        return rtn.toJSONString();
    }
    @RequestMapping(value = "regist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String registJson(@RequestBody JSONObject jsonParam){
        // 直接将json信息打印出来
        System.out.println(jsonParam.toJSONString());
        String username=jsonParam.getString("username");
        String password=jsonParam.getString("password");
        String result=loginRegistService.registService(username,password);
        JSONObject rtn = new JSONObject();
        rtn.put("status", result);
        return rtn.toJSONString();
    }


}
