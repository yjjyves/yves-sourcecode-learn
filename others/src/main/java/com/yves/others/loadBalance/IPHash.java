package com.yves.others.loadBalance;

import java.util.ArrayList;
import java.util.List;

public class IPHash {

    public static void main(String[] args) {
        List<Invoker> invokers = new ArrayList<>();
        Invoker invoker = new Invoker("A");
        Invoker invoker2 = new Invoker("B");
        Invoker invoker3 = new Invoker("C");

        invokers.add(invoker);
        invokers.add(invoker2);
        invokers.add(invoker3);

        for (int i = 0; i < 7; i++) {
            Invoker selectInvoker = doSelect(invokers, "D");
            System.err.println(selectInvoker.getIp());
        }

        /**
         * 调用结果
         C
         C
         C
         C
         C
         C
         C
         */

    }


    protected static Invoker doSelect(List<Invoker> invokers, String ip) {
        int length = invokers.size();

        int hashCode = ip.hashCode();
        //获取在服务列表中的下标
        int serverPos = hashCode % length;
        return invokers.get(serverPos);
    }
}
