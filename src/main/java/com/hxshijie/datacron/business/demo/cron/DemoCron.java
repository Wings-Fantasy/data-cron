package com.hxshijie.datacron.business.demo.cron;

import com.hxshijie.datacron.business.demo.mapper.DemoMapper;
import com.hxshijie.datacron.util.DynamicDataSourceContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

/**
 * 示例任务
 * */
@Component
public class DemoCron {

    private final Logger log = LoggerFactory.getLogger(DemoCron.class);

    @Resource
    private TransactionTemplate transactionTemplate;

    private final DemoMapper mapper;

    public DemoCron(DemoMapper mapper) {
        this.mapper = mapper;
    }

    /*
     添加@Async注解代表异步任务，不添加代表阻塞任务。注意：阻塞任务未执行完不会执行第二次。
     例如DemoCron需要3秒才能运行完成，配置为每秒运行一次，阻塞任务则会跳过第二次任务执行第三次任务，异步任务则会同时存在两个DemoCron任务线程

     定时调度配置在cron.properties文件内。idea2024.2以上可以提示出cron的含义
     注解@Scheduled的写法固定为@Scheduled(cron = "${配置名:-}")。在生产环境中，不再需要执行某个任务时可以注释掉对应的配置
    */
    @Async
    @Scheduled(cron = "${cron.com.hxshijie.datacron.business.demo.cron.DemoCron:-}")
    public void run() {
        // 多数据源示例
        log.info("定时任务开始运行");
        DynamicDataSourceContext.setDataSource("master");
        log.info("切换到名为 master 的数据源");
        Integer i = mapper.selectOne();
        log.info("执行结果：{}", i);
        DynamicDataSourceContext.setDataSource("database2");
        log.info("切换到名为 database2 的数据源");
        i = mapper.selectOne();
        log.info("执行结果：{}", i);
        DynamicDataSourceContext.setDataSource();
        log.info("切换到默认数据源，默认数据源是master");
        i = mapper.selectOne();
        log.info("执行结果：{}", i);
        log.info("定时任务运行完成");

        // 编程式事务
        transactionTemplate.execute((status) -> {
            try {
                return mapper.selectOne();
            } catch (Exception e) {
                // 回滚
                status.setRollbackOnly();
                return null;
            }
        });
    }

    // 树形表转树形结构
    private List<Map<String, Object>> buildOrgTree(List<Map<String, Object>> src, String parentId) {
        return src.stream()
                .filter(parent -> (parent.get("parent_id") + "").equals(parentId))
                .peek(child -> child.put("children", buildOrgTree(src, child.get("id") + "")))
                .toList();
    }
}
