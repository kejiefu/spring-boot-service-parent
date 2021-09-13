package com.mountain.project.spider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author kejiefu
 * @Description TODO
 * @Date 2021/6/4 18:09
 * @Created by kejiefu
 */
@Configuration
public class RestTemplateConfig {

    @Bean(value = "restTemplate")
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(60000);
        return new RestTemplate(factory);
    }

}
