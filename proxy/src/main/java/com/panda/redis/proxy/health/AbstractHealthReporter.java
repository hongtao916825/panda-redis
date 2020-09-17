package com.panda.redis.proxy.health;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.redis.base.api.Client;
import com.panda.redis.base.common.LogUtil;
import com.panda.redis.proxy.config.ProxyProperties;
import com.panda.redis.proxy.health.pojo.HealthReportInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            LogUtil.error("health report error: ", e);
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
                LogUtil.error("health reporter failed", e);
                throw new RuntimeException(e);
            }
            StopWatch stopWatch = null;
            try {
                stopWatch = new StopWatch("delay");
                stopWatch.start();
                client.ping();
                return healthReportInfoBuilder.setTimeDelay(stopWatch.getTotalTimeMillis())
                        .setHealth(true)
                        .create();
            }  catch (Exception e){
                LogUtil.error("node ping failed", e);
                return healthReportInfoBuilder.setTimeDelay(null)
                        .setHealth(false)
                        .create();
            } finally {
                Optional.ofNullable(stopWatch).ifPresent((s)->{
                    if(s.isRunning()){
                        s.stop();
                    }
                });
            }
        }).collect(Collectors.toList());
        return healthReportInfos;
    }


}
