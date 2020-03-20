package com.sustech.conferenceSystem.controler.inform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.sustech.conferenceSystem.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LongPullingController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //guava中的Multimap，多值map,对map的增强，一个key可以保持多个value
    public static ConcurrentHashMap<String, DeferredResult<Message>> watchRequests = new ConcurrentHashMap<>();

    //模拟长轮询
    @RequestMapping(value = "/connect/{id}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public DeferredResult<Message> connect(@PathVariable("id") String id, @PathVariable("name") String name) {
        String namespace = id + name;
        System.out.println("-------------------------");
        System.out.println("namespace:" + namespace);
        if (watchRequests.containsKey(namespace)) {
            System.out.println("containsKey");
            disconnect(id, name);
        }
        return getMessageDeferredResult(namespace);
    }

    public DeferredResult<Message> getMessageDeferredResult(String namespace) {
        DeferredResult<Message> deferredResult = new DeferredResult<>();

        //当deferredResult完成时（不论是超时还是异常还是正常完成），移除watchRequests中相应的watch key
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                System.out.println("remove key:" + namespace);
                watchRequests.remove(namespace, deferredResult);
            }
        });
        watchRequests.put(namespace, deferredResult);
        logger.info("Servlet thread released");
        return deferredResult;
    }

    //模拟发布namespace配置
    @RequestMapping(value = "/disconnect/{id}/{name}", method = RequestMethod.GET, produces = "text/html")
    public Object disconnect(@PathVariable("id") String id, @PathVariable("name") String name) {
        String namespace = id + name;
        if (watchRequests.containsKey(namespace)) {
            DeferredResult<Message> deferredResult = watchRequests.get(namespace);
            Message msg = new Message();
            msg.setMessageBody("连接已断开");
            msg.setSenderName(name);
            msg.setMessageTopic("disconnect success");
            deferredResult.setResult(msg);
            return "disconnect success";
        } else {
            return "disconnect failed";
        }
    }

    public static void sendMessage(Message message, DeferredResult<Message> deferredResult) {
        //通知所有watch这个namespace变更的长轮训配置变更结果
        deferredResult.setResult(message);
    }
}