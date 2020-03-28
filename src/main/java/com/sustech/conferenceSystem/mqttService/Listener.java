package com.sustech.conferenceSystem.mqttService;

import com.alibaba.fastjson.JSON;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import com.sustech.conferenceSystem.service.inform.InformService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Listener {
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private InformService informService;
    @Resource
    private MqttUtil mqttUtil;
    public void dealWithMessage(String topic,int qos,String message) throws IOException {
        System.out.println("dealWithMessage before");
        System.out.println("topic:"+topic);
        System.out.println("message:"+message);
        if(topic.equals("Register")){
            String res=registerProcessor(message);
            mqttUtil.publish(2,false,"AssignRoom",res);
            return;
        }
        String roomId=topic.split("_")[0];
        Map<String,String> mess = JSON.parseObject(message,Map.class);
        Device device=new Device();
        device.setDeviceId(Integer.parseInt(mess.get("device_id")));
        device.setState(fliter(mess.get("device_status")));
        deviceMapper.updateDevice(device);

        Message msg = new Message("Listener->dealWithMessage");
        msg.setMessageTopic("device state change");
        msg.setMessageBody(JSON.toJSONString(device));
        System.out.println("dealWithMessage after");

        informService.informAll(msg);
    }
    private Integer fliter(String staus){
        if(staus.equals("off")){
            return 0;
        }else if(staus.equals("on")){
            return 1;
        }else if(staus.equals("tv_notify")){
            return 2;
        }
        return null;
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
            res.put("room_id",d.getRoom().getRoomId()+"");
        }
        System.out.println(JSON.toJSONString(res));
        return JSON.toJSONString(res);
    }
}
