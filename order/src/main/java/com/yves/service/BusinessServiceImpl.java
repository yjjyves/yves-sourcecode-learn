package com.yves.service;

import com.yves.feign.StorageFeign;
import com.yves.model.Order;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl {

    @Autowired
    private StorageFeign storageFeign;

    @Autowired
    private OrderServiceImpl orderService;

    /**
     * 采购
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public void purchase(String userId, String commodityCode, int orderCount) {

        storageFeign.deduct(commodityCode, orderCount);

        Order order = orderService.create(userId, commodityCode, orderCount);
        if(order == null){
            throw new RuntimeException("创建订单失败");
        }
    }
}