package com.panda.redis.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = PandaRedisProperties.PANDAREDIS_PREFIX)
public class PandaRedisProperties {

    public static final String PANDAREDIS_PREFIX = "panda.redis";


//    private String password;

    private Integer maxTotal=50;

    private Integer minIdel = 5;

    private Integer maxIdel=20;

    private Integer timeOut=2000;

    /**从连接池中借出的jedis都会经过测试*/
    private  boolean testOnBorrow = true;
    /**返回jedis到池中Jedis 实例都会经过测试*/
    private  boolean testOnRetrun = false;

    private String groupLoadBalance;

    private List<GroupClient> groupClients;

    public String getGroupLoadBalance() {
        return groupLoadBalance;
    }

    public void setGroupLoadBalance(String groupLoadBalance) {
        this.groupLoadBalance = groupLoadBalance;
    }

    public List<GroupClient> getGroupClients() {
        return groupClients;
    }

    public void setGroupClients(List<GroupClient> groupClients) {
        this.groupClients = groupClients;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMinIdel() {
        return minIdel;
    }

    public void setMinIdel(Integer minIdel) {
        this.minIdel = minIdel;
    }

    public Integer getMaxIdel() {
        return maxIdel;
    }

    public void setMaxIdel(Integer maxIdel) {
        this.maxIdel = maxIdel;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnRetrun() {
        return testOnRetrun;
    }

    public void setTestOnRetrun(boolean testOnRetrun) {
        this.testOnRetrun = testOnRetrun;
    }

}
