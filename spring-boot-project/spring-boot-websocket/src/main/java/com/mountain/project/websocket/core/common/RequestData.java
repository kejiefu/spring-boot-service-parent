package com.mountain.project.websocket.core.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

public class RequestData {

    /**
     * 客户端的唯一id
     */
    private String requestId;
    /**
     * 请求类型
     */
    private Integer serviceId;
    /**
     * 名称
     */
    private String name;
    /**
     * 消息
     */
    private String message;

    public String getRequestId() {
        return requestId;
    }

    public RequestData setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public RequestData setServiceId(int serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getName() {
        return name;
    }

    public RequestData setName(String name) {
        this.name = name;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RequestData setMessage(String message) {
        this.message = message;
        return this;
    }

    public static RequestData create(String json) {
        if (!Strings.isNullOrEmpty(json)) {
            return JSONObject.parseObject(json, RequestData.class);
        }
        return null;
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public String toString() {
        return toJson();
    }

}
