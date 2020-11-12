package com.mountain.project.websocket.service;


import com.mountain.project.websocket.core.common.RequestData;

public interface BananaService {
	
	// 服务端发送消息给客户端
	void send(RequestData request) throws Exception;
	
}
