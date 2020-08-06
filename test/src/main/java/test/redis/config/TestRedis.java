package test.redis.config;

import com.panda.redis.base.api.Client;
import com.panda.redis.core.context.ClientsLoadBalance;
import com.panda.redis.core.context.ServersContext;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import test.redis.config.bo.RedisBo;

import java.util.ArrayList;
import java.util.List;

public class TestRedis {

    public static void main(String[] args) throws InterruptedException {
//        RedisBo redisBo = new RedisBo();
//        System.out.println(redisBo.stringRedisTemplate.opsForValue().get("panda-redis:test2"));
        Client client = new Client("127.0.0.1", 6061);
        Client client2 = new Client("127.0.0.1", 6063);
        List<Client> clients = new ArrayList<>();
        clients.add(client);
        clients.add(client2);
        ServersContext serversContext = new ServersContext(clients);
        ClientsLoadBalance clientsLoadBalance = new ClientsLoadBalance(serversContext);
        clientsLoadBalance.set("test.loadBalance3", "1111111");
        clientsLoadBalance.set("test.loadBalance4", "2222222");
    }

}
