package com.yves.others.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {


    public static void main(String[] args) {
        //队列是一种特殊的线性表，它只允许在表的前端进行删除操作，而在表的后端进行插入操作。
        LinkedBlockingQueue queue = new LinkedBlockingQueue(3);
        //添加

        //如果队列满了抛异常
        queue.add("a");
        queue.add("a1");
        queue.add("a2");
        //queue.add("a3");
        //如果队列满了返回false
        queue.offer("b");
        System.err.println(queue.poll());
        queue.offer("c");
        queue.offer("d");

        //删除
        //remove() 和 poll() 方法都是从队列中删除第一个元素。
        // remove() 的行为与 Collection 接口的版本相似， 但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null。因此新的方法更适合容易出现异常条件的情况
        queue.remove();
        queue.poll();

        //peek,element区别：
        //element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null。
        queue.element();
        queue.peek();

        try {
            //如果队列满了阻塞
            queue.put("a");
            // 队列为空，阻塞等待
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ok:
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println("i=" + i + ",j=" + j);
                if (j == 5) {
                    break ok;
                }

            }
        }

    }
}
