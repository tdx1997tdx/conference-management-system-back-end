package com.sustech.conferenceSystem;
import com.sustech.conferenceSystem.mqttService.MqttUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.sustech.conferenceSystem.mapper")
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        MqttUtil mqttUtil =applicationContext.getBean(MqttUtil.class);
        mqttUtil.initMqtt();
    }

}
