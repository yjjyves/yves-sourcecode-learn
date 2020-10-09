package com.yves.others.loadBalance;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoker {
    private String ip;
    private int weight;

    public Invoker(String ip) {
        this.ip = ip;
    }

    /**
     * 将权重递减
     */
    public void decrement() {
        this.weight--;
    }

}
