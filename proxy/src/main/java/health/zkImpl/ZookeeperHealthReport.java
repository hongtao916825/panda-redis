package health.zkImpl;

import com.panda.redis.proxy.config.ProxyProperties;
import health.AbstractHealthReporter;
import health.pojo.HealthReportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
public class ZookeeperHealthReport extends AbstractHealthReporter {

    @Override
    protected void doReport(List<HealthReportInfo> healthReportInfos) {

    }

    @Autowired
    protected void setProperties(ProxyProperties properties) {
        super.proxyProperties = properties;
    }

}
