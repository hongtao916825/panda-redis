package health;

import com.panda.redis.base.api.Client;
import com.panda.redis.proxy.config.ProxyProperties;
import health.pojo.HealthReportInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
public abstract class AbstractHealthReporter implements HealthReporter {

    protected ProxyProperties proxyProperties;

    @Override
    public void run() {
        report();
    }

    @Override
    public void report() {
        List<HealthReportInfo> healthReportInfos = redisHealth();
        if(!CollectionUtils.isEmpty(healthReportInfos)){
            doReport(healthReportInfos);
        }
    }

    protected abstract void doReport(List<HealthReportInfo> healthReportInfos);

    public List<HealthReportInfo> redisHealth(){
        List<String> nodes = proxyProperties.getNodes();
        nodes.stream().map(Client::new).forEach(client -> {
            try {
                long startTime=System.currentTimeMillis();   //获取开始时间
                String pong = client.ping();
                long endTime=System.currentTimeMillis(); //获取结束时间
                HealthReportInfo.builder()
                        .setProxy(InetAddress.getLocalHost().getHostAddress()+":" + proxyProperties.getBindPort())
                        .setRedisNode(client.getAddress())
                        .setTimeDelay(endTime - startTime)
                        .create();
            } catch (UnknownHostException e) {
                log.error(e.getMessage());
            }
        });
//        Client jedis = new Client(proxyProperties.getNodes().get(0));
//        Future<String> future = executor.submit(() -> {
//            return jedis.send(req.getBytes());
//        });
        return new ArrayList<>();
    }


}
