# Spring Boot Async Demo
## Overview
Spring Boot Multi Queue Async Demo

Config Package 안에 두 개의 Queue 설정이 있습니다. ThreadPoolTaskExecutor를 그대로 사용 할 수 있으나, Queue가 현재 얼마나 있는지 궁금하기 때문에 getQueue() method를 추가하여 확인 하려고 합니다.

Class CustomThreadPoolTaskExecutor
```java
public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private BlockingQueue<Runnable> queue;

    @Override
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        queue = super.createQueue(queueCapacity);
        return queue;

    }
    public BlockingQueue<Runnable> getQueue(){
        return queue;
    }
}
```
Config Package 안에 설정된 Queue 의 수를 확인 하기 위해서 CustomThreadPoolTaskExecutor 클래스를 추가 하였습니다.
```java
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
```
기본으로 Pool이 1개로 설정되어 있으며, Queue의 수를 넘지않으면 두번째 Pool이 실행되지 않습니다.
Tiger-Pool-2이 나오는 것을보려면 Queue의 수를 넘겨주어야 합니다.


아래처럼 delay를 주어서 실행 할 수 있는 Class를 생성하여 사용 하였습니다.
```java
package com.example.asynctest.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(com.example.asynctest.service.AsyncService.class);

    @Async("taskExecutor")
    public void tiger(int i) {
        logger.info("Start order_1_" + i);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Ended order_1_" + i);
    }

    @Async("taskExecutor2")
    public void cats1(int i) {
        logger.info("Start  order_2_" + i);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Ended  order_2_" + i);
    }
}

```

tiger의 처리 속도는 100입니다. Order의 속도는 40이기 때문에 처리하는 속도보다 일을 시키는 속도가 빠르게 됩니다.

AsyncTestApplication
```java
@SpringBootApplication
@EnableAsync
public class AsyncTestApplication {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(AsyncTestApplication.class);
        ConfigurableApplicationContext context = SpringApplication.run(AsyncTestApplication.class, args);
        AsyncService asyncService = context.getBean(AsyncService.class);


        CustomThreadPoolTaskExecutor taskExecutor = context.getBean("taskExecutor", CustomThreadPoolTaskExecutor.class);
        CustomThreadPoolTaskExecutor taskExecutor2 = context.getBean("taskExecutor2", CustomThreadPoolTaskExecutor.class);


        for(int i=0; i<20; i++) {
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asyncService.tiger(i);
            logger.info("Tiger Order now : " + i + " / Queue size = " + taskExecutor.getQueue().size());
        }

        for(int i=0; i<100; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ayncService.cats1(i);
            logger.info("Cats1 Order now : " + i + " / Queue size = " + taskExecutor2.getQueue().size());
        }
    }
}
```

Queue가 10을 넘어서는 순간 두번째 Tiger Pool이 등장합니다. 이후에는 두번째 Pool이 실행되어 같이 일을 처리합니다.
```java
2021-11-06 16:18:04.937  INFO 24724 --- [   Tiger-Pool-1] c.example.asynctest.service.AyncService  : Start order_1_7
2021-11-06 16:18:04.963  INFO 24724 --- [  restartedMain] c.e.asynctest.AsyncTestApplication       : Tiger Order now : 17 / Queue size = 10
2021-11-06 16:18:05.009  INFO 24724 --- [  restartedMain] c.e.asynctest.AsyncTestApplication       : Tiger Order now : 18 / Queue size = 10
2021-11-06 16:18:05.009  INFO 24724 --- [   Tiger-Pool-2] c.example.asynctest.service.AyncService  : Start order_1_18
2021-11-06 16:18:05.042  INFO 24724 --- [   Tiger-Pool-1] c.example.asynctest.service.AyncService  : Ended order_1_7
2021-11-06 16:18:05.043  INFO 24724 --- [   Tiger-Pool-1] c.example.asynctest.service.AyncService  : Start order_1_8
2021-11-06 16:18:05.052  INFO 24724 --- [  restartedMain] c.e.asynctest.AsyncTestApplication       : Tiger Order now : 19 / Queue size = 10
2021-11-06 16:18:05.112  INFO 24724 --- [   Tiger-Pool-2] c.example.asynctest.service.AyncService  : Ended order_1_18
2021-11-06 16:18:05.114  INFO 24724 --- [   Tiger-Pool-2] c.example.asynctest.service.AyncService  : Start order_1_9
```