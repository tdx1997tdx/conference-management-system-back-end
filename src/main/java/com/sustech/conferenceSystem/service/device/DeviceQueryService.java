package com.sustech.conferenceSystem.service.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制设备的查询模块service层
 */
@Service
public class DeviceQueryService {
    @Resource
    private DeviceMapper deviceMapper;


    /**
     * 处理获取所有会议的业务逻辑
     * @return 所有会议
     */
    public List<Device> deviceGetAllService() {
        return deviceMapper.getAllDevice();
    }

    /**
     * 查询指定设备详细信息
     * @param device 设备信息
     * @return 结果0或1
     */
    public Device deviceDetailService(Device device){
        List<Device> res = deviceMapper.searchDevice(device);
        return res.get(0);
    }

    /**
     * 模糊查询业务逻辑
     * @param deviceName 设备名称
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public Map<String,Object> deviceSearchService(String deviceName,int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Device> list=deviceMapper.fuzzySearchDevice(deviceName);
        PageInfo<Device> pageInfo=new PageInfo<>(list);
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
    public Map<String,Object> deviceSearchPageService(int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Device> list=deviceMapper.fuzzySearchDevice("");
        PageInfo<Device> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }


}
