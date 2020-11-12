package com.mountain.project.websocket.core.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {

    /**
     * 客户端的唯一id
     */
    private String requestId;
    /**
     * 请求类型
     */
    private Integer serviceId;
    /**
     * 是否成功
     */
    private Boolean isSucc;
    /**
     * 名称
     */
    private String name;
    /**
     * 消息
     */
    private String message;

    private Map<String, String> hadOnline = new HashMap<String, String>(); // <requestId, name>

    public String getRequestId() {
        return requestId;
    }

    public ResponseData setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public int getServiceId() {
        return serviceId;
    }

    public ResponseData setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public Boolean getIsSucc() {
        return isSucc;
    }

    public ResponseData setIsSucc(boolean isSucc) {
        this.isSucc = isSucc;
        return this;
    }

    public String getName() {
        return name;
    }

    public ResponseData setName(String name) {
        this.name = name;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseData setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, String> getHadOnline() {
        return hadOnline;
    }

    public ResponseData setHadOnline(Map<String, String> hadOnline) {
        this.hadOnline = hadOnline;
        return this;
    }

    public static ResponseData create(String json) {
        if (!Strings.isNullOrEmpty(json)) {
            return JSONObject.parseObject(json, ResponseData.class);
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
