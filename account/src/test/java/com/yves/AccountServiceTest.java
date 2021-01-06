package com.yves;

import com.yves.model.Account;
import com.yves.service.AccountService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceTest extends BaseTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void insert_test() {
        Account account = new Account();
        account.setMoney(100);
        account.setUserId("1001");

        boolean result = accountService.insert(account);

        System.err.println(result);
    }
}
