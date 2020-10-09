package com.yves.others.loadBalance;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class LeastActiveLoadBalance {
    private static final Random random = new Random();
    //这个需要每次调用+1,用完-1
    private static Map<String, Integer> activeMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        activeMap.put("A",3);
        activeMap.put("B",3);
        activeMap.put("C",4);
        List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("A", 5);
        Invoker invoker2 = new Invoker("B", 4);
        Invoker invoker3 = new Invoker("C", 1);

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
         A
         A
         B
         A
         A
         A
         A
         A
         */

    }


    /**
     * 主要思路：
     * 1.遍历所有的节点,找到最小活跃数的那个节点
     * 2.如果只有一个最小活跃数,那就返回,如果有多个一样的活跃数,那就按照权重随机
     *
     * @param invokers
     * @return
     */
    protected static Invoker doSelect(List<Invoker> invokers) {
        int length = invokers.size();
        // 最小的活跃数
        int leastActive = -1;
        // 具有相同“最小活跃数”的服务者提供者（以下用 Invoker 代称）数量
        int leastCount = 0;
        // leastIndexs 用于记录具有相同“最小活跃数”的 Invoker 在 invokers 列表中的下标信息
        int[] leastIndexs = new int[length];
        int totalWeight = 0;
        // 第一个最小活跃数的 Invoker 权重值，用于与其他具有相同最小活跃数的 Invoker 的权重进行对比，
        // 以检测是否“所有具有相同最小活跃数的 Invoker 的权重”均相等
        int firstWeight = 0;
        boolean sameWeight = true;

        // 遍历 invokers 列表
        for (int i = 0; i < length; i++) {
            Invoker invoker = invokers.get(i);
            // 获取 Invoker 对应的活跃数
            int active = activeMap.get(invoker.getIp());

            // 获取权重 - ⭐
            int weight = invoker.getWeight();
            // 发现更小的活跃数，重新开始
            if (leastActive == -1 || active < leastActive) {
                // 使用当前活跃数 active 更新最小活跃数 leastActive
                leastActive = active;
                // 更新 leastCount 为 1
                leastCount = 1;
                // 记录当前下标值到 leastIndexs 中
                leastIndexs[0] = i;
                totalWeight = weight;
                firstWeight = weight;
                sameWeight = true;

                // 当前 Invoker 的活跃数 active 与最小活跃数 leastActive 相同
            } else if (active == leastActive) {
                // 在 leastIndexs 中记录下当前 Invoker 在 invokers 集合中的下标
                leastIndexs[leastCount++] = i;
                // 累加权重
                totalWeight += weight;
                // 检测当前 Invoker 的权重与 firstWeight 是否相等，
                // 不相等则将 sameWeight 置为 false
                if (sameWeight && i > 0
                        && weight != firstWeight) {
                    sameWeight = false;
                }
            }
        }

        // 当只有一个 Invoker 具有最小活跃数，此时直接返回该 Invoker 即可
        if (leastCount == 1) {
            return invokers.get(leastIndexs[0]);
        }

        // 有多个 Invoker 具有相同的最小活跃数，但它们之间的权重不同
        if (!sameWeight && totalWeight > 0) {
            // 随机生成一个 [0, totalWeight) 之间的数字
            int offsetWeight = random.nextInt(totalWeight);
            // 循环让随机数减去具有最小活跃数的 Invoker 的权重值，
            // 当 offset 小于等于0时，返回相应的 Invoker
            for (int i = 0; i < leastCount; i++) {
                int leastIndex = leastIndexs[i];
                // 获取权重值，并让随机数减去权重值 - ⭐️
                offsetWeight -= invokers.get(leastIndex).getWeight();
                if (offsetWeight <= 0)
                    return invokers.get(leastIndex);
            }
        }
        // 如果权重相同或权重为0时，随机返回一个 Invoker
        return invokers.get(leastIndexs[random.nextInt(leastCount)]);
    }

}
