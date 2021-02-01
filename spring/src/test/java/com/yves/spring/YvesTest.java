package com.yves.spring;


import org.junit.Test;

import java.math.BigDecimal;

public class YvesTest {

    @Test
    public void test() {
        BigDecimal sumRechargeRemainingAmount = BigDecimal.ONE;
        sumRechargeRemainingAmount = sumRechargeRemainingAmount.add(BigDecimal.TEN);
        String msg = String.format("赠送%s元!", BigDecimal.valueOf(10.00).intValue());
        System.err.println(msg);
        System.err.println(sumRechargeRemainingAmount.negate());
    }
}
