package com.iscas.sdas.ws;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
/**
 * websocket注册类
 * @author dongqun
 * 2017年11月27日下午3:53:28
 */
public class WsHandlerShakeInterceptor extends HttpSessionHandshakeInterceptor {
	private static Logger logger = Logger.getLogger(WsHandlerShakeInterceptor.class); 
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		// TODO Auto-generated method stub
		logger.info("afterHandshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2,
			Map<String, Object> arg3) throws Exception {
		// TODO Auto-generated method stub
		logger.info("beforeHandshake");
		return super.beforeHandshake(arg0, arg1, arg2, arg3);
	}
	
}
