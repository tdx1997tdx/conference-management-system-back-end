package com.sustech.conferenceSystem.controler.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.service.device.DeviceQueryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 控制设备的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceQueryControler {
    @Resource
    private DeviceQueryService deviceQueryService;

    /**
     * /device/device_search_all 接口，用于获取所有会议
     * @return
     */
    @RequestMapping(value = "/device_search_all",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String deviceGetAll(){
        List<Device> result = deviceQueryService.deviceGetAllService();
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }
    
    /**
     * /device/device_detail 接口，接收指定id,用于显示设备相关信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceDetail(@RequestBody JSONObject jsonParam){
        Device device = JSON.parseObject(jsonParam.toString(), Device.class);
        Device result=deviceQueryService.deviceDetailService(device);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * /device/device_search 接口，根据名称模糊分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceSearch(@RequestBody JSONObject jsonParam){
        String deviceName=jsonParam.getString("device_name");
        int page=jsonParam.getInteger("page");
        int volume=jsonParam.getInteger("volume");
        Map<String,Object> result=deviceQueryService.deviceSearchService(deviceName,page,volume);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * /device/device_search_page 接口，用于分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_search_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceSearchPage(@RequestBody JSONObject jsonParam){
        int page=jsonParam.getInteger("page");
        int volume=jsonParam.getInteger("volume");
        Map<String,Object> result=deviceQueryService.deviceSearchPageService(page,volume);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * /device/device_floor_search 接口，获取指定大楼楼层的设备信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/device_floor_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceFloorSearch(@RequestBody JSONObject jsonParam){
        String building=jsonParam.getString("building");
        String floor=jsonParam.getString("floor");
        List<Room> result=deviceQueryService.deviceFloorSearchService(building,floor);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }
}
