package com.panda.redis.proxy.event.listener;

import com.panda.redis.proxy.event.pojo.CommandEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 失败率记录
 */
@Component
public class CommandErrorRateListener implements ApplicationListener<CommandEvent> {

    @Override
    public void onApplicationEvent(CommandEvent event) {

    }

}
