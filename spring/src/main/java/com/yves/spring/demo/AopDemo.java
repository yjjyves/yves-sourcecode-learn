package com.yves.spring.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopDemo {

    @Pointcut("execution(* com.yves.spring.service.FLoveServiceImpl.*(..))")
    public void save() {
        // 保存信息
    }

    // 保存用户信息后
    @After(value = "save()")
    public void saveUserInfoHandle(JoinPoint joinPoint) {
        System.out.println("");
    }

}
