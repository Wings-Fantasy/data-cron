package com.hxshijie.datacron.config;

import com.hxshijie.datacron.util.DynamicDataSourceContext;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean("lucyDataSource")
    @ConfigurationProperties(prefix = "lucy")
    public LucyDataSource lucyDataSource() {
        return new LucyDataSource();
    }

    @Primary
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("lucyDataSource") LucyDataSource lucyDataSource) {
        List<Map<String, DataSourceParam>> dataSourcesParamList = lucyDataSource.getDataSources();
        if (dataSourcesParamList == null || dataSourcesParamList.isEmpty()) {
            throw new RuntimeException("找不到数据源配置项");
        }
        if (dataSourcesParamList.get(0).get("master") == null) {
            throw new RuntimeException("第一个数据源必须是master");
        }
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourcesParamList.forEach((item) -> item.forEach((k, v) -> {

            HikariDataSource dataSource = (HikariDataSource)(DataSourceBuilder.create()
                    .url(v.getJdbcUrl())
                    .username(v.getUsername())
                    .password(v.getPassword())
                    .driverClassName(v.getDriverClassName())
                    .build());
            dataSource.setConnectionTimeout(v.getConnectionTimeout());
            dataSourceMap.put(k, dataSource);
            log.info("成功加载数据源：{}", k);
        }));

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(dataSourceMap.get("master"));
        dynamicDataSource.setTargetDataSources(dataSourceMap);

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }
}

class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContext.getDataSource();
    }
}
