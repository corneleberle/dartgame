package com.namics.lab.dartgame.handler;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.message.AbstractMessage;

public interface DelegateMessageHandler {
	void delegate(WebSocketSession session, AbstractMessage message);
}
