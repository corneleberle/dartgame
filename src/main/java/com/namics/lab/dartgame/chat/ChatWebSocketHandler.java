package com.namics.lab.dartgame.chat;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namics.lab.dartgame.handler.DelegateMessageHandler;
import com.namics.lab.dartgame.message.AbstractMessage;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private DelegateMessageHandler delegateMessageHandler;

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
		ObjectMapper mapper = new ObjectMapper();
		// Message msg = mapper.readValue(message.getPayload(), Message.class);
		AbstractMessage connectMessage = mapper.readValue(
				"{\"messageType\":\"CONNECT\",\"sender\":\"tester\",\"name\":\"test\",\"sent\":\"2011-04-08T09:00:00\"}", AbstractMessage.class);
		AbstractMessage shotMessage = mapper.readValue("{\"messageType\":\"SHOT_REQUEST\",\"sender\":\"tester\",\"sent\":\"2011-04-08T09:00:00\"}",
				AbstractMessage.class);

		AbstractMessage shotResult = mapper.readValue("{\"messageType\":\"SHOT_RESULT\",\"sender\":\"tester\",\"sent\":\"2011-04-08T09:00:00\"}",
				AbstractMessage.class);

		delegateMessageHandler.delegate(session, connectMessage);
		delegateMessageHandler.delegate(session, connectMessage);
		delegateMessageHandler.delegate(session, shotMessage);
		delegateMessageHandler.delegate(session, shotResult);
	}

	public DelegateMessageHandler getDelegateMessageHandler() {
		return delegateMessageHandler;
	}

	@Autowired
	public void setDelegateMessageHandler(DelegateMessageHandler delegateMessageHandler) {
		this.delegateMessageHandler = delegateMessageHandler;
	}

}