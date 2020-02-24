package com.sustech.conferenceSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 * @author Administrator
 */
@Configuration
public class WebSocketConfig {
    /**
     * 扫描并注册带有@ServerEndpoint注解的所有服务端
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}