package com.luli.nebulapics;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableAsync
@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
@MapperScan("com.luli.nebulapics.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
@CrossOrigin(origins = {"http://localhost:8081"},allowCredentials = "true")
public class NebulaPicsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NebulaPicsApplication.class, args);
    }
}
