package com.yves.spring.demo;

import com.yves.spring.service.DBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DBean.class)
public class Config {

}
