package com.sustech.conferenceSystem.service.room;

import com.github.pagehelper.PageHelper;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制房间的查询模块service层
 */
@Service
public class RoomQueryService {
    @Resource
    private RoomMapper roomMapper;

    /**
     * 查询指定房间详细信息
     * @param room 房间信息
     * @return 结果0或1
     */
    public Room roomDetailService(Room room){
        List<Room> res = roomMapper.searchRoom(room);
        return res.get(0);
    }

    /**
     * 模糊查询业务逻辑
     * @param roomName 房间名称
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public List<Room> roomSearchService(String roomName,int page,int volume){
        PageHelper.startPage(page, volume);
        List<Room> res=roomMapper.fuzzySearchRoom(roomName);
        return res;
    }

    /**
     * 分页查询业务逻辑
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public List<Room> roomSearchPageService(int page,int volume){
        PageHelper.startPage(page, volume);
        List<Room> res=roomMapper.fuzzySearchRoom("");
        return res;
    }
}
