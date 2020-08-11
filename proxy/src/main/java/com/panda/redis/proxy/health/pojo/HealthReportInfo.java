package com.panda.redis.proxy.health.pojo;

public class HealthReportInfo {

    private String proxy;

    private String redisNode;

    private Long timeDelay;

    private Boolean health;

    private String groupId;

    public HealthReportInfo() {
    }

    public HealthReportInfo(String proxy, String redisNode, Long timeDelay, Boolean health, String groupId) {
        this.proxy = proxy;
        this.redisNode = redisNode;
        this.timeDelay = timeDelay;
        this.health = health;
        this.groupId = groupId;
    }

    public String getProxy() {
        return proxy;
    }

    public String getRedisNode() {
        return redisNode;
    }

    public Long getTimeDelay() {
        return timeDelay;
    }

    public Boolean getHealth() {
        return health;
    }

    public String getGroupId() {
        return groupId;
    }

    public static HealthReportInfoBuilder builder(){
        return new HealthReportInfoBuilder();
    }

    public static class HealthReportInfoBuilder {
        private String proxy;

        private String redisNode;

        private Long timeDelay;

        private Boolean health;

        private String groupId;

        public HealthReportInfoBuilder setRedisNode(String redisNode) {
            this.redisNode = redisNode;
            return this;
        }

        public HealthReportInfoBuilder setTimeDelay(Long timeDelay) {
            this.timeDelay = timeDelay;
            return this;
        }

        public HealthReportInfoBuilder setProxy(String proxy) {
            this.proxy = proxy;
            return this;
        }

        public HealthReportInfoBuilder setHealth(Boolean health) {
            this.health = health;
            return this;
        }

        public HealthReportInfoBuilder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public HealthReportInfo create(){
            return new HealthReportInfo(proxy, redisNode, timeDelay, health, groupId);
        }
    }

}
