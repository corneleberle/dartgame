package com.namics.lab.dartgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.namics.lab.dartgame.handler.DelegateMessageHandler;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.handler.impl.ConnectMessageHandler;
import com.namics.lab.dartgame.handler.impl.DelegateMessageHandlerImpl;
import com.namics.lab.dartgame.handler.impl.ShotRequestMessageHandlerImpl;
import com.namics.lab.dartgame.handler.impl.ShotResultMessageHandler;
import com.namics.lab.dartgame.message.ConnectMessage;
import com.namics.lab.dartgame.message.ShotRequestMessage;
import com.namics.lab.dartgame.message.ShotResultMessage;
import com.namics.lab.dartgame.socket.DartGameWebSocketHandler;

@Configuration
@ComponentScan(basePackages = "com.namics.lab.dartgame")
@EnableWebMvc
@EnableWebSocket
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(dartGameWebSocketHandler(), "/controller");
	}

	@Bean
	public WebSocketHandler dartGameWebSocketHandler() {
		return new DartGameWebSocketHandler();
	}

	@Bean
	public DelegateMessageHandler delegateMessageHandler() {
		DelegateMessageHandlerImpl delegateMessageHandler = new DelegateMessageHandlerImpl();
		delegateMessageHandler.setConnectMessageHandler(connectMessageHandler());
		delegateMessageHandler.setShotRequestMessageHandler(shotRequestMessageHandler());
		delegateMessageHandler.setShotResultMessageHandler(shotResultMessageHandler());
		return delegateMessageHandler;
	}

	@Bean
	public MessageHandler<ConnectMessage> connectMessageHandler() {
		return new ConnectMessageHandler();
	}

	@Bean
	public MessageHandler<ShotRequestMessage> shotRequestMessageHandler() {
		return new ShotRequestMessageHandlerImpl();
	}

	@Bean
	public MessageHandler<ShotResultMessage> shotResultMessageHandler() {
		return new ShotResultMessageHandler();
	}

	// Allow serving HTML files through the default Servlet

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}