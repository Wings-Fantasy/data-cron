package com.hxshijie.datacron.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LucyDataSource {
    private List<Map<String, DataSourceParam>> dataSources;
}

@Data
class DataSourceParam {
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private Long connectionTimeout = 30000L;
}
