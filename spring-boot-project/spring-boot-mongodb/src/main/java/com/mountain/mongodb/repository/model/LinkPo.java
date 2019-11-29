package com.mountain.mongodb.repository.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @Auther kejiefu
 * @Date 2019/4/30 0030
 */
@Getter
@Setter
@Accessors(chain = true)
@Document(collection = "link")
public class LinkPo implements Serializable {

    private static final long serialVersionUID = -7373962522715313232L;

    /**
     * id
     */
    @Id
    private long id;
    /**
     * 长链接
     */
    @Field("url")
    private String url;
    /**
     * 时间周期
     */
    @Field("sp")
    private Integer statisticalPeriod;
    /**
     * 过期时间
     */
    @Field("et")
    private Integer expireTime;
    /**
     * 创建时间
     */
    @Field("ct")
    private Integer createTime;
    /**
     * 更新时间
     */
    @Field("ut")
    private Integer updateTime;

}
