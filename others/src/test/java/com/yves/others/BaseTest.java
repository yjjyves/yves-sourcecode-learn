package com.yves.others;


import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.yves.others"})
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class BaseTest {
    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;


    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        System.out.println("--------------开始---------------");
    }


    @After
    public void after() {
        System.out.println("--------------结束---------------");
    }
}
