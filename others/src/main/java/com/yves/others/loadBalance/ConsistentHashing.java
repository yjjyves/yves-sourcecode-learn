package com.yves.others.loadBalance;

import java.util.List;
import java.util.TreeMap;

/**
 * 源地址hash法
 */
public class ConsistentHashing {

    // 使用 TreeMap 存储 Invoker 虚拟节点
    private final TreeMap<Long, Invoker> virtualInvokers = new TreeMap<>();


    protected static Invoker doSelect(List<Invoker> invokers, String ip) {
        int length = invokers.size();

        int hashCode = ip.hashCode();
        //获取在服务列表中的下标
        int serverPos = hashCode % length;
        return invokers.get(serverPos);
    }

}
