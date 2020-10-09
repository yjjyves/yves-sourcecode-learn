package com.yves.spring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class Bbean {

    public void sayHello() {
        System.out.println(this + " 大哥，进来玩会！");
    }

    @Bean
    public EBean eBean() {
        return new EBean();
    }
}
