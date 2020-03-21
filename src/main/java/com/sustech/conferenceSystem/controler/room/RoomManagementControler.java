package com.sustech.conferenceSystem.controler.room;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.service.room.RoomManagementService;
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
public class RoomManagementControler {
    @Resource
    private RoomManagementService roomManagementService;

    /**
     * /room/room_add 接口，用于添加房间
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/room_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roomAdd(@RequestBody JSONObject jsonParam){
        Room room = JSON.parseObject(jsonParam.toString(), Room.class);
        Map result=roomManagementService.roomAddService(room);
        return JSON.toJSONString(result);
    }

    /**
     * /room/room_delete 接口，用于删除房间
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/room_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roomDelete(@RequestBody JSONObject jsonParam){
        int roomId = Integer.parseInt(jsonParam.getString("room_id"));
        Map result=roomManagementService.roomDeleteService(roomId);
        return JSON.toJSONString(result);
    }

    /**
     * /room/room_modify 接口，用于更改房间信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/room_modify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String roomModify(@RequestBody JSONObject jsonParam){
        Room room = JSON.parseObject(jsonParam.toString(), Room.class);
        Map result=roomManagementService.roomModifyService(room);
        return JSON.toJSONString(result);
    }
}
