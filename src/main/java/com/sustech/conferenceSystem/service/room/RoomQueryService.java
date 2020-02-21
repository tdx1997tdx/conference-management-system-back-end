package com.sustech.conferenceSystem.service.room;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.MeetingSimple;
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
     * @param room 房间
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public Map<String,Object> roomSearchService(Room room,int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Room> list=roomMapper.fuzzySearchRoom(room);
        PageInfo<Room> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    /**
     * 分页查询业务逻辑
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public Map<String,Object> roomSearchPageService(int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Room> list=roomMapper.fuzzySearchRoom(new Room());
        PageInfo<Room> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }
}
