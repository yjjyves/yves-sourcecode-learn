package com.yves.others.loadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 权重随机
 *
 * @author yijinjin
 * @date 2020/9/28 -14:30
 */
public class RandomLoadBalance {
    private final static Random random = new Random();

    public static void main(String[] args) {
        List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("192.168.1.100", 5);
        Invoker invoker2 = new Invoker("192.168.1.101", 3);
        Invoker invoker3 = new Invoker("192.168.1.102", 2);

        invokers.add(invoker);
        invokers.add(invoker2);
        invokers.add(invoker3);

        for (int i = 0; i < 10; i++) {
            Invoker selectInvoker = doSelect(invokers);
            System.err.println(selectInvoker.getIp());
        }

    }

    /**
     * 主要思想是通过随机数去计算,看随机数落在哪个区间
     *
     * @param invokers
     * @return
     */
    protected static Invoker doSelect(List<Invoker> invokers) {
        int length = invokers.size();
        int totalWeight = 0;
        boolean sameWeight = true;
        // 下面这个循环有两个作用，第一是计算总权重 totalWeight，
        // 第二是检测每个服务提供者的权重是否相同
        for (int i = 0; i < length; i++) {
            int weight = invokers.get(i).getWeight();
            // 累加权重
            totalWeight += weight;
            // 检测当前服务提供者的权重与上一个服务提供者的权重是否相同，
            // 不相同的话，则将 sameWeight 置为 false。
            if (sameWeight && i > 0
                    && weight != invokers.get(i - 1).getWeight()) {
                sameWeight = false;
            }
        }

        // 下面的 if 分支主要用于获取随机数，并计算随机数落在哪个区间上
        if (totalWeight > 0 && !sameWeight) {
            // 随机获取一个 [0, totalWeight) 区间内的数字
            int offset = random.nextInt(totalWeight);
            // 循环让 offset 数减去服务提供者权重值，当 offset 小于0时，返回相应的 Invoker。
            // 举例说明一下，我们有 servers = [A, B, C]，weights = [5, 3, 2]，offset = 7。
            // 第一次循环，offset - 5 = 2 > 0，即 offset > 5，
            // 表明其不会落在服务器 A 对应的区间上。
            // 第二次循环，offset - 3 = -1 < 0，即 5 < offset < 8，
            // 表明其会落在服务器 B 对应的区间上
            for (int i = 0; i < length; i++) {
                // 让随机值 offset 减去权重值
                offset -= invokers.get(i).getWeight();
                if (offset < 0) {
                    // 返回相应的 Invoker
                    return invokers.get(i);
                }
            }
        }

        // 如果所有服务提供者权重值相同，此时直接随机返回一个即可
        return invokers.get(random.nextInt(length));
    }
}

