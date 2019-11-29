package com.mountain.redis.controller;

import com.mountain.redis.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther kejiefu
 * @Date 2019/10/24 0024
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisControllerTest {

    @Autowired
    RedisController testController;

    @Test
    public void test() throws Exception {
        testController.test();
    }

}