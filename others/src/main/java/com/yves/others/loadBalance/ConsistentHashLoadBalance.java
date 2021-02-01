package com.yves.others.loadBalance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsistentHashLoadBalance {

    private final ConcurrentMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        /*List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("A");
        Invoker invoker2 = new Invoker("B");
        Invoker invoker3 = new Invoker("C");
        invokers.add(invoker);
        invokers.add(invoker2);
        invokers.add(invoker3);

        Invoker targetInvoker = new ConsistentHashLoadBalance().doSelect(invokers, "");
        System.err.println(" " + targetInvoker.getIp());*/

        TreeMap treeMap = new TreeMap();
        treeMap.put(0,"A");
        treeMap.put(1,"B");
        treeMap.put(4,"D");
        treeMap.put(2,"C");
        treeMap.put(5,"E");

        System.err.println(treeMap.tailMap(3).firstKey());
    }

    protected Invoker doSelect(List<Invoker> invokers, String arg) {
        //String methodName = RpcUtils.getMethodName(invocation);
        String key = "doSelect";

        // 获取 invokers 原始的 hashcode
        int identityHashCode = System.identityHashCode(invokers);
        ConsistentHashSelector selector = selectors.get(key);
        // 如果 invokers 是一个新的 List 对象，意味着服务提供者数量发生了变化，可能新增也可能减少了。
        // 此时 selector.identityHashCode != identityHashCode 条件成立
        if (selector == null || selector.identityHashCode != identityHashCode) {
            // 创建新的 ConsistentHashSelector
            selectors.put(key, new ConsistentHashSelector(invokers, identityHashCode));
            selector = selectors.get(key);
        }

        // 调用 ConsistentHashSelector 的 select 方法选择 Invoker
        return selector.select(arg);
    }


    private static final class ConsistentHashSelector {

        // 使用 TreeMap 存储 Invoker 虚拟节点
        private final TreeMap<Long, Invoker> virtualInvokers;

        private final int replicaNumber;

        private final int identityHashCode;

        private final int[] argumentIndex;

        ConsistentHashSelector(List<Invoker> invokers, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;
            // 获取虚拟节点数，默认为160
            this.replicaNumber = 160;
            // 获取参与 hash 计算的参数下标值，默认对第一个参数进行 hash 运算
            String[] index = {"0"};
            argumentIndex = new int[index.length];
            for (int i = 0; i < index.length; i++) {
                argumentIndex[i] = Integer.parseInt(index[i]);
            }
            for (Invoker invoker : invokers) {
                String address = invoker.getIp();
                for (int i = 0; i < replicaNumber / 4; i++) {
                    // 对 address + i 进行 md5 运算，得到一个长度为16的字节数组
                    int newAddressHash = getHash(address + i);
                    // 对 digest 部分字节进行4次 hash 运算，得到四个不同的 long 型正整数
                    for (int h = 0; h < 4; h++) {
                        // h = 0 时，取 digest 中下标为 0 ~ 3 的4个字节进行位运算
                        // h = 1 时，取 digest 中下标为 4 ~ 7 的4个字节进行位运算
                        // h = 2, h = 3 时过程同上
                        long m = getHash(newAddressHash + "" + h);
                        // 将 hash 到 invoker 的映射关系存储到 virtualInvokers 中，
                        // virtualInvokers 需要提供高效的查询操作，因此选用 TreeMap 作为存储结构
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        public Invoker select(String arg) {
            return selectForKey(getHash(arg));
        }

        private Invoker selectForKey(long hash) {
            // 到 TreeMap 中查找第一个节点值大于或等于当前 hash 的 Invoker
            Map.Entry<Long, Invoker> entry = virtualInvokers.tailMap(hash, true).firstEntry();
            // 如果 hash 大于 Invoker 在圆环上最大的位置，此时 entry = null，
            // 需要将 TreeMap 的头节点赋值给 entry
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }

            // 返回 Invoker
            return entry.getValue();
        }
    }

    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
