package com.yves.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.yves.model.Account;

@Mapper
public interface AccountDao {
    int insert(@Param("account") Account account);

    int insertSelective(@Param("account") Account account);

    int insertList(@Param("accounts") List<Account> accounts);

    int update(@Param("account") Account account);

    Account getByUserId(String userId);

}
