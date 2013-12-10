package com.namics.lab.dartgame.chat;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

	Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();

	@Autowired
	public ChatWebSocketHandler() {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		for (WebSocketSession otherSession : sessions) {
			if (!otherSession.equals(session)) {
				String echoMessage = message.getPayload();
				otherSession.sendMessage(new TextMessage(echoMessage));
			}
		}
	}

}