package com.sustech.conferenceSystem.controler.inform;

import com.sustech.conferenceSystem.dto.Message;
import com.sustech.conferenceSystem.service.inform.InformConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;

import static com.sustech.conferenceSystem.service.inform.InformConstants.*;
import static com.sustech.conferenceSystem.service.inform.InformConstants.LinkState.LONG_PULLING;
import static com.sustech.conferenceSystem.service.inform.InformConstants.LinkState.WEBSOCKET;

@RestController
public class LongPullingController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void checkLinkStates(int id, String name) throws IOException {
        String namespace = id + name;

        Message message = new Message("LongPullingController->connect->checkLinkStates");
        message.setMessageTopic("disconnect success");
        message.setReceiverId(id);
        message.setReceiverName(name);
        LinkState linkState = LinkStatesMap.get(namespace);
        System.out.println("LongPullingController checkLinkStates: " + linkState);
        if (linkState == WEBSOCKET) {
            WebSocketControler webSocketControler = webSocketServersMAP.get(namespace);
            if(webSocketControler != null){ //同样业务的连接已经在线，则把原来的挤下线。
                message.setMessageBody(webSocketControler.getUri() + " websocket连接被long pulling挤下线了");
                webSocketControler.sendMessage(message);
                System.out.println("getSession close: 11");
                webSocketControler.getSession().close();//关闭连接，触发关闭连接方法onClose()
                System.out.println("getSession close: 2");
            }
        } else {
            DeferredResult<Message> deferredResult = deferredResultsMap.get(namespace);
            if (deferredResult != null) {
                System.out.println("LongPullingController containsKey");
                message.setMessageBody("重复连接被long pulling挤下线了");
                sendMessage(message, deferredResult);
            }
        }
    }

    //模拟长轮询
    @RequestMapping(value = "/connect/{id}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public DeferredResult<Message> connect(@PathVariable("id") int id, @PathVariable("name") String name) throws IOException {
        String namespace = id + name;

        checkLinkStates(id, name);

        DeferredResult<Message> deferredResult = new DeferredResult<>();
        //当deferredResult完成时（不论是超时还是异常还是正常完成），移除deferredResultsMap中相应的key
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                System.out.println("remove key:" + namespace);
                deferredResultsMap.remove(namespace, deferredResult);
                LinkStatesMap.remove(namespace, LONG_PULLING);//删除该namespace对应的连接状态
            }
        });
        deferredResultsMap.put(namespace, deferredResult);
        System.out.println("LongPullingController add key:" + namespace);
        LinkStatesMap.put(namespace, LONG_PULLING); //保存namespace的连接状态为LONG_PULLING
        logger.info("Servlet thread released");
        return deferredResult;
    }

    //模拟发布namespace配置
    @RequestMapping(value = "/disconnect/{id}/{name}", method = RequestMethod.GET, produces = "text/html")
    public Object disconnect(@PathVariable("id") int id, @PathVariable("name") String name) {
        String namespace = id + name;
        if (deferredResultsMap.containsKey(namespace)) {
            DeferredResult<Message> deferredResult = deferredResultsMap.get(namespace);
            Message msg = new Message("LongPullingController->disconnect");
            msg.setMessageBody("连接已断开");
            msg.setReceiverId(id);
            msg.setReceiverName(name);
            msg.setMessageTopic("disconnect success");
            sendMessage(msg, deferredResult);
            return "disconnect success";
        } else {
            return "disconnect failed";
        }
    }

    public static void sendMessage(Message message, DeferredResult<Message> deferredResult) {
        String namespace = message.getReceiverId() + message.getReceiverName();
        deferredResult.setResult(message);
    }
}