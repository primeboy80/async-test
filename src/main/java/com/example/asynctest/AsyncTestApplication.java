package com.example.asynctest;

import com.example.asynctest.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.ConfigurableApplicationContext;

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
            asyncService.cats1(i);
            logger.info("Cats1 Order now : " + i + " / Queue size = " + taskExecutor2.getQueue().size());
        }
    }
}

