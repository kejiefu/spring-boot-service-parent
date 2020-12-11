package com.mountain.project.websocket.core.common;

/**
 * 统一消息码
 *
 */
public enum CodeEnum {

	/**
	 * 成功
	 */
	SUCCESS(200, "成功"),

	PARAMETERS_INCORRECT(400, "参数不正确"),
	PARAMETERS_INVALID(401, "特定参数不符合条件(EG:没有这个用户)"),
	SERVICE_NOTFOUND(402, "没有这个服务"),
	NODE_UNAVAILABLE(403, "没有可用的服务节点"),

	ERROR(500, "执行错误"),
	AUTHENTICATION_FAIL(501, "认证失败"),
	ROLES_FAIL(502, "授权失败"),
	SESSION_EXPIRATION(503, "SESSION 过期"),
	SESSION_LOSE(504, "SESSION 丢失"),

	TIMEOUT(510, "调用超时"),
	GENERATE_RETURN_ERROR(511, "处理返回值错误"),
	LIMIT(512, "接口调用次数超过限制"),
	LIMIT_BY_GROUP(513, "用户调用次数超过限制"),

	ON_LINE(1001, "客户端上线请求"),
	SEND_MESSAGE(1002, "客户端发送'发送消息'请求"),
	RECEIVE_MESSAGE(1003, "服务端发送'接收消息'请求"),
	DOWN_LINE(1004, "客户端下线请求"),
	HEART_BEAT(1005, "心跳检查");

	public String note;
	public Integer code;

	private CodeEnum(Integer code, String note) {
		this.note = note;
		this.code = code;
	}
}
