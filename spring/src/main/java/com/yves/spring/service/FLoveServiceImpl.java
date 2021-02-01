package com.yves.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FLoveServiceImpl {
    @Autowired
    private Abean abean;

    @Value("${value}")
    private int value;

    public void test_out() {

    }
}
