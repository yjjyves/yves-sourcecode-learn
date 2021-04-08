package com.yves.others.concurrent;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        //线程启动时不打印,等待唤醒
        Semaphore a = new Semaphore(0);
        Semaphore b = new Semaphore(0);
        Semaphore c = new Semaphore(0);
        Semaphore last = new Semaphore(0);

        MyThread myThreadA = new MyThread("A", a, b);
        MyThread myThreadB = new MyThread("B", b, c);
        MyThread myThreadC = new MyThread("C", c, last); 

        //准备输出
        myThreadA.start();
        myThreadB.start();
        myThreadC.start();

        for (int i = 0; i < 10; i++) {
            //开始依次打印
            a.release(1);
            try {
                last.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //停止线程
        myThreadA.setStop();
        myThreadB.setStop();
        myThreadC.setStop();

        a.release(1);
        b.release(1);
        c.release(1);
    }

    private static class MyThread extends Thread {
        private volatile boolean stop = false;
        private String remark;
        private Semaphore start;
        private Semaphore end;

        public MyThread(String remark, Semaphore start, Semaphore end) {
            this.remark = remark;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            while (!stop) {
                try {
                    this.start.acquire();
                    if (stop) {
                        break;
                    }
                    System.err.println(remark);
                    this.end.release(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setStop() {
            this.stop = true;
        }
    }
}