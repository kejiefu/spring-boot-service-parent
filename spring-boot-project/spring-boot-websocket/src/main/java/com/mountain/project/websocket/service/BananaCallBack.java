package com.mountain.project.websocket.service;


import com.mountain.project.websocket.core.common.Request;

public interface BananaCallBack {
	
	// 服务端发送消息给客户端
	void send(Request request) throws Exception;
	
}
