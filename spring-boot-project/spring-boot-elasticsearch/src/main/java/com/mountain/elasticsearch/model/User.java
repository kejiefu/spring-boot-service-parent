package com.mountain.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @Auther kejiefu
 * @Date 2020/1/2 0002
 * ElasticSearch在7.X版本去掉type
 */
@Data
@Document(indexName = "user", shards = 5, replicas = 0)
public class User implements Serializable {

    @Id
    private String id;

    private String name;

    private Integer age;
}