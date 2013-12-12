package com.namics.lab.dartgame.service;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.message.AbstractMessage;

public interface MessageService {

	void send(AbstractMessage message, WebSocketSession... sessions);

}
