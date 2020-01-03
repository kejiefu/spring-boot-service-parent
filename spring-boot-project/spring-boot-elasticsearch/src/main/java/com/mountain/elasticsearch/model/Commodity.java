package com.mountain.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * ElasticSearch在7.X版本去掉type
 */
@Data
@Document(indexName = "commodity", shards = 1, replicas = 0)
public class Commodity implements Serializable {

    @Id
    private String skuId;

    private String name;

    private String category;

    private Integer price;

    private String brand;

    private Integer stock;

}