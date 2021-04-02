package com.yves.others.concurrent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorTest {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(5);
        poolExecutor.submit(() -> System.err.println("=====submit======"));
        poolExecutor.schedule(() -> System.err.println("=====schedule======"), 5, TimeUnit.SECONDS);
        poolExecutor.scheduleAtFixedRate(() -> System.err.println("=====scheduleAtFixedRate======"), 5, 5, TimeUnit.SECONDS);


    }
}
