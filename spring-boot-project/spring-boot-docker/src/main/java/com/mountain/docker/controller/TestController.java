package com.mountain.docker.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther kejiefu
 * @Date 2019/4/25 0025
 */
@RestController
public class TestController {

    @RequestMapping("/")
    public String index() {
        return "Hello Docker!";
    }

}
