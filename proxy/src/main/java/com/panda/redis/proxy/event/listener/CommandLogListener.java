package com.panda.redis.proxy.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.redis.proxy.event.pojo.CommandEvent;
import com.panda.redis.proxy.event.pojo.CommandPojo;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 记录指令
 */
@Component
public class CommandLogListener implements ApplicationListener<CommandEvent> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onApplicationEvent(CommandEvent event) {
        CommandPojo commandPojo = event.getCommandPojo();
        try {
            System.out.println(objectMapper.writeValueAsString(commandPojo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
