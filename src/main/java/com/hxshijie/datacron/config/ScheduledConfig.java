package com.hxshijie.datacron.config;

import com.hxshijie.datacron.util.DynamicDataSourceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ScheduledConfig {

    @Before("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void beforeScheduled(JoinPoint joinPoint) {
        String threadName = Thread.currentThread().getName();
        String jobClass = joinPoint.getTarget().getClass().toString();
        String jobName = jobClass.substring(jobClass.lastIndexOf(".") + 1);
        StringBuilder newThreadName = new StringBuilder(threadName);
        newThreadName.replace(threadName.lastIndexOf("-"), threadName.lastIndexOf("-") + 1,
                "[" + jobName + "]-");
        Thread.currentThread().setName(newThreadName.toString());
        DynamicDataSourceContext.setDataSource();
    }

    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void afterScheduled(JoinPoint joinPoint) {
        String threadName = Thread.currentThread().getName();
        String jobClass = joinPoint.getTarget().getClass().toString();
        String jobName = jobClass.substring(jobClass.lastIndexOf(".") + 1);
        String newThreadName = threadName.replace("[" + jobName + "]", "");
        Thread.currentThread().setName(newThreadName);
    }
}
