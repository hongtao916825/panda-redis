package com.panda.redis.proxy.event.pojo;

import org.springframework.context.ApplicationEvent;

/**
 * @author hongtao
 */
public class CommandEvent extends ApplicationEvent {

    private static final long serialVersionUID = -1333762681610307006L;
    private CommandPojo commandPojo;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param commandPojo the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CommandEvent(CommandPojo commandPojo) {
        super(commandPojo);
        this.commandPojo = commandPojo;
    }

    public CommandPojo getCommandPojo() {
        return commandPojo;
    }

    public void setCommandPojo(CommandPojo commandPojo) {
        this.commandPojo = commandPojo;
    }
}
