package com.example.asynctest.config;

import com.example.asynctest.CustomThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Tiger-Pool-");
        executor.initialize();
        System.out.println("taskExecutor");
        return executor;
    }

    @Bean(name = "taskExecutor2")
    public TaskExecutor threadPoolTaskExecutor2() {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("Cats1-Pool : ");
        executor.initialize();
        System.out.println("taskExecutor");
        return executor;
    }

}
