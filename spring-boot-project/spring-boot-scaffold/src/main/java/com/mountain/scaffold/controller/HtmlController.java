package com.mountain.scaffold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther kejiefu
 * @Date 2018/8/29 0029
 */
@Controller
public class HtmlController {

    @RequestMapping(value = "/1/index", method = RequestMethod.GET)
    public String t1() {
        return "/1/index.html";
    }

}
