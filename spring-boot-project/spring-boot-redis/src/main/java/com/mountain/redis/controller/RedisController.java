package com.mountain.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Auther kejiefu
 * @Date 2019/10/24 0024
 */
@RestController
/**
 redisTemplate.opsForValue();　　//操作字符串
 redisTemplate.opsForHash();　　 //操作hash
 redisTemplate.opsForList();　　 //操作list
 redisTemplate.opsForSet();　　  //操作set
 redisTemplate.opsForZSet();　 　//操作有序set
 redisTemplate有两个方法经常用到,一个是opsForXXX一个是boundXXXOps,XXX是value的类型,
 前者获取到一个Opercation,但是没有指定操作的key,可以在一个连接(事务)内操作多个key以及对应的value;
 后者会获取到一个指定了key的operation,在一个连接内只操作这个key对应的value.
 */
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/test")
    public void test() {

        stringRedisTemplate.opsForValue().set("test", "100", 60 * 10000, TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间

        stringRedisTemplate.boundValueOps("test").increment(-1);//val做-1操作

        stringRedisTemplate.opsForValue().get("test");//根据key获取缓存中的val

        stringRedisTemplate.boundValueOps("test").increment(1);//val +1

        stringRedisTemplate.getExpire("test");//根据key获取过期时间

        stringRedisTemplate.getExpire("test", TimeUnit.SECONDS);//根据key获取过期时间并换算成指定单位

        stringRedisTemplate.delete("test");//根据key删除缓存

        stringRedisTemplate.hasKey("546545");//检查key是否存在，返回boolean值

        stringRedisTemplate.opsForSet().add("red_123", "1", "2", "3");//向指定key中存放set集合

        stringRedisTemplate.expire("red_123", 1000, TimeUnit.MILLISECONDS);//设置过期时间

        stringRedisTemplate.opsForSet().isMember("red_123", "1");//根据key查看集合中是否存在指定数据

        stringRedisTemplate.opsForSet().members("red_123");//根据key获取set集合


    }


}
