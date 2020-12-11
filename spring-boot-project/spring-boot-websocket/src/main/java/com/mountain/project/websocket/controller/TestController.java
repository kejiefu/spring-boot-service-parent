package com.mountain.project.websocket.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Date 2020/11/13 15:36
 * @Created by kejiefu
 */
@Controller
public class TestController {

    //2分钟只能承受40个请求,每秒承受大概0.33个请求
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(0.33);
        List<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new UserRequest(i));
        }
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (Runnable runnable : tasks) {
            System.out.println("等待时间：" + rateLimiter.acquire());
            runnable.run();
            //直接抛弃多余的请求,不做等待
            /*if(rateLimiter.tryAcquire()){
                threadPool.execute(runnable);
            }*/
        }
    }

    private static class UserRequest implements Runnable {
        private int id;

        public UserRequest(int id) {
            this.id = id;
        }

        public void run() {
            System.out.println("我拿到令牌了，标号：" + id);
        }
    }


}
