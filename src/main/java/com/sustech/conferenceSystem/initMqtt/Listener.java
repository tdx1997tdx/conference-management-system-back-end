package com.sustech.conferenceSystem.initMqtt;

import com.alibaba.fastjson.JSON;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import com.sustech.conferenceSystem.util.mqtt.MqttConfiguration;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Listener {
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private MqttConfiguration mqttConfiguration;
    public void dealWithMessage(String topic,int qos,String message){
        if(topic.equals("Register")){
            String res=registerProcessor(message);
            mqttConfiguration.getMqttPushClient().publish(2,false,"AssignRoom",res);
        }
    }
    private String registerProcessor(String message){
        Map<String,String> res=new HashMap<>();
        Map<String,String> mess = JSON.parseObject(message,Map.class);
        Device d=new Device();
        d.setDeviceId(Integer.parseInt(mess.get("device_id")));
        List<Device> devices=deviceMapper.searchDevice(d);
        if(devices.size()!=0){
            d=devices.get(0);
            res.put("device_id",String.valueOf(d.getDeviceId()));
            res.put("room_name",d.getRoom().getRoomName());
        }
        return JSON.toJSONString(res);
    }
}
