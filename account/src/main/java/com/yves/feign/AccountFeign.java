package com.yves.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yves-account", path = "/account", contextId = "account")
public interface AccountFeign {

    /**
     * 从用户账户中借出
     */
    @RequestMapping("/debit")
    void debit(@RequestParam("userId") String userId, @RequestParam("money") int money);
}