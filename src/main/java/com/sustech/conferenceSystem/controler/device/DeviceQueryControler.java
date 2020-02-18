package com.sustech.conferenceSystem.controler.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.service.device.DeviceQueryService;
import com.sustech.conferenceSystem.util.Filter;
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
        Filter.attributeFilter(device);
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
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
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
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map<String,Object> result=deviceQueryService.deviceSearchPageService(page,volume);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }
}