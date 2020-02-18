package com.sustech.conferenceSystem.service.device;

import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制设备的增删改模块service层
 */
@Service
public class DeviceManagementService {
    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 添加设备业务逻辑
     * @param device 设备信息
     * @return 结果0或1
     */
    public Map<String,String> deviceAddService(Device device){
        Map<String,String> res=new HashMap<>();
        List<Device> devices = null;
//        List<Device> devices = deviceMapper.isDeviceExist(device);
        // 检测是否有重复的设备，未完成
        boolean isSuccess = false;
        if(devices == null || devices.size() == 0) {
            isSuccess = deviceMapper.addDevice(device);
        }
        if(isSuccess){
            res.put("state","1");
            res.put("message","添加设备成功");
        }else {
            res.put("state","0");
            res.put("message","添加设备失败");
        }
        return res;
    }

    /**
     * 删除设备业务逻辑
     * @param deviceId 设备信息
     * @return 结果0或1
     */
    public Map<String,String> deviceDeleteService(int deviceId){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess=deviceMapper.deleteDevice(deviceId);
        if(isSuccess){
            res.put("state","1");
            res.put("message","删除设备成功");
        }else {
            res.put("state","0");
            res.put("message","删除设备失败");
        }
        return res;
    }

    /**
     * 更改设备业务逻辑
     * @param device 设备信息
     * @return 结果0或1
     */
    public Map<String,String> deviceModifyService(Device device){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess = false;
        if(deviceMapper.findDeviceById(device.getDeviceId()) != null) {
            isSuccess = deviceMapper.updateDevice(device);
        }
        if(isSuccess){
            res.put("state","1");
            res.put("message","更改设备成功");
        }else {
            res.put("state","0");
            res.put("message","更改设备失败");
        }
        return res;
    }
}