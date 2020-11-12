package com.mountain.project.websocket.service.impl;

import com.mountain.project.websocket.core.common.CodeEnum;
import com.mountain.project.websocket.core.common.RequestData;
import com.google.common.base.Strings;
import com.mountain.project.websocket.service.BananaService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BananaServiceImpl implements BananaService {

	private static final Logger logger = LoggerFactory.getLogger(BananaServiceImpl.class);

	// <requestId, BananaService>
	public static final Map<String, BananaService> bananaWatchMap = new ConcurrentHashMap<>();

	//每个BananaService对象都有一个ChannelHandlerContext
	//管道处理上下文，便于服务器推送数据到客户端
	private ChannelHandlerContext ctx;

	private String name;
	
	public BananaServiceImpl(ChannelHandlerContext ctx, String name) {
		this.ctx = ctx;
		this.name = name;
	}

	public static boolean register(String requestId, BananaService bananaService) {
		if (Strings.isNullOrEmpty(requestId) || bananaWatchMap.containsKey(requestId)) {
			return false;
		}
		bananaWatchMap.put(requestId, bananaService);
		return true;
	}
	
	public static boolean logout(String requestId) {
		if (Strings.isNullOrEmpty(requestId) || !bananaWatchMap.containsKey(requestId)) {
			return false;
		}
		bananaWatchMap.remove(requestId);
		return true;
	}
	
	@Override
	public void send(RequestData request) throws Exception {
		if (this.ctx == null || this.ctx.isRemoved()) {
			throw new Exception("尚未握手成功，无法向客户端发送WebSocket消息");
		}
		this.ctx.channel().write(new TextWebSocketFrame(request.toJson()));
		this.ctx.flush();
	}
	
	
	/**
	 * 通知所有机器有机器下线
	 * @param requestId
	 */
	public static void notifyDownLine(String requestId) {
		BananaServiceImpl.bananaWatchMap.forEach((reqId, callBack) -> { // 通知有人下线
			RequestData serviceRequest = new RequestData();
			serviceRequest.setServiceId(CodeEnum.DOWN_LINE.code);
			serviceRequest.setRequestId(requestId);
			try {
				callBack.send(serviceRequest);
			} catch (Exception e) {
				logger.warn("回调发送消息给客户端异常", e);
			}
		});
	}
	
	public String getName() {
		return name;
	}

}
