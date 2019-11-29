package com.mountain.project.spider.one.model;

import java.io.Serializable;

/**
 * @Auther kejiefu
 * @Date 2019/1/18 0018
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer number;
    private String title;
    private String content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
