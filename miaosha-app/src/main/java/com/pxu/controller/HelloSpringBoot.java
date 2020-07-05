package com.pxu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xupeng3@corp.netease.com
 * @date 2020/7/5 20:58
 */
@Controller
public class HelloSpringBoot {
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
