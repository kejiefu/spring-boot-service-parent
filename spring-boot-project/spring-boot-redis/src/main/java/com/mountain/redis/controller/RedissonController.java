package com.mountain.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Auther kejiefu
 * @Date 2020/6/11 0011
 */
@RestController
@Slf4j
public class RedissonController {

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/redisson-test")
    public void test() {

        redissonClient.getBucket("redisson-test").set("test", 100, TimeUnit.SECONDS);//设置桶存储的对象，设置操作的超时时间var2

        boolean flag1 = redissonClient.getBucket("redisson-test").trySet("test1", 100, TimeUnit.SECONDS);//尝试设置桶的新值，设置超时时间var2,setnx
        log.info("flag1:{}", flag1);

        boolean flag2 = redissonClient.getBucket("redisson-test").compareAndSet("test", "test2");//原子替换桶的新值为var2
        log.info("flag2:{}", flag2);

        Object flag3 = redissonClient.getBucket("redisson-test").getAndSet("test3", 100, TimeUnit.SECONDS);//返回桶的旧值，设置新值，设置操作的超时时间var2
        log.info("flag3:{}", flag3);


    }

}
