package com.example.asynctest;

import com.example.asynctest.service.AyncService;
import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class AsyncTestApplication {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(AsyncTestApplication.class);
        ConfigurableApplicationContext context = SpringApplication.run(AsyncTestApplication.class, args);
        AyncService ayncService = context.getBean(AyncService.class);
        CustomThreadPoolTaskExecutor taskExecutor = context.getBean("taskExecutor2", CustomThreadPoolTaskExecutor.class);



//        for(int i=0; i<10; i++) {
//            logger.info("test order now : " + i);
//            ayncService.test1(i);
//        }

        for(int i=0; i<100; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            logger.info("Order now : " + i+" / Queue size = " + taskExecutor.getQueue().size());
            ayncService.test2(i);
        }

    }




}

