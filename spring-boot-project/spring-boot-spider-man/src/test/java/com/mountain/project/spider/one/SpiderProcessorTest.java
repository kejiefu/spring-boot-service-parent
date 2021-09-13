package com.mountain.project.spider.one;

import com.mountain.project.spider.Application;
import com.mountain.project.spider.processor.LoggerPipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;


/**
 * @Auther kejiefu
 * @Date 2019/1/18 0018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SpiderProcessorTest {

    @Autowired
    SpiderProcessor spiderProcessor;

    @Test
    public void addActivity() {
        Spider spider = Spider.create(spiderProcessor);
        for (int i = 1; i < 4000; i++) {
            String url = "http://wufazhuce.com/article/" + i;
            spider.addUrl(url);
        }
        spider.addPipeline(new LoggerPipeline());
        spider.thread(100);
        spider.run();
    }

}
