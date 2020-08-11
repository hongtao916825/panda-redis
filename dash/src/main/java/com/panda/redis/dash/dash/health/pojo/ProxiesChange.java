package com.panda.redis.dash.dash.health.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ProxiesChange {

    private String group;

    private List<String> currentChildren;

}
