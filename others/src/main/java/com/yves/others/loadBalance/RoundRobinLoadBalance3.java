package com.yves.others.loadBalance;

import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class RoundRobinLoadBalance3 {
    public static final String NAME = "roundrobin";

    private static int RECYCLE_PERIOD = 60000;

    public static void main(String[] args) {
        List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("A", 5);
        Invoker invoker2 = new Invoker("B", 1);
        Invoker invoker3 = new Invoker("C", 1);

        invokers.add(invoker);
        invokers.add(invoker2);
        invokers.add(invoker3);


        for (int i = 0; i < 7; i++) {
            Invoker selectInvoker = doSelect(invokers);
            System.err.println(selectInvoker.getIp());
        }

        /**
         * 调用结果
         A
         A
         B
         A
         C
         A
         A
         */

    }

    @Data
    protected static class WeightedRoundRobin {
        // 服务提供者权重
        private int weight;
        // 当前权重
        private AtomicLong current = new AtomicLong(0);
        // 最后一次更新时间
        private long lastUpdate;

        public void setWeight(int weight) {
            this.weight = weight;
            // 初始情况下，current = 0
            current.set(0);
        }

        public long increaseCurrent() {
            // current = current + weight；
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            // current = current - total;
            current.addAndGet(-1 * total);
        }
    }

    // 最外层为服务类名 + 方法名，第二层为 url 到 WeightedRoundRobin 的映射关系。
    // 这里我们可以将 url 看成是服务提供者的 id
    private static ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> methodWeightMap = new ConcurrentHashMap<String, ConcurrentMap<String, WeightedRoundRobin>>();

    // 原子更新锁
    private static AtomicBoolean updateLock = new AtomicBoolean();

    /**
     * 主体思路：
     * 1.为每个节点设置自己的权重以及当前权重
     * 2.遍历所有的节点,设置当前权重 += weight,统计总权重 totalWeight
     * 3.找个所有节点中当前权重最大的节点,设置当前权重-= totalWeight并返回
     *
     * @param invokers
     * @return
     */
    protected static Invoker doSelect(List<Invoker> invokers) {
        String key = "doSelect";
        // 获取 url 到 WeightedRoundRobin 映射表，如果为空，则创建一个新的
        ConcurrentMap<String, WeightedRoundRobin> map = methodWeightMap.get(key);
        if (map == null) {
            methodWeightMap.putIfAbsent(key, new ConcurrentHashMap<String, WeightedRoundRobin>());
            map = methodWeightMap.get(key);
        }
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;

        // 获取当前时间
        long now = System.currentTimeMillis();
        Invoker selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;

        // 下面这个循环主要做了这样几件事情：
        //   1. 遍历 Invoker 列表，检测当前 Invoker 是否有
        //      相应的 WeightedRoundRobin，没有则创建
        //   2. 检测 Invoker 权重是否发生了变化，若变化了，
        //      则更新 WeightedRoundRobin 的 weight 字段
        //   3. 让 current 字段加上自身权重，等价于 current += weight
        //   4. 设置 lastUpdate 字段，即 lastUpdate = now
        //   5. 寻找具有最大 current 的 Invoker，以及 Invoker 对应的 WeightedRoundRobin，
        //      暂存起来，留作后用
        //   6. 计算权重总和
        for (Invoker invoker : invokers) {
            String identifyString = invoker.getIp();
            WeightedRoundRobin weightedRoundRobin = map.get(identifyString);
            int weight = invoker.getWeight();
            if (weight < 0) {
                weight = 0;
            }

            // 检测当前 Invoker 是否有对应的 WeightedRoundRobin，没有则创建
            if (weightedRoundRobin == null) {
                weightedRoundRobin = new WeightedRoundRobin();
                // 设置 Invoker 权重
                weightedRoundRobin.setWeight(weight);
                // 存储 url 唯一标识 identifyString 到 weightedRoundRobin 的映射关系
                map.putIfAbsent(identifyString, weightedRoundRobin);
                weightedRoundRobin = map.get(identifyString);
            }
            // Invoker 权重不等于 WeightedRoundRobin 中保存的权重，说明权重变化了，此时进行更新
            if (weight != weightedRoundRobin.getWeight()) {
                weightedRoundRobin.setWeight(weight);
            }

            // 让 current 加上自身权重，等价于 current += weight
            long cur = weightedRoundRobin.increaseCurrent();
            // 设置 lastUpdate，表示近期更新过
            weightedRoundRobin.setLastUpdate(now);
            // 找出最大的 current
            if (cur > maxCurrent) {
                maxCurrent = cur;
                // 将具有最大 current 权重的 Invoker 赋值给 selectedInvoker
                selectedInvoker = invoker;
                // 将 Invoker 对应的 weightedRoundRobin 赋值给 selectedWRR，留作后用
                selectedWRR = weightedRoundRobin;
            }

            // 计算权重总和
            totalWeight += weight;
        }

        // 对 <identifyString, WeightedRoundRobin> 进行检查，过滤掉长时间未被更新的节点。
        // 该节点可能挂了，invokers 中不包含该节点，所以该节点的 lastUpdate 长时间无法被更新。
        // 若未更新时长超过阈值后，就会被移除掉，默认阈值为60秒。
        if (!updateLock.get() && invokers.size() != map.size()) {
            if (updateLock.compareAndSet(false, true)) {
                try {
                    ConcurrentMap<String, WeightedRoundRobin> newMap = new ConcurrentHashMap<String, WeightedRoundRobin>();
                    // 拷贝
                    newMap.putAll(map);

                    // 遍历修改，即移除过期记录
                    Iterator<Map.Entry<String, WeightedRoundRobin>> it = newMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, WeightedRoundRobin> item = it.next();
                        if (now - item.getValue().getLastUpdate() > RECYCLE_PERIOD) {
                            it.remove();
                        }
                    }

                    // 更新引用
                    methodWeightMap.put(key, newMap);
                } finally {
                    updateLock.set(false);
                }
            }
        }

        if (selectedInvoker != null) {
            // 让 current 减去权重总和，等价于 current -= totalWeight
            selectedWRR.sel(totalWeight);
            // 返回具有最大 current 的 Invoker
            return selectedInvoker;
        }

        // should not happen here
        return invokers.get(0);
    }
}
