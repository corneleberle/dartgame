package com.namics.lab.dartgame.handler;

import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler<T> {

	void handle(WebSocketSession session, T message);

}
