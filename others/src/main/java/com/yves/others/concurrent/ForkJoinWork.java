package com.yves.others.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinWork extends RecursiveTask<Long> {
    private Long start;//起始值
    private Long end;//结束值

    public static final Long critical = 100000L;//临界值

    public ForkJoinWork(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // return null;
        //判断是否是拆分完毕
        Long lenth = end - start;   //起始值差值
        if (lenth <= critical) {
            //如果拆分完毕就相加
            Long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            //没有拆分完毕就开始拆分
            Long middle = (end + start) / 2;//计算的两个值的中间值
            ForkJoinWork right = new ForkJoinWork(start, middle);
            right.fork();//拆分，并压入线程队列
            ForkJoinWork left = new ForkJoinWork(middle + 1, end);
            left.fork();//拆分，并压入线程队列

            //合并
            return right.join() + left.join();
        }
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinWork(0L, 10000000000L);//参数为起始值与结束值
        Long invoke = forkJoinPool.invoke(task);
        new Runnable() {
            @Override
            public void run() {

            }
        };

        new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        Thread thread = new Thread();
        try {
            thread.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("invoke = " + invoke);
    }
}


