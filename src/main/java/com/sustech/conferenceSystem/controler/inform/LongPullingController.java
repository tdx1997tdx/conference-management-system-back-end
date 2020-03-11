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

@RestController
public class LongPullingController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //guava中的Multimap，多值map,对map的增强，一个key可以保持多个value
    public static Multimap<String, DeferredResult<Message>> watchRequests = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    //模拟长轮询
    @RequestMapping(value = "/watch/{id}/{name}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public DeferredResult<Message> watch(@PathVariable("id") String id, @PathVariable("name") String name) {
        String namespace = id + name;
        System.out.println("-------------------------");
        System.out.println("namespace:" + namespace);
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
    @RequestMapping(value = "/publish/{namespace}", method = RequestMethod.GET, produces = "text/html")
    public Object publishConfig(@PathVariable("namespace") String namespace) {
        if (watchRequests.containsKey(namespace)) {
            Collection<DeferredResult<Message>> deferredResults = watchRequests.get(namespace);
            Long time = System.currentTimeMillis();
            //通知所有watch这个namespace变更的长轮训配置变更结果
            for (DeferredResult<Message> deferredResult : deferredResults) {
                Message msg = new Message();
                msg.setMessageBody(namespace + " changed:" + time);
                deferredResult.setResult(msg);
            }
            return "success";
        } else {
            return "failed";
        }
    }

    public static void sendMessage(Message message, Collection<DeferredResult<Message>> deferredResults) {
        //通知所有watch这个namespace变更的长轮训配置变更结果
        for (DeferredResult<Message> deferredResult : deferredResults) {
            deferredResult.setResult(message);
        }
    }
}