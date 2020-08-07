package com.panda.redis.core.loadBalance.impl;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ServersContext;
import com.panda.redis.core.loadBalance.abstractImpl.AbstractClientLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@Lazy(true)
public class RoundRobinClientLoadBalance extends AbstractClientLoadBalance {

    private AtomicInteger nextServerCyclicCounter;

    public RoundRobinClientLoadBalance() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }

    @Override
    protected Client doChooseClient(ServersContext serversContext) {
        Client server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Client> allServers = serversContext.getClients();
            int upCount = allServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: ");
                return null;
            }

            int nextServerIndex = incrementAndGetModulo(serverCount);
            server = allServers.get(nextServerIndex);

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }

            return (server);

        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: ");
        }
        return server;
    }


    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
