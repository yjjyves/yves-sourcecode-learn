package com.yves.service;

import com.yves.dao.AccountDao;
import com.yves.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 从用户账户中借出
     */
    public void debit(String userId, int money) {
        Account account = accountDao.getByUserId(userId);
        if(account == null){
            return;
        }
        account.setMoney(account.getMoney() - money);

        accountDao.update(account);
    }

    public boolean insert(Account account) {
        return accountDao.insert(account) > 0;
    }

}
