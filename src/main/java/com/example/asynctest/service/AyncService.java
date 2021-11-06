package com.example.asynctest.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AyncService {
    private static final Logger logger = LoggerFactory.getLogger(AyncService.class);

    @Async("taskExecutor")
    public void test1(int i) {
        logger.info("Start order_1_" + i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Ended order_1_" + i);
    }

    @Async("taskExecutor2")
    public void test2(int i) {
        logger.info("Start  order_2_" + i);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Ended  order_2_" + i);
    }
}
