package com.yves.others.concurrent;

public class ThreadLocalTest {
    //线程本地共享变量
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Object> threadLocal2 = new ThreadLocal<>();

    public static void main(String[] args) {

        threadLocal.set(1);
        threadLocal2.set(2);

        threadLocal.get();

        //threadLocal.remove();
        System.err.println(threadLocal.get());
        System.gc();

        System.err.println(threadLocal.get());

        InheritableThreadLocal threadLocal = new InheritableThreadLocal();
        threadLocal.set(1);
        threadLocal.get();


    }

}
