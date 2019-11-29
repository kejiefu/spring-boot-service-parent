package com.mountain.mongodb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;


/**
 * 监听ContextRefreshedEvent事件，当所有的bean都初始化完成并被成功装载后会触发该事件，
 * 实现ApplicationListener<ContextRefreshedEvent>接口可以收到监听动作，然后可以写自己的逻辑。
 */
@Configuration
public class ApplicationListenerConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        MongoConverter converter = mongoTemplate.getConverter();
        if (converter.getTypeMapper().isTypeKey("_class")) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
