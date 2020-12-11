package com.mountain.project.websocket.handler;

import com.google.common.base.Strings;
import com.mountain.project.websocket.core.common.CodeEnum;
import com.mountain.project.websocket.core.common.RequestData;
import com.mountain.project.websocket.core.common.ResponseData;
import com.mountain.project.websocket.model.RequestParser;
import com.mountain.project.websocket.service.BananaService;
import com.mountain.project.websocket.service.impl.BananaServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 每个实例都有一个BananaWebSocketServerHandler
 * WebSocket服务端Handler
 */
public class BananaWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(BananaWebSocketServerHandler.class);

    private WebSocketServerHandshaker handshaker;

    /**
     * 管道处理上下文，便于服务器推送数据到客户端
     */
    private ChannelHandlerContext ctx;

    private String sessionId;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead0:{}", msg);
        if (msg instanceof FullHttpRequest) {
            // 传统的HTTP接入
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            // WebSocket接入
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelReadComplete...");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("WebSocket异常", cause);
        ctx.close();
        logger.info(sessionId + " 	注销");
        // 注销
        BananaServiceImpl.logout(sessionId);
        // 通知有人下线
        BananaServiceImpl.notifyDownLine(sessionId);
    }

    private int lossConnectCount = 0;

    /**
     * 心跳检查,websocket不会触发这个
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("已经5秒未收到客户端的消息了！");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    logger.info("关闭这个不活跃通道！");
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 处理Http请求，完成WebSocket握手<br/>
     * 注意：WebSocket连接第一次请求使用的是Http
     *
     * @param ctx
     * @param request
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果HTTP解码失败，返回HTTP异常
        if (!request.decoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 正常WebSocket的Http连接请求，构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory("ws://" + request.headers().get(HttpHeaderNames.HOST),
                        null, false);
        handshaker = wsFactory.newHandshaker(request);
        // 无法处理的websocket版本
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            Map<String, String> paramMap = new RequestParser(request).parse(); // 将GET, POST所有请求参数转换成Map对象
            logger.info("paramMap:{}", paramMap);
            // 向客户端发送websocket握手,完成握手
            handshaker.handshake(ctx.channel(), request);
            // 记录管道处理上下文，便于服务器推送数据到客户端
            this.ctx = ctx;
        }
    }

    /**
     * 处理Socket请求
     *
     * @param ctx
     * @param frame
     * @throws Exception
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            logger.info("WebSocket关闭链路");
            logger.info(sessionId + " 注销");
            BananaServiceImpl.logout(sessionId); // 注销
            BananaServiceImpl.notifyDownLine(sessionId); // 通知有人下线
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            logger.info("ping...........");
            return;
        }
        // 当前只支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException("当前只支持文本消息，不支持二进制消息");
        }

        // 处理来自客户端的WebSocket请求
        try {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
            RequestData requestData = RequestData.create(textWebSocketFrame.text());
            logger.info("requestData:{}", requestData);
            ResponseData response = new ResponseData();
            response.setServiceId(requestData.getServiceId());
            // 客户端注册
            if (CodeEnum.ON_LINE.code.equals(requestData.getServiceId())) {
                String requestId = requestData.getRequestId();
                if (Strings.isNullOrEmpty(requestId)) {
                    response.setIsSucc(false).setMessage("requestId不能为空");
                    return;
                } else if (Strings.isNullOrEmpty(requestData.getName())) {
                    response.setIsSucc(false).setMessage("name不能为空");
                    return;
                } else if (BananaServiceImpl.bananaWatchMap.containsKey(requestId)) {
                    response.setIsSucc(false).setMessage("您已经注册了，不能重复注册");
                    return;
                }
                //实例化
                BananaService bananaService = new BananaServiceImpl(ctx, requestData.getName());
                if (!BananaServiceImpl.register(requestId, bananaService)) {
                    response.setIsSucc(false).setMessage("注册失败");
                } else {
                    response.setIsSucc(true).setMessage("注册成功");

                    BananaServiceImpl.bananaWatchMap.forEach((reqId, callBack) -> {
                        response.getHadOnline().put(reqId, ((BananaServiceImpl) callBack).getName()); // 将已经上线的人员返回

                        if (!reqId.equals(requestId)) {
                            RequestData serviceRequest = new RequestData();
                            serviceRequest.setServiceId(CodeEnum.ON_LINE.code);
                            serviceRequest.setRequestId(requestId);
                            serviceRequest.setName(requestData.getName());
                            try {
                                // 通知有人上线
                                callBack.send(serviceRequest);
                            } catch (Exception e) {
                                logger.warn("回调发送消息给客户端异常", e);
                            }
                        }
                    });
                }
                sendWebSocket(response.toJson());
                // 记录会话id，当页面刷新或浏览器关闭时，注销掉此链路
                this.sessionId = requestId;
            } else if (CodeEnum.SEND_MESSAGE.code.equals(requestData.getServiceId())) {
                // 客户端发送消息到聊天群
                String requestId = requestData.getRequestId();
                if (Strings.isNullOrEmpty(requestId)) {
                    response.setIsSucc(false).setMessage("requestId不能为空");
                } else if (Strings.isNullOrEmpty(requestData.getName())) {
                    response.setIsSucc(false).setMessage("name不能为空");
                } else if (Strings.isNullOrEmpty(requestData.getMessage())) {
                    response.setIsSucc(false).setMessage("message不能为空");
                } else {
                    response.setIsSucc(true).setMessage("发送消息成功");
                    // 将消息发送到所有机器
                    BananaServiceImpl.bananaWatchMap.forEach((reqId, callBack) -> {
                        RequestData requestData1 = new RequestData();
                        requestData1.setServiceId(CodeEnum.RECEIVE_MESSAGE.code);
                        requestData1.setRequestId(requestId);
                        requestData1.setName(requestData.getName());
                        requestData1.setMessage(requestData.getMessage());
                        try {
                            logger.info("将消息发送到所有机器,requestData1:{}", requestData1);
                            callBack.send(requestData1);
                        } catch (Exception e) {
                            logger.warn("回调发送消息给客户端异常", e);
                        }
                    });
                }
                sendWebSocket(response.toJson());
            } else if (CodeEnum.DOWN_LINE.code.equals(requestData.getServiceId())) {
                // 客户端下线
                String requestId = requestData.getRequestId();
                if (Strings.isNullOrEmpty(requestId)) {
                    sendWebSocket(response.setIsSucc(false).setMessage("requestId不能为空").toJson());
                } else {
                    BananaServiceImpl.logout(requestId);
                    response.setIsSucc(true).setMessage("下线成功");
                    // 通知有人下线
                    BananaServiceImpl.notifyDownLine(requestId);
                    sendWebSocket(response.toJson());
                }
            } else if (CodeEnum.HEART_BEAT.code.equals(requestData.getServiceId())) {
                sendWebSocket(response.setIsSucc(true).setMessage("心跳检查成功").toJson());
            }else {
                sendWebSocket(response.setIsSucc(false).setMessage("未知请求").toJson());
            }
        } catch (Exception ex) {
            logger.error("处理Socket请求异常", ex);
        }
    }

    /**
     * Http返回
     *
     * @param ctx
     * @param request
     * @param response
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        // 返回应答给客户端
        if (response.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(response, response.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if (!HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * WebSocket返回
     */
    public void sendWebSocket(String msg) throws Exception {
        logger.info("sendWebSocket:{}", msg);
        if (this.handshaker == null || this.ctx == null || this.ctx.isRemoved()) {
            throw new Exception("尚未握手成功，无法向客户端发送WebSocket消息");
        }
        this.ctx.channel().write(new TextWebSocketFrame(msg));
        this.ctx.flush();
    }

}
