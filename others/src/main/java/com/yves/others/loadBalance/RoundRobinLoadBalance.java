package com.yves.others.loadBalance;

import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoundRobinLoadBalance {
    private static final ConcurrentMap<String, AtomicPositiveInteger> sequences =
            new ConcurrentHashMap();


    public static void main(String[] args) {
        List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("A", 5);
        Invoker invoker2 = new Invoker("B", 3);
        Invoker invoker3 = new Invoker("C", 2);

        invokers.add(invoker);
        invokers.add(invoker2);
        invokers.add(invoker3);


        for (int i = 0; i < 10; i++) {
            Invoker selectInvoker = doSelect(invokers);
            System.err.println(selectInvoker.getIp());
        }

        /**
         * 调用结果
         A
         B
         C
         A
         A
         B
         A
         B
         C
         A
         */

    }

    /**
     * 主要思想：
     * 0.为每次请求定义一个固定的Key,找一个请求次数序号
     * 1.累加所有权重,相当于统计一共有多少个请求
     * 2.循环找到最大权重,最小权重,以及把有效权重的服务提供者有序放到一个列表里面
     * 3.按总权重次数轮询,按key对应的序号对权重取余得到mod,代表实际的第mod请求
     * 4.在循环中当mod为0且当前服务列表中的节点还有可用次数的即作为当前应该轮询的节点
     * 目前这种个方法存在个问题,如果mod特别大,需要遍历的次数非常多,存在效率问题,优化版见第二种实现
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
        // 权重总和
        int weightSum = 0;

        // 下面这个循环主要用于查找最大和最小权重，计算权重总和等
        for (int i = 0; i < length; i++) {
            int weight = invokers.get(i).getWeight();
            // 获取最大和最小权重
            maxWeight = Math.max(maxWeight, weight);
            minWeight = Math.min(minWeight, weight);
            if (weight > 0) {
                effectiveInvokers.add(invokers.get(i));
                // 累加权重
                weightSum += weight;
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

        // 获取当前的调用编号
        int currentSequence = sequence.getAndIncrement();
        // 如果最小权重小于最大权重，表明服务提供者之间的权重是不相等的
        if (maxWeight > 0 && minWeight < maxWeight) {
            // 使用调用编号对权重总和进行取余操作
            int mod = currentSequence % weightSum;
            // 进行 maxWeight 次遍历
            for (int i = 0; i < maxWeight; i++) {
                // 遍历 invokerToWeightMap
                for (Invoker invoker : effectiveInvokers) {
                    // 如果 mod = 0，且权重大于0，此时返回相应的 Invoker
                    if (mod == 0 && invoker.getWeight() > 0) {
                        //当调用次数用完,而且还有未用完次数的节点就可以返回了
                        return invoker;
                    }

                    // mod != 0，且权重大于0，此时对权重和 mod 分别进行自减操作,代表已经访问过一次该invoker节点
                    if (invoker.getWeight() > 0) {
                        invoker.decrement();
                        mod--;
                    }
                }
            }
        }

        // 服务提供者之间的权重相等，此时通过轮询选择 Invoker
        return invokers.get(currentSequence % length);
    }


}
