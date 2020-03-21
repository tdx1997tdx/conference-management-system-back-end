package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Room;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DeviceMapper {

    /**
     * @return 所有设备集合
     */
    List<Device> getAllDevice();


    /**
     *
     * 注册一个device
     * @param device
     * @return
     */
    boolean addDevice(Device device);

    /**
     * 删除一个device
     * @param deviceId 唯一的设备id
     * @return
     */
    boolean deleteDevice(int deviceId);

    /**
     * 修改一个device
     * @param device 设备
     * @return
     */
    boolean updateDevice(Device device);

    /**
     * 查询符合限制的设备
     * @param device 设备
     * @return
     */
    List<Device> searchDevice(Device device);

    /**
     * 模糊查询设备
     * @param deviceName 设备名字
     * @return
     */
    List<Device> fuzzySearchDevice(String deviceName);


    /**
     * 根据id查找设备
     * @param deviceId
     * @return
     */
    Device findDeviceById(int deviceId);


}
