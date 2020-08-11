package com.panda.redis.dash.dash.controller;

import com.panda.redis.dash.dash.health.HealthReport;
import com.panda.redis.dash.dash.util.DashContents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthReport healthReport;

    @PostConstruct
    public void registerListener(){
        healthReport.listener();
    }

    @RequestMapping(path="/subscribe")
    public @ResponseBody
    SseEmitter subscribe(String id) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(3600000L);
        DashContents.sseCache.put(id, sseEmitter);
        sseEmitter.onTimeout(() -> {
            DashContents.sseCache.remove(id);
        });
        sseEmitter.onCompletion(() -> {});
        return sseEmitter;
    }

    @RequestMapping("/test")
    public String test() throws IOException {
        return "test.html";
    }

//    @RequestMapping("/push")
//    @ResponseBody
//    public String push(String id, String content) throws IOException {
//        SseEmitter sseEmitter = sseCache.get(id);
//        if(sseEmitter != null){
//            sseEmitter.send(content);
//        }
//        return "success";
//    }

}
