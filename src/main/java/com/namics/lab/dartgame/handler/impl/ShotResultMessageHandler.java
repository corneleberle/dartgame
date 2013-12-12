package com.namics.lab.dartgame.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.ShotResultMessage;
import com.namics.lab.dartgame.repository.GameRepository;
import com.namics.lab.dartgame.service.MessageService;

public class ShotResultMessageHandler implements MessageHandler<ShotResultMessage> {

	private GameRepository gameRepository;

	private MessageService messageService;

	@Override
	public void handle(WebSocketSession session, ShotResultMessage message) {
		Game game = this.gameRepository.getGameForSession(session);

		WebSocketSession receiver = null;
		if (game.getLeftPlayer().equals(session)) {
			receiver = game.getRightPlayer();
		} else {
			receiver = game.getLeftPlayer();
		}

		ShotResultMessage shotResultMessage = new ShotResultMessage();
		shotResultMessage.setShotId(1);
		shotResultMessage.setStrike(true);

		this.messageService.send(shotResultMessage, receiver);
	}

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	@Autowired
	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}
