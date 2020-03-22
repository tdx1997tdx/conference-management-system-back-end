package com.sustech.conferenceSystem.controler.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.service.device.DeviceManagementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 控制设备的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceManagementControler {
    @Resource
    private DeviceManagementService deviceManagementService;

    /**
     * /device/device_add 接口，用于添加设备
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceAdd(@RequestBody JSONObject jsonParam){
        Device device = JSON.parseObject(jsonParam.toString(), Device.class);
        Map result=deviceManagementService.deviceAddService(device);
        return JSON.toJSONString(result);
    }

    /**
     * /device/device_delete 接口，用于删除设备
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceDelete(@RequestBody JSONObject jsonParam){
        int deviceId = jsonParam.getInteger("device_id");
        Map result=deviceManagementService.deviceDeleteService(deviceId);
        return JSON.toJSONString(result);
    }

    /**
     * /device/device_modify 接口，用于更改设备信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_modify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceModify(@RequestBody JSONObject jsonParam){
        Device device = JSON.parseObject(jsonParam.toString(), Device.class);
        Map result=deviceManagementService.deviceModifyService(device);
        return JSON.toJSONString(result);
    }

    /**
     * /device/device_state_change 接口，用于更改设备状态
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_modify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceStateChange(@RequestBody JSONObject jsonParam){
        int deviceId = jsonParam.getInteger("device_id");
        String state = jsonParam.getString("state");
        String roomName = jsonParam.getString("room_name");
        Map result=deviceManagementService.deviceStateChangeService(deviceId,state,roomName);
        return JSON.toJSONString(result);
    }


}
