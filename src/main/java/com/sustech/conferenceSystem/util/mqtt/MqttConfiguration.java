package com.sustech.conferenceSystem.util.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Configuration
@Data
public class MqttConfiguration {
    @Value("${mqtt.host}")
    private String host;
    @Value("${mqtt.clientid}")
    private String clientid;
    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.timeout}")
    private int timeout;
    @Value("${mqtt.keepalive}")
    private int keepalive;
    @Resource
    MqttPushClient mqttPushClient;
    @Bean
    public MqttPushClient getMqttPushClient(){
        mqttPushClient.connect(host, clientid, username, password);
        return mqttPushClient;
    }
}



