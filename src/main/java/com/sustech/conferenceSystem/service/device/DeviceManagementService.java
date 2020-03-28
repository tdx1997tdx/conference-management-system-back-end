package com.sustech.conferenceSystem.service.device;

import com.alibaba.fastjson.JSON;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import com.sustech.conferenceSystem.mqttService.MqttUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制设备的增删改模块service层
 */
@Service
public class DeviceManagementService {
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private MqttUtil mqttUtil;

    /**
     * 添加设备业务逻辑
     * @param device 设备信息
     * @return 结果0或1
     */
    public Map<String,String> deviceAddService(Device device){
        Map<String,String> res=new HashMap<>();
        try{
            deviceMapper.addDevice(device);
            res.put("state","1");
            res.put("message","添加设备成功");
        }catch (Exception e){
            e.printStackTrace();
            res.put("state","0");
            res.put("message","添加设备失败，设备重复");
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
        try{
            deviceMapper.updateDevice(device);
            res.put("state","1");
            res.put("message","更改设备成功");
        }catch (Exception e){
            e.printStackTrace();
            res.put("state","0");
            res.put("message","更改设备失败,重名");
        }
        return res;
    }

    /**
     * 更改设备状态
     * @return 结果0或1
     */
    public Map<String,String> deviceStateChangeService(Integer deviceId,String state,String roomId){
        Map<String,String> res=new HashMap<>();
        Map<String,String> json=new HashMap<>();
        json.put("device_id",deviceId+"");
        json.put("command",state);
        String message=JSON.toJSONString(json);
        System.out.println("roomId:"+roomId);
        System.out.println("message:"+message);
        mqttUtil.publish(roomId,message);
        res.put("state","1");
        res.put("message","发送成功");
        return res;
    }
}
