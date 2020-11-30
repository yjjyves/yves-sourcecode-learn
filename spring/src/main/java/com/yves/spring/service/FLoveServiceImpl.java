package com.yves.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FLoveServiceImpl {
    private Abean abean;

    @Value("${value}")
    private int value;
}
