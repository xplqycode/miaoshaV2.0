package com.pxu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 21:00
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ImportResource(locations={"classpath:application-esjob.xml"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
