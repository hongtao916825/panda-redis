package com.panda.redis.dash.dash.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DashContents {

    public final static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

}
