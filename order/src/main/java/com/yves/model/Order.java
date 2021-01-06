package com.yves.model;


import lombok.Data;

@Data
public class Order {
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer orderCount;

    private Integer money;
}
