package com.yves.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yves-storage", path = "/storage", contextId = "storage")
public interface StorageFeign {

    /**
     * 扣除存储数量
     */
    @RequestMapping("/deduct")
    void deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count);
}