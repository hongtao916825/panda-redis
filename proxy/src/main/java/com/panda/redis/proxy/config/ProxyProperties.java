package com.panda.redis.proxy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProxyProperties {

    @Value("${redis.masterName}")
    private String masterName;

    @Value("${server.groupId}")
    private String groupId;

    @Value("${server.registerAddress}")
    private String registerAddress;

    @Value("${redis.cluster:single}")
    private String cluster;

    @Value("#{'${redis.nodes}'.split(',')}")
    private List<String> nodes;

    @Value("${redis.database:0}")
    private Integer database;

    @Value("${server.bindPort}")
    private Integer bindPort;

    @Value("${server.enable.redundant:false}")
    private Boolean enableRedundant;

    @Value("${server.workThread:1}")
    private Integer workThread;

    @Value("${server.bossThread:1}")
    private Integer bossThread;

    @Value("${server.enable.health-report:false}")
    private Boolean healthReport;

    @Value("${server.enable.delay:10}")
    private Integer delay;

    @Value("${redis.password:''}")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public Integer getBindPort() {
        return bindPort;
    }

    public void setBindPort(Integer bindPort) {
        this.bindPort = bindPort;
    }

    public Boolean getEnableRedundant() {
        return enableRedundant;
    }

    public void setEnableRedundant(Boolean enableRedundant) {
        this.enableRedundant = enableRedundant;
    }

    public Integer getWorkThread() {
        return workThread;
    }

    public void setWorkThread(Integer workThread) {
        this.workThread = workThread;
    }

    public Integer getBossThread() {
        return bossThread;
    }

    public void setBossThread(Integer bossThread) {
        this.bossThread = bossThread;
    }

    public Boolean getHealthReport() {
        return healthReport;
    }

    public void setHealthReport(Boolean healthReport) {
        this.healthReport = healthReport;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }
}
