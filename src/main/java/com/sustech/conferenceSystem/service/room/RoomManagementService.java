package com.sustech.conferenceSystem.service.room;

import com.github.pagehelper.PageHelper;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.RoomMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制房间的增删改模块service层
 */
@Service
public class RoomManagementService {
    @Resource
    private RoomMapper roomMapper;

    /**
     * 添加房间业务逻辑
     * @param room 房间信息
     * @return 结果0或1
     */
    public Map<String,String> roomAddService(Room room){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess;
        try{
            isSuccess=roomMapper.addRoom(room);
        }catch (Exception e){
            e.printStackTrace();
            isSuccess=false;
        }
        if(isSuccess){
            res.put("state","1");
            res.put("message","添加房间成功");
        }else {
            res.put("state","0");
            res.put("message","添加房间失败");
        }
        return res;
    }

    /**
     * 删除房间业务逻辑
     * @param roomId 房间信息
     * @return 结果0或1
     */
    public Map<String,String> roomDeleteService(int roomId){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess=roomMapper.deleteRoom(roomId);
        if(isSuccess){
            res.put("state","1");
            res.put("message","删除房间成功");
        }else {
            res.put("state","0");
            res.put("message","删除房间失败");
        }
        return res;
    }

    /**
     * 更改房间业务逻辑
     * @param room 房间信息
     * @return 结果0或1
     */
    public Map<String,String> roomModifyService(Room room){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess=roomMapper.updateRoom(room);
        if(isSuccess){
            res.put("state","1");
            res.put("message","更改房间成功");
        }else {
            res.put("state","0");
            res.put("message","更改房间失败");
        }
        return res;
    }
}
