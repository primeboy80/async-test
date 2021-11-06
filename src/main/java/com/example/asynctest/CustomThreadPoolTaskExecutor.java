package com.example.asynctest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.BlockingQueue;

public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private BlockingQueue<Runnable> queue;

    @Override
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        queue = super.createQueue(queueCapacity);
        System.out.println("CustomThreadPoolTaskExecutor.createQueue()");
        return queue;

    }
    public BlockingQueue<Runnable> getQueue(){
        return queue;
    }
}
