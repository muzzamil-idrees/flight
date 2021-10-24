package com.flight.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class FlightConfiguration implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(FlightConfiguration.class);

    @Value("${app.async.thread.max.size}")
    int maxThreadPoolSize;

    @Value("${app.async.thread.core.max.size}")
    int coreThreadPoolSize;

    @Value("${app.async.thread.name.prfix}")
    String asyncThreadPrefix;

    @Value("${app.async.thread.queue.size}")
    int maxThreadQueueSize;

    @Autowired
    private FlightPriceRuleEngine flightPriceRuleEngine;

    @Bean(name = "asyncThreadPoolTaskExecutor")
    public Executor asyncThreadPoolTaskExecutor() {//Custom Executor
        ThreadPoolTaskExecutor taskExecutor
                = new ThreadPoolTaskExecutor();
        taskExecutor.afterPropertiesSet();

        taskExecutor.setCorePoolSize(coreThreadPoolSize);         //It should be equal to the total number of incoming REST requests
        taskExecutor.setQueueCapacity(maxThreadQueueSize); //even if pool size is exhausted, some requests can be queued rather rejecting client calls/connection
        taskExecutor.setMaxPoolSize(maxThreadPoolSize);
        taskExecutor.setThreadNamePrefix(asyncThreadPrefix);
        taskExecutor.initialize();

        // Getting PoolSize

        logger.info("asyncThreadPoolTaskExecutor Properties, core {}, max {}, pool {}, active {}",
                taskExecutor.getCorePoolSize() ,taskExecutor.getMaxPoolSize(), taskExecutor.getPoolSize(), taskExecutor.getActiveCount());

        // Shutting Down ThreadPoolTaskExecutor
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.shutdown();


        return taskExecutor;
    }

    @PostConstruct
    private void postConstruct() {
        flightPriceRuleEngine.asyncInitializeRuleEngine(Thread.currentThread().getName());

    }
}
