package com.panda.redis.proxy.event.listener;

import com.panda.redis.proxy.event.pojo.CommandEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 指令时延
 */
@Component
public class CommandDelayListener  implements ApplicationListener<CommandEvent> {
    @Override
    public void onApplicationEvent(CommandEvent event) {

    }
}
