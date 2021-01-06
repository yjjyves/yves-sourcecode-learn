package com.yves.feign;

import com.yves.model.Order;

public interface OrderFeign {

    /**
     * 创建订单
     */
    Order create(String userId, String commodityCode, int orderCount);
}