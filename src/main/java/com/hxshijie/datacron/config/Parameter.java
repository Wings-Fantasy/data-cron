package com.hxshijie.datacron.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局常量类
 * <p>
 * 常量的值写在 parameter.yml 配置文件里，建议使用idea的自动生成Setter功能避免注入失败，使用方式示例：
 * Parameter.upload_path
 * <p>
 * 配置文件会在打包后的jar包相同目录下，方便生产环境按需修改
 * */
@Component
@ConfigurationProperties(prefix = "lucy.parameter")
public class Parameter {

    public static String upload_path;

    public static void setUpload_path(String upload_path) {
        Parameter.upload_path = upload_path;
    }
}