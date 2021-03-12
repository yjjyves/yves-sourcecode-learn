package com.yves.spring;

import com.yves.spring.service.Abean;
import com.yves.spring.service.CBean;
import com.yves.spring.service.CombatService;
import com.yves.spring.service.EBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement(proxyTargetClass = true)
public class IOCTest {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationTest.xml");
        CombatService cs = context.getBean(CombatService.class);
        cs.doInit();
        cs.combating();

        /*ApplicationContext context1 = new FileSystemXmlApplicationContext("e:/study/applicationTest.xml");
        cs = context1.getBean(CombatService.class);
        cs.doInit();
        cs.combating();

        ApplicationContext context1 = new GenericXmlApplicationContext("file:e:/study/applicationTest.xml");
        cs = context1.getBean(CombatService.class);
        cs.doInit();
        cs.combating();*/

        // 注解的方式
        ApplicationContext context2 = new AnnotationConfigApplicationContext(IOCTest.class);
        CombatService cs2 = context2.getBean(CombatService.class);
        cs2.combating();

        System.out.println("------------------------------------------------------");
        GenericApplicationContext context3 = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context3).loadBeanDefinitions("classpath:applicationTest.xml");
        new ClassPathBeanDefinitionScanner(context3).scan("com.yves.spring");
        // 一定要刷新
        context3.refresh();
     /*   cs2 = context3.getBean(CombatService.class);
        cs2.combating();*/
        Abean ab = context3.getBean(Abean.class);
        ab.doSomething();

        CBean cBean = context3.getBean(CBean.class);
        System.err.println(cBean.getDriverClassName());

        EBean eBean = context3.getBean(EBean.class);
        eBean.doSomething();


    }

    @Bean
    public CombatService getCombatService() {
        return new CombatService(120);
    }
}
