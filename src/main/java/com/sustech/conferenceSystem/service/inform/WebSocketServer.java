package com.sustech.conferenceSystem.service.inform;

import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket服务端,多例的，一次websocket连接对应一个实例
 *  @ServerEndpoint 注解的值为URI,映射客户端输入的URL来连接到WebSocket服务器端
 */
@Component
@ServerEndpoint("/{id}/{name}")
@Getter
public class WebSocketServer {
    /** 用来记录当前在线连接数。设计成线程安全的，原子计数。*/
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /** 用于保存uri对应的连接服务，{uri:WebSocketServer}，设计成线程安全的 */
    static ConcurrentHashMap<String, WebSocketServer> webSocketServerMAP = new ConcurrentHashMap<>();
    // 存储各个客户端连接情况，包含uri，session等，package-private
    private Session session;// 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private String id;   //客户端用户ID，验证客户身份
    private String name; //客户端用户名字，验证客户身份
    private String uri; //连接的uri
    
     /**
     * 连接建立成功时触发，绑定参数
     * @param session
     *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param id
     * @param name
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id, @PathParam("name") String name) throws IOException {
        this.session = session;
        this.id = id;
        this.name = name;
        this.uri = session.getRequestURI().toString();

        WebSocketServer webSocketServer = webSocketServerMAP.get(uri);
        if(webSocketServer != null){ //同样业务的连接已经在线，则把原来的挤下线。
            // 需要添加cookie认证（未实现）
            webSocketServer.sendMessage(uri + "重复连接被挤下线了");
            webSocketServer.onClose();//关闭连接，触发关闭连接方法onClose()
        }
        webSocketServerMAP.put(uri, this);//保存uri对应的连接服务
        addOnlineCount(); // 在线数加1
        sendMessage("新用户登录， id:"+ id + " name: " + name + "当前在线连接数:" + getOnlineCount());
        System.out.println("onOpen: " + uri);
        System.out.println("有新连接加入！当前在线连接数：" + getOnlineCount());
    }

    /**
     * 连接关闭时触发，注意不能向客户端发送消息了
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        webSocketServerMAP.remove(uri);//删除uri对应的连接服务
        reduceOnlineCount(); // 在线数减1
        System.out.println("onClose: " + uri);
        System.out.println("有一连接关闭！当前在线连接数" + getOnlineCount());
    }

    /**
     * 收到客户端消息后触发
     * （暂时用不到，出于学习原因，保留相关代码，在之后删除）
     *
     * @param message 客户端发送过来的消息
//     * @param session 可选的参数
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        //服务器向消息发送者客户端发送消息
        this.sendMessage("id: " + id + " name: " + name + "发送给服务端消息：" + message);
        System.out.println("id: " + id + " name: " + name + "发送来消息：" + message);

    }

    /**
     * 通信发生错误时触发
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     * @param message
     * @throws IOException
     */
     public void sendMessage(String message) throws IOException {
         System.out.println("sendMessage： uri: " + this.uri + " message: " + message);
         this.session.getBasicRemote().sendText(message);
    }

    /**
     * 获取在线连接数
     * @return
     */
     public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 原子性操作，在线连接数加一
     */
    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    /**
     * 原子性操作，在线连接数减一
     */
    public static void reduceOnlineCount() {
        onlineCount.getAndDecrement();
    }
}