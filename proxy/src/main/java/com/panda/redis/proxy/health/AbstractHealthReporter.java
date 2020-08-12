package com.panda.redis.proxy.health;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.redis.base.api.Client;
import com.panda.redis.proxy.config.ProxyProperties;
import com.panda.redis.proxy.health.pojo.HealthReportInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractHealthReporter implements HealthReporter {

    protected ProxyProperties proxyProperties;

    @Override
    public void run() {
        report();
    }

    @Override
    public void report() {
        try {
            List<HealthReportInfo> healthReportInfos = redisHealth();
            if(!CollectionUtils.isEmpty(healthReportInfos)){
                doReport(healthReportInfos);
            }
        } catch (Exception e) {
            log.error("health report error: ", e);
        }
    }

    protected abstract void doReport(List<HealthReportInfo> healthReportInfos) throws Exception;

    public List<HealthReportInfo> redisHealth(){
        List<String> nodes = proxyProperties.getNodes();
        List<HealthReportInfo> healthReportInfos = nodes.stream().map(Client::new).map(client -> {
            HealthReportInfo.HealthReportInfoBuilder healthReportInfoBuilder = null;
            try {
                healthReportInfoBuilder = HealthReportInfo.builder()
                        .setProxy(InetAddress.getLocalHost().getHostAddress() + ":" + proxyProperties.getBindPort())
                        .setRedisNode(client.getAddress())
                        .setGroupId(proxyProperties.getGroupId());
            } catch (UnknownHostException e) {
                log.error("health reporter failed", e);
                throw new RuntimeException(e);
            }
            try {
                long startTime = System.currentTimeMillis();   //获取开始时间
                client.ping();
                long endTime = System.currentTimeMillis(); //获取结束时间
                return healthReportInfoBuilder.setTimeDelay(endTime - startTime)
                        .setHealth(true)
                        .create();
            }  catch (Exception e){
                log.error("node ping failed", e);
                return healthReportInfoBuilder.setTimeDelay(null)
                        .setHealth(false)
                        .create();
            }
        }).collect(Collectors.toList());
        return healthReportInfos;
    }


}
