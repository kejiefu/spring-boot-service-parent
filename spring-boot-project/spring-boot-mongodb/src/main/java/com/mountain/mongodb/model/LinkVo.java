package com.mountain.mongodb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther kejiefu
 * @Date 2019/4/30 0030
 */
@Getter
@Setter
@Accessors(chain = true)
public class LinkVo implements Serializable {

    private static final long serialVersionUID = -7373962522715313232L;

    /**
     * id
     */
    private long id;
    /**
     * 长链接
     */
    private String url;
    /**
     * 时间周期
     */
    private Integer statisticalPeriod;
    /**
     * 过期时间
     */
    private Integer expireTime;
    /**
     * 创建时间
     */
    private Integer createTime;
    /**
     * 更新时间
     */
    private Integer updateTime;

}
