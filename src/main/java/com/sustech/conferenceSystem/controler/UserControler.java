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
        String username=jsonParam.getString("username");
        String password=jsonParam.getString("password");
        int role=Integer.parseInt(jsonParam.getString("role"));
        Map result=userService.loginService(new User(username,password,role));
        return JSON.toJSONString(result);
    }

    /**
     * /user/regist 接口，用于注册用户
     * @param jsonParam
     * @return
     */
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

    /**
     * /user/regist 接口，用于注册用户
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/modify_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String modifyInfo(@RequestBody JSONObject jsonParam){
        // 直接将json信息打印出来
        String username=jsonParam.getString("username");
        String name=jsonParam.getString("name");
        String gender=jsonParam.getString("gender");
        String phone=jsonParam.getString("phone");
        String email=jsonParam.getString("email");
        String organization=jsonParam.getString("organization");
        String department=jsonParam.getString("department");
        String position=jsonParam.getString("position");

        User user=new User();
        user.setName(name);
        user.setUsername(username);
        user.setGender(gender);
        user.setPhone(phone);
        user.setEmail(email);
        user.setOrganization(organization);
        user.setDepartment(department);
        user.setPosition(position);

        User.attributeFilter(user);
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


}
