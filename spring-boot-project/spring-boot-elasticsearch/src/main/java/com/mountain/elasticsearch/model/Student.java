package com.mountain.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @Auther kejiefu
 * @Date 2020/1/2 0002
 */
@Data
@Document(indexName = "student", shards = 1, replicas = 0)
public class Student implements Serializable {

    @Id
    private String id;

    private String name;


}