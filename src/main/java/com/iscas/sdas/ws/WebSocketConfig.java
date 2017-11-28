package com.iscas.sdas.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		//前台 可以使用websocket环境
		registry.addHandler(systemWebSocketHandler(), "/websocket").addInterceptors(new WsHandlerShakeInterceptor());
		
		//前台 不可以使用websocket环境，则使用sockjs进行模拟连接
        registry.addHandler(systemWebSocketHandler(), "/sockjs/websocket").addInterceptors(new WsHandlerShakeInterceptor())
                .withSockJS();
	}

	@Bean
	public WebSocketHandler systemWebSocketHandler() {
		return new WsHandler();
	}
}
