package com.pxu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 21:00
 */
@SpringBootApplication
@EnableScheduling
@ImportResource(locations={"classpath:application-esjob.xml"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
