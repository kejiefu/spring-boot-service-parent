package com.mountain.project.spider.Processor;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Iterator;
import java.util.Map;

/**
 * @Auther kejiefu
 * @Date 2019/1/18 0018
 */
public class LoggerPipeline implements Pipeline {


    public LoggerPipeline() {
    }

    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        Iterator var3 = resultItems.getAll().entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) var3.next();
            System.out.println((String) entry.getKey() + ":\t" + entry.getValue());
        }

    }

}
