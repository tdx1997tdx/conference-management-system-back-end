package com.sustech.conferenceSystem.mqttService;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Callback implements MqttCallback {
    @Resource
    Listener listener;
    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        log.info("连接断开重新连接");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        listener.dealWithMessage(topic,message.getQos(),new String(message.getPayload()));
    }
}
