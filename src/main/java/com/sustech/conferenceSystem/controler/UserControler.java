package com.sustech.conferenceSystem.controler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.service.UserService;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
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
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Map result=userService.loginService(user);
        return JSON.toJSONString(result);
    }

    /**
     * /user/regist 接口，用于注册用户
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String registJson(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Filter.attributeFilter(user);
        Map result=userService.registService(user);
        return JSON.toJSONString(result);
    }

    /**
     * /user/regist 接口，用于注册用户
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/modify_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String modifyInfo(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Filter.attributeFilter(user);
        Map result=userService.modifyInfoService(user);
        return JSON.toJSONString(result);
    }

    /**
     * /user/admin_search 接口，用查找相关用户信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/admin_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String adminSearch(@RequestBody JSONObject jsonParam){
        // 直接将json信息打印出来
        String username=jsonParam.getString("username");
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        List<User> result=userService.adminSearchService(username,page,volume);
        return JSON.toJSONString(result, Filter.getFilter());
    }

    /**
     * /user/admin_search_all 接口，返回所有用户信息
     */
    @GetMapping(value = "/admin_search_all")
    public String adminSearchAll(){
        List<User> result=userService.adminSearchAllService();
        return JSON.toJSONString(result, Filter.getFilter());
    }

}
