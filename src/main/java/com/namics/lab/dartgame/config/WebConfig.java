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