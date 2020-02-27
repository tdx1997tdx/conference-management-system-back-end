package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.Room;

import java.util.List;

public interface RoomMapper {
    /**
     * 注册一个room
     * @param room
     * @return
     */
    boolean addRoom(Room room);

    /**
     * 删除一个room
     * @param roomId 唯一的房间id
     * @return
     */
    boolean deleteRoom(int roomId);

    /**
     * 删除修改一个room
     * @param room 房间
     * @return
     */
    boolean updateRoom(Room room);

    /**
     * 查询符合限制的房间
     * @param room 房间
     * @return
     */
    List<Room> searchRoom(Room room);

    /**
     * 模糊查询房间
     * @param room 房间
     * @return
     */
    List<Room> fuzzySearchRoom(Room room);


    /**
     * 根据id查找房间
     * @param roomId
     * @return
     */
    Room findRoomById(int roomId);

    /**
     * 查询大楼信息
     * @return
     */
    List<String> buildingSearch();

    /**
     * 查询楼层信息
     * @return
     */
    List<String> floorSearch(String building);

}
