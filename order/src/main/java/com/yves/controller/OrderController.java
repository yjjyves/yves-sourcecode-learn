package com.yves.controller;

import com.yves.service.BusinessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order/*")
public class OrderController {

    @Autowired
    private BusinessServiceImpl businessService;

    @RequestMapping("/create")
    public String create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode,
                         @RequestParam("orderCount") Integer orderCount) {
        businessService.purchase(userId, commodityCode, orderCount);
        return "success";
    }
}
