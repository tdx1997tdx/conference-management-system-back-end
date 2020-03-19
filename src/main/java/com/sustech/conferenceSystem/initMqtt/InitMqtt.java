package com.sustech.conferenceSystem.initMqtt;

import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.mapper.RoomMapper;
import com.sustech.conferenceSystem.util.mqtt.MqttConfiguration;
import com.sustech.conferenceSystem.util.mqtt.MqttPushClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class InitMqtt {
    @Resource
    private MqttConfiguration mc;
    @Resource
    private RoomMapper roomMapper;
    public void initMqtt(){
        MqttPushClient mqttPushClient=mc.getMqttPushClient();
        mqttPushClient.subscribe("Register");
        List<Room> rooms=roomMapper.searchRoom(new Room());
        for(Room r:rooms){
            mqttPushClient.subscribe(r.getRoomId()+"_feedback");
            mqttPushClient.subscribe(r.getRoomId()+"_touch_pad_fb");
        }
    }
}
