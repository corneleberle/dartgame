package com.namics.lab.dartgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.namics.lab.dartgame.chat.ChatWebSocketHandler;
import com.namics.lab.dartgame.echo.DefaultEchoService;
import com.namics.lab.dartgame.echo.EchoWebSocketHandler;
import com.namics.lab.dartgame.handler.DelegateMessageHandler;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.handler.impl.DelegateMessageHandlerImpl;
import com.namics.lab.dartgame.handler.impl.ShotRequestMessageHandlerImpl;
import com.namics.lab.dartgame.message.ConnectMessage;
import com.namics.lab.dartgame.message.InitMessage;
import com.namics.lab.dartgame.message.ShotMessage;
import com.namics.lab.dartgame.message.ShotRequestMessage;
import com.namics.lab.dartgame.message.ShotResultMessage;
import com.namics.lab.dartgame.message.StatusMessage;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(echoWebSocketHandler(), "/echo");
		// registry.addHandler(snakeWebSocketHandler(), "/snake");

		registry.addHandler(chatWebSocketHandler(), "/chat");

		registry.addHandler(echoWebSocketHandler(), "/sockjs/echo").withSockJS();
		// registry.addHandler(snakeWebSocketHandler(), "/sockjs/snake").withSockJS();
	}

	@Bean
	public WebSocketHandler echoWebSocketHandler() {
		return new EchoWebSocketHandler(echoService());
	}

	@Bean
	public WebSocketHandler chatWebSocketHandler() {
		return new ChatWebSocketHandler();
	}

	@Bean
	public DelegateMessageHandler delegateMessageHandler() {
		DelegateMessageHandlerImpl delegateMessageHandler = new DelegateMessageHandlerImpl();
		delegateMessageHandler.setConnectMessageHandler(connectMessageHandler());
		delegateMessageHandler.setInitMessageHandler(initMessageHandler());
		delegateMessageHandler.setShotRequestMessageHandler(shotRequestMessageHandler());
		delegateMessageHandler.setShotMessageHandler(shotMessageHandler());
		delegateMessageHandler.setShotResultMessageHandler(shotResultMessageHandler());
		delegateMessageHandler.setStatusMessageHandler(statusMessageHandler());
		return delegateMessageHandler;
	}

	@Bean
	public MessageHandler<ConnectMessage> connectMessageHandler() {
		// TODO implement
		return null;
	}

	@Bean
	public MessageHandler<InitMessage> initMessageHandler() {
		// TODO implement
		return null;
	}

	@Bean
	public MessageHandler<ShotRequestMessage> shotRequestMessageHandler() {
		return new ShotRequestMessageHandlerImpl();
	}

	@Bean
	public MessageHandler<ShotMessage> shotMessageHandler() {
		// TODO implement
		return null;
	}

	@Bean
	public MessageHandler<ShotResultMessage> shotResultMessageHandler() {
		// TODO implement
		return null;
	}

	@Bean
	public MessageHandler<StatusMessage> statusMessageHandler() {
		// TODO implement
		return null;
	}

	// @Bean
	// public WebSocketHandler snakeWebSocketHandler() {
	// return new PerConnectionWebSocketHandler(SnakeWebSocketHandler.class);
	// }

	@Bean
	public DefaultEchoService echoService() {
		return new DefaultEchoService("Did you say \"%s\"?");
	}

	// Allow serving HTML files through the default Servlet

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}