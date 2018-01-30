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
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(message.getPayload().toString());
		//System.out.println("开始发送数据");
		int progress = 0;	
		while (true) {
			progress = Constraints.getFtp_upload_progress();
			TextMessage msg = new TextMessage(String.valueOf(progress));
			session.sendMessage(msg);
			if (Constraints.getFtp_upload_progress()==100) {
				Constraints.setFtp_upload_progress(0);
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
