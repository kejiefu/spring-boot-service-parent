package com.mountain.elasticsearch.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther kejiefu
 * @Date 2020/1/3 0003
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    @Test
    public void insertUser() throws Exception {
        userService.insertUser();
    }

    @Test
    public void batchInsertUser() throws Exception {
        userService.batchInsertUser();
    }

    @Test
    public void getUserById() throws Exception {
        userService.getUserById();
    }

    @Test
    public void getUser() throws Exception {
        userService.getUser();
    }

    @Test
    public void listUser() throws Exception {
        userService.listUser();
    }

}