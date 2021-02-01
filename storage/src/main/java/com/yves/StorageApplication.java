package com.yves;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.yves.dao"})
@EnableAutoDataSourceProxy
public class StorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);

        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService("172.24.4.134:8848");
            configService.addListener("yves-app", "local114", new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    System.err.println("receive: " + s);
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }

        System.err.println("============================");


    }


}
