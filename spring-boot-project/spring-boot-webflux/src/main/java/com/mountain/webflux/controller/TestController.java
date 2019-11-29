package com.mountain.webflux.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @Auther kejiefu
 * @Date 2019/11/18 0018
 * WebFlux 并不能使接口的响应时间缩短，它仅仅能够提升吞吐量和伸缩性。
 * 2019-11-18 10:35:15.826  INFO 11768 --- [nio-8080-exec-9] c.m.webflux.controller.TestController    : get1 start
 * 2019-11-18 10:35:20.826  INFO 11768 --- [nio-8080-exec-9] c.m.webflux.controller.TestController    : get1 end.
 * 2019-11-18 10:35:25.496  INFO 11768 --- [io-8080-exec-10] c.m.webflux.controller.TestController    : get2 start
 * 2019-11-18 10:35:25.496  INFO 11768 --- [io-8080-exec-10] c.m.webflux.controller.TestController    : get2 end.
 * 从调用者(浏览器)的角度而言，是感知不到有什么变化的，因为都是得等待5s才返回数据。但是，从服务端的日志我们可以看出，WebFlux是直接返回Mono对象的(而不是像SpringMVC一直同步阻塞5s，线程才返回)。
这正是WebFlux的好处：能够以固定的线程来处理高并发（充分发挥机器的性能）。
 */
@RestController
public class TestController {

    private final static Logger log = LoggerFactory.getLogger(TestController.class);

    // 阻塞5秒钟
    private String createStr() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }
        return "some string";
    }

    // 普通的SpringMVC方法
    @GetMapping("/1")
    private String get1() {
        log.info("get1 start");
        String result = createStr();
        log.info("get1 end.");
        return result;
    }

    // WebFlux(返回的是Mono)
    @GetMapping("/2")
    private Mono<String> get2() {
        log.info("get2 start");
        Mono<String> result = Mono.fromSupplier(() -> createStr());
        log.info("get2 end.");
        return result;
    }

    /**
     * Flux : 返回0-n个元素
     * 注：需要指定MediaType
     * WebFlux还支持服务器推送(SSE - >Server Send Event)，我们来看个例子：
     * @return
     */
    @GetMapping(value = "/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> flux() {
        Flux<String> result = Flux
                .fromStream(IntStream.range(1, 5).mapToObj(i -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    return "flux data--" + i;
                }));
        return result;
    }
}
