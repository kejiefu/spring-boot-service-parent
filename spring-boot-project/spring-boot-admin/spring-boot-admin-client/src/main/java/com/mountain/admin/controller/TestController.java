package com.mountain.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther kejiefu
 * @Date 2019/10/23 0023
 */

@RestController
@Slf4j
public class TestController {

    @RequestMapping("/test")
    public String test() {
        log.info("~~~~~~~~~~~~~~~~ test~~~~~~~~~~~~~~~~!");
        try {
            Thread.sleep(10000L);
        }catch (Exception ex){

        }
        return "hello world";
    }

    @RequestMapping("/test2")
    public String test2() {
        log.info("~~~~~~~~~~~~~~~~ test2 ~~~~~~~~~~~~~~~~!");
        try {
            Thread.sleep(100000L);
        }catch (Exception ex){

        }
        return "hello world";
    }
}
