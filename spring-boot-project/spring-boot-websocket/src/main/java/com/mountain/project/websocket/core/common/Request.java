package com.mountain.project.websocket.core.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

public class Request {
	
	private String requestId;
	private Integer serviceId;
	private String name;
	private String message;
	
	public String getRequestId() {
		return requestId;
	}
	public Request setRequestId(String requestId) {
		this.requestId = requestId;
		return this;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public Request setServiceId(int serviceId) {
		this.serviceId = serviceId;
		return this;
	}
	public String getName() {
		return name;
	}
	public Request setName(String name) {
		this.name = name;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public Request setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public static Request create(String json) {
		if (!Strings.isNullOrEmpty(json)) {
			return JSONObject.parseObject(json, Request.class);
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
