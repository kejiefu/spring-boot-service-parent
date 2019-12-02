package com.mountain.kafka.worker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyListener {

    @KafkaListener(topics = "myTopic")
    public void processMessage2(String content) {
        log.info("【Received Message From 'myTopic'】: {}", content);
    }

}