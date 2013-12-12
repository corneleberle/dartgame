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
import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.handler.DelegateMessageHandler;
import com.namics.lab.dartgame.message.AbstractMessage;
import com.namics.lab.dartgame.message.MessageType;
import com.namics.lab.dartgame.message.StatusMessage;
import com.namics.lab.dartgame.message.StatusType;
import com.namics.lab.dartgame.repository.GameRepository;
import com.namics.lab.dartgame.service.GameService;
import com.namics.lab.dartgame.service.MessageService;

/**
 * Echo messages by implementing a Spring {@link WebSocketHandler} abstraction.
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private DelegateMessageHandler delegateMessageHandler;

	Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();

	private GameRepository gameRepository;

	private GameService gameService;

	private MessageService messageService;

	@Autowired
	public ChatWebSocketHandler() {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Game game = gameRepository.getGameForSession(session);
		if (game != null) {
			WebSocketSession other = gameService.getOther(game, session);
			if (other != null) {
				// Remove game
				gameRepository.removeGame(game);

				// Send status
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setMessageType(MessageType.STATUS);
				statusMessage.setStatusType(StatusType.PLAYER_LEFT);
				messageService.send(statusMessage, other);
			}
		}
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

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	@Autowired
	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public GameService getGameService() {
		return gameService;
	}

	@Autowired
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}