package com.iscas.sdas.ws;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.iscas.sdas.util.Constraints;

public class WsHandler implements WebSocketHandler {
	
	private static Logger logger = Logger.getLogger(WsHandler.class);
	
	private int progress;

	@Override
	public void afterConnectionClosed(WebSocketSession arg0, CloseStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		logger.info("连接已关闭！");
		System.out.println("连接已关闭！");
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession arg0) throws Exception {
		// TODO Auto-generated method stub
		logger.info("连接已建立！");
	}

	@Override
	public void handleMessage(WebSocketSession arg0, WebSocketMessage<?> arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(arg1.getPayload().toString());
		System.out.println("开始发送数据");
		while (true) {
			if (progress!=Constraints.getFtp_upload_progress()) {
				progress = Constraints.getFtp_upload_progress();
				TextMessage msg = new TextMessage(String.valueOf(progress));
				arg0.sendMessage(msg);
			}
			if (Constraints.getFtp_upload_progress()==100) {
				progress=0;
				break;
			}			
		}

	}

	@Override
	public void handleTransportError(WebSocketSession arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		logger.info("连接异常！");
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	} 
	


	
}
