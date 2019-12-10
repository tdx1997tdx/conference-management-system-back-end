package com.sustech.conferenceSystem.controler.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.service.user.AdminService;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 管理员模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class AdminControler {
    @Resource
    private AdminService adminService;

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
        List<User> result= adminService.adminSearchService(username,page,volume);
        return JSON.toJSONString(result, Filter.getFilter());
    }

    /**
     * /user/admin_search_page 接口，返回指定页数用户信息
     */
    @RequestMapping(value = "/admin_search_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String adminSearchAll(@RequestBody JSONObject jsonParam){
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map result=adminService.adminSearchPageService(page,volume);
        return JSON.toJSONString(result, Filter.getFilter());
    }

    /**
     * /user/admin_delete 接口，用于删除某一用户
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/admin_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String adminDelete(@RequestBody JSONObject jsonParam){
        User user = JSON.parseObject(jsonParam.toString(), User.class);
        Map<String,String> result=adminService.adminDeleteService(user);
        return JSON.toJSONString(result);
    }

}
