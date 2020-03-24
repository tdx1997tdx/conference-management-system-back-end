package com.sustech.conferenceSystem.controler.inform;

import com.alibaba.fastjson.JSON;
import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.util.RedisUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sustech.conferenceSystem.service.inform.InformConstants.*;
import static com.sustech.conferenceSystem.service.inform.InformConstants.LinkState.LONG_PULLING;
import static com.sustech.conferenceSystem.service.inform.InformConstants.LinkState.WEBSOCKET;

/**
 * websocket服务端,多例的，一次websocket连接对应一个实例
 *  @ServerEndpoint 注解的值为URI,映射客户端输入的URL来连接到WebSocket服务器端
 */
@Component
@ServerEndpoint("/{id}/{token}/{name}")
@Getter
public class WebSocketControler {
    /** 用来记录当前在线连接数。设计成线程安全的，原子计数。*/
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /** 用于保存uri对应的连接服务，{uri:WebSocketServer}，设计成线程安全的 */

    private static RedisUtil redisUtil;

    private Session session;// 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private int id;   //客户端用户ID，验证客户身份
    private String name; //客户端用户名字，验证客户身份
    private String uri; //连接的uri
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void checkToken(int id, String token) throws IOException {
        System.out.println("onOpen id: " + id + " token: " + token);
        if (INFORM_TEST_ON) {
            return;
        }
        String CheckToken=(String) redisUtil.get(Integer.toString(id));
        if(!token.equals(CheckToken)){
////        if (!token.equals("abc123")) {
            this.id = 1;
            this.name = "System";
            session.close();
            return;
        }
        System.out.println("onOpen CheckToken: " + CheckToken);
    }

    public void checkLinkStates(int id, String name) throws IOException {
        String namespace = id + name;

        Message message = new Message("WebSocketControler->onOpen->checkLinkStates");
        message.setMessageTopic("disconnect success");
        message.setReceiverId(id);
        message.setReceiverName(name);
        LinkState linkState = LinkStatesMap.get(namespace);
        System.out.println("WebSocketControler checkLinkStates: " + linkState);
        if (linkState == LONG_PULLING) {
            DeferredResult<Message> deferredResult = deferredResultsMap.get(namespace);
            if (deferredResult != null) {
                message.setMessageBody(uri + " long pulling被websocket挤下线了");
                LongPullingController.sendMessage(message, deferredResult);
            }
        } else {
            WebSocketControler webSocketControler = webSocketServersMAP.get(namespace);
            if(webSocketControler != null){ //同样业务的连接已经在线，则把原来的挤下线。
                message.setMessageBody(uri + "重复连接被websocket挤下线了");
                webSocketControler.sendMessage(message);
                webSocketControler.session.close();//关闭连接，触发关闭连接方法onClose()
            }
        }
        System.out.println("WebSocketControler1 checkLinkStates: " + LinkStatesMap.get(namespace));
        LinkStatesMap.put(namespace, WEBSOCKET); //保存namespace的连接状态为webSocket
        System.out.println("WebSocketControler2 checkLinkStates: " + LinkStatesMap.get(namespace));
    }

     /**
     * 连接建立成功时触发，绑定参数
     * @param session
     *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param id
     * @param token
     * @param name
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") int id, @PathParam("token") String token, @PathParam("name") String name) throws IOException {
        this.session = session;
        this.id = id;
        this.name = name;
        this.uri = session.getRequestURI().toString();

        String namespace = id + name;
        addOnlineCount(); // 在线数加1

        checkToken(id, token);
        checkLinkStates(id, name);
        webSocketServersMAP.put(namespace, this); //保存namespace对应的webSocket连接服务

        Message message = new Message("WebSocketControler->onOpen");
        message.setReceiverId(id);
        message.setReceiverName(name);
        message.setMessageBody("新用户登录， id:"+ id + " name: " + name + "当前在线连接数:" + getOnlineCount());
        sendMessage(message);

        System.out.println("onOpen: " + uri);
        System.out.println("有新连接加入！当前在线连接数：" + getOnlineCount());
    }

    /**
     * 连接关闭时触发，注意不能向客户端发送消息了
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        String namespace = id + name;
        webSocketServersMAP.remove(namespace);//删除namespace对应的连接服务
        LinkStatesMap.remove(namespace, WEBSOCKET);//删除该namespace对应的连接状态
        reduceOnlineCount(); // 在线数减1
        System.out.println("onClose: uri: " + uri + " namespace: " + namespace);
        System.out.println("有一连接关闭！当前在线连接数" + getOnlineCount());
    }

    /**
     * 收到客户端消息后触发
     * （暂时用不到，出于学习原因，保留相关代码，在之后删除）
     *
     * @param messageGet 客户端发送过来的消息
//     * @param session 可选的参数
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String messageGet) throws IOException {
        //服务器向消息发送者客户端发送消息
        Message message = new Message("WebSocketControler->onMessage");
        message.setMessageBody("id: " + id + " name: " + name + "发送给服务端消息：" + message);
        this.sendMessage(message);
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
     public void sendMessage(Message message) throws IOException {
         System.out.println("sendMessage： uri: " + this.uri);
         System.out.println("message: " + message);
         this.session.getBasicRemote().sendText(JSON.toJSONString(message));
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