package com.panda.redis.proxy.health.zkImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.redis.base.constants.ProxyConstants;
import com.panda.redis.proxy.config.ProxyProperties;
import com.panda.redis.proxy.health.AbstractHealthReporter;
import com.panda.redis.proxy.health.pojo.HealthReportInfo;
import com.panda.redis.proxy.report.zkImpl.CuratorCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.List;


@Component
public class ZookeeperHealthReport extends AbstractHealthReporter {

    private static final String PREFIX = "zookeeper:";

    ObjectMapper objectMapper = new ObjectMapper();

    private CuratorCrud curatorCrud;

    @PostConstruct
    public void initConnection(){
        String registerAddress = proxyProperties.getRegisterAddress();
        String connectString = registerAddress.replace(PREFIX, "");
        curatorCrud = new CuratorCrud(connectString);
    }

    @Override
    protected void doReport(List<HealthReportInfo> healthReportInfos) throws Exception {
        String healthData = objectMapper.writeValueAsString(healthReportInfos);
        curatorCrud.setData(ProxyConstants.GROUP_REGISTER + "/" + proxyProperties.getGroupId() + "/"
                + (InetAddress.getLocalHost().getHostAddress()+":" + proxyProperties.getBindPort()), healthData);
    }

    @Autowired
    protected void setProperties(ProxyProperties properties) {
        super.proxyProperties = properties;
    }

}
