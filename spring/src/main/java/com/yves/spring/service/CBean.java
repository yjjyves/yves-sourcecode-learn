package com.yves.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

public class CBean {
    private int years;

    private int integerProperty;

    private CombatService cService;

    @Resource
    private Abean aabean;

    @Value("${value}")
    private int value;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    public CBean() {
    }

    public CBean(int years) {
        this.years = years;
    }

    public CBean(@Value("${years}") int years, Abean aabean) {
        this.years = years;
        this.aabean = aabean;
    }

    public void init() {

    }

    public void cleanup() {

    }

    public void setcService(CombatService cService) {
        this.cService = cService;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
