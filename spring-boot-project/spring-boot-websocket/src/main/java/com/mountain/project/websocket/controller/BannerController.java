package com.mountain.project.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther kejiefu
 * @Date 2019/2/15 0015
 */
@Controller
public class BannerController {

    @RequestMapping(value = "/websocket/index", method = RequestMethod.GET)
    public String index() {
        return "/socket/banana.html";
    }

}
