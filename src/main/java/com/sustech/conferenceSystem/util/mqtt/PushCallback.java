package com.sustech.conferenceSystem.util.mqtt;

import com.sustech.conferenceSystem.initMqtt.InitMqtt;
import com.sustech.conferenceSystem.initMqtt.Listener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PushCallback implements MqttCallback {
    @Resource
    Listener listener;
    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开重新连接");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //订阅成功后回调
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
        System.out.println(listener);
        listener.dealWithMessage(topic,message.getQos(),new String(message.getPayload()));
    }
}
