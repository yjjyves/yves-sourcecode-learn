package com.yves.controller;

import com.yves.feign.AccountFeign;
import com.yves.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController implements AccountFeign {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/debit")
    @Override
    public void debit(String userId, int money) {
        accountService.debit(userId, money);
    }
}
