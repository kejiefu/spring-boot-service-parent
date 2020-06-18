package com.mountain.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;
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

        // 1. 最常见的使用方法
        //redissonClient.getLock("redisson-test-lock-1").lock();

        // 2. 支持过期解锁功能,10秒钟以后自动解锁, 无需调用unlock方法手动解锁
        //redissonClient.getLock("redisson-test-lock-2").lock(10, TimeUnit.SECONDS);
        //log.info("getLock,currentTimeMillis:{}", System.currentTimeMillis());

        // locking
        RLock rLock = redissonClient.getLock("redisson-test-lock-3");
        try {
            //尝试加锁，最多等待3秒，上锁以后10秒自动解锁(意思是并发了两个线程，第二个线程在3秒后自动会执行下面to do的方法，但是没有获取到锁)
            boolean tryLockFlag = rLock.tryLock(3, 10, TimeUnit.SECONDS);
            //to do ..
            log.info("tryLock.flag:{}", tryLockFlag);
            Thread.sleep(5000L);
            //同时执行两个线程，如果这里执行超过5秒的话，第二个线程进来tryLockFlag会为false
        } catch (Exception ex) {
            //to do ..
            log.info("tryLock.Exception:{}", ex);
        } finally {
            if (rLock.isLocked()) {
                log.info("threadName:{},unlock", Thread.currentThread().getName());
                if (rLock.isHeldByCurrentThread()) {
                    rLock.unlock(); //两个判断条件是非常必要的
                }
            }
        }
    }

    /**
     * Redisson同时还为分布式锁提供了异步执行
     */
    @RequestMapping("/redisson-test-2")
    public void test2() {
        log.info("threadName:{}", Thread.currentThread().getName());
        RLock rLock = redissonClient.getLock("anyLock");
        try {
            //rLock.lockAsync();
            //rLock.lockAsync(10, TimeUnit.SECONDS);
            //尝试加锁，最多等待3秒，上锁以后10秒自动解锁
            Future<Boolean> res = rLock.tryLockAsync(3, 10, TimeUnit.SECONDS);
            if (res.get()) {
                log.info("threadName:{},tryLockAsync:{}", Thread.currentThread().getName(), System.currentTimeMillis());
                Thread.sleep(5000L);
            }
            //同时执行两个线程，如果上面执行超过5秒的话，第二个线程进来在超过3秒之后就往下执行to do下面的方法
            // to do ..
            log.info("threadName:{},do:{}", Thread.currentThread().getName(), System.currentTimeMillis());
        } catch (Exception ex) {
            //to do ..
            log.info("threadName:{},tryLockAsync.Exception:{}", Thread.currentThread().getName(), ex);
        } finally {
            if (rLock.isLocked()) {
                if (rLock.isHeldByCurrentThread()) {
                    log.info("threadName:{},unlock", Thread.currentThread().getName());
                    rLock.unlock(); //两个判断条件是非常必要的
                }
            }
        }
    }

    /**
     * Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。在提供了自动过期解锁功能的同时，保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     */
    @RequestMapping("/redisson-test-3")
    public void test3() {
        RLock fairLock = redissonClient.getFairLock("anyLock");
        try {
            // 最常见的使用方法
            fairLock.lock();

            // 支持过期解锁功能, 10秒钟以后自动解锁,无需调用unlock方法手动解锁
            fairLock.lock(10, TimeUnit.SECONDS);

            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = fairLock.tryLock(100, 10, TimeUnit.SECONDS);

            Future<Boolean> res1 = fairLock.tryLockAsync(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            fairLock.unlock();
        }
    }
}
