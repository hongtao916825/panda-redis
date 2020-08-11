package com.panda.redis.dash.dash.health.pojo;

import lombok.Data;

@Data
public class DataChange {

    // 1:变化，0删除
    private Integer type;

    private String data;

}
