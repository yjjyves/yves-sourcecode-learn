package com.yves.others.concurrent;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolTest {
    public static void main(String[] args) {

        System.err.println(Runtime.getRuntime().availableProcessors());
        //ForkJoinPool pool = new ForkJoinPool();
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            pool.submit(() -> {
                System.out.println(finalI + ".........");
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*pool.submit(() -> {
            System.out.println("two.........");
            try {
                Thread.sleep(1000*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
            System.out.println("three.........");
            try {
                Thread.sleep(1000*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        pool.submit(() -> {
            System.out.println("four.........");
            try {
                Thread.sleep(1000*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        pool.submit(() -> {
            System.out.println("five.........");
            try {
                Thread.sleep(1000*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
            System.out.println("six.........");
            try {
                Thread.sleep(1000*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
    }
}
