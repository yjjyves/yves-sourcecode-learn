package com.yves.others.loadBalance;

import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoundRobinLoadBalance2 {
    private static final ConcurrentMap<String, AtomicPositiveInteger> sequences = new ConcurrentHashMap();

    private static final ConcurrentMap<String, AtomicPositiveInteger> indexSeqs = new ConcurrentHashMap();


    public static void main(String[] args) {
        List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("A", 5);
        Invoker invoker2 = new Invoker("B", 2);
        Invoker invoker3 = new Invoker("C", 1);

        invokers.add(invoker);
        invokers.add(invoker2);
        invokers.add(invoker3);


        for (int i = 0; i < 8; i++) {
            Invoker selectInvoker = doSelect(invokers);
            System.err.println(selectInvoker.getIp());
        }

        /**
         * 调用结果
         A
         B
         A
         A
         A
         A
         B
         C
         */

    }

    /**
     * 主要思想：
     * 0.为每次请求定义一个固定的Key,找一个请求次数序号
     * 1.累加所有权重,相当于统计一共有多少个请求
     * 2.循环找到最大权重,最小权重,以及把有效权重的服务提供者有序放到一个列表里面
     * 3.按总权重次数轮询,按key对应的序号对权重取余得到mod,代表实际的第mod请求
     * 4.在循环中当mod为0且当前服务列表中的节点还有可用次数的即作为当前应该轮询的节点
     *
     * @param invokers
     * @return Invoker
     */
    protected static Invoker doSelect(List<Invoker> invokers) {
        // key = 全限定类名 + "." + 方法名，比如 com.xxx.DemoService.sayHello,这里暂且写死
        String key = "doSelect";
        int length = invokers.size();
        // 最大权重
        int maxWeight = 0;
        // 最小权重
        int minWeight = Integer.MAX_VALUE;

        final List<Invoker> effectiveInvokers = new ArrayList<>();

        // 下面这个循环主要用于查找最大和最小权重，计算权重总和等
        for (int i = 0; i < length; i++) {
            int weight = invokers.get(i).getWeight();
            // 获取最大和最小权重
            maxWeight = Math.max(maxWeight, weight);
            minWeight = Math.min(minWeight, weight);
            if (weight > 0) {
                effectiveInvokers.add(invokers.get(i));
            }
        }

        // 查找 key 对应的对应 AtomicPositiveInteger 实例，为空则创建。
        // 这里可以把 AtomicPositiveInteger 看成一个黑盒，大家只要知道
        // AtomicPositiveInteger 用于记录服务的调用编号即可
        AtomicPositiveInteger sequence = sequences.get(key);
        if (sequence == null) {
            sequences.putIfAbsent(key, new AtomicPositiveInteger());
            sequence = sequences.get(key);
        }

        // 获取下标序列对象 AtomicPositiveInteger
        AtomicPositiveInteger indexSeq = indexSeqs.get(key);
        if (indexSeq == null) {
            // 创建 AtomicPositiveInteger，默认值为 -1
            indexSeqs.putIfAbsent(key, new AtomicPositiveInteger(-1));
            indexSeq = indexSeqs.get(key);
        }

        // 如果最小权重小于最大权重，表明服务提供者之间的权重是不相等的
        if (maxWeight > 0 && minWeight < maxWeight) {
            length = effectiveInvokers.size();
            /**
             * 假设服务器 [A, B, C] 对应权重 [5, 2, 1]
             * 第一轮循环，currentWeight = 1，可返回 A 和 B,实际返回A
             * 第二轮循环，currentWeight = 2，返回A
             * 第三轮循环，currentWeight = 3，返回A
             * 第三轮循环，currentWeight = 3，返回A
             * 第四轮循环，currentWeight = 4，返回A
             * 第五轮循环，currentWeight = 0，返回A, B, C
             */
            while (true) {
                //对应服务列表的下标
                int index = indexSeq.incrementAndGet() % length;
                //调用次数对最大权重取余
                int currentWeight = sequence.get() % maxWeight;

                // 每循环一轮（index = 0），重新计算 currentWeight
                if (index == 0) {
                    currentWeight = sequence.incrementAndGet() % maxWeight;
                }

                // 检测 Invoker 的权重是否大于 currentWeight，大于则返回
                if (effectiveInvokers.get(index).getWeight() > currentWeight) {
                    return effectiveInvokers.get(index);
                }
            }
        }

        // 服务提供者之间的权重相等，此时通过轮询选择 Invoker
        return invokers.get(sequence.incrementAndGet() % length);
    }
}
