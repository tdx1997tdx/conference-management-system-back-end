package com.sustech.conferenceSystem.controler.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.service.room.RoomQueryService;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 控制房间的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/room")
public class RoomQueryControler {
    @Resource
    private RoomQueryService roomQueryService;

    /**
     * /room/room_detail 接口，用于显示房间相关信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/room_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roomDetail(@RequestBody JSONObject jsonParam){
        Room room = JSON.parseObject(jsonParam.toString(), Room.class);
        Filter.attributeFilter(room);
        Room result=roomQueryService.roomDetailService(room);
        return JSON.toJSONString(result);
    }

    /**
     * /room/room_search 接口，根据名称模糊分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/room_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roomDelete(@RequestBody JSONObject jsonParam){
        String roomName=jsonParam.getString("room_name");
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map<String,Object> result=roomQueryService.roomSearchService(roomName,page,volume);
        return JSON.toJSONString(result);
    }

    /**
     * /room/room_search_page 接口，用于分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/room_search_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roomSearchPage(@RequestBody JSONObject jsonParam){
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map<String,Object> result=roomQueryService.roomSearchPageService(page,volume);
        return JSON.toJSONString(result);
    }
}
