package com.yves.service;

import com.yves.dao.OrderDao;
import com.yves.feign.AccountFeign;
import com.yves.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderServiceImpl {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AccountFeign accountFeign;

    public Order create(String userId, String commodityCode, int orderCount) {

        int orderMoney = calculate(commodityCode, orderCount);

        accountFeign.debit(userId, orderMoney);

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setOrderCount(orderCount);
        order.setMoney(orderMoney);

        // INSERT INTO orders ...
        boolean result = orderDao.insert(order) > 0;
        if(result){
            //throw new RuntimeException("创建订单失败");
            return null;
        }
        return order;
    }

    private int calculate(String commodityCode, int orderCount) {

        return orderCount * 10;
    }
}
