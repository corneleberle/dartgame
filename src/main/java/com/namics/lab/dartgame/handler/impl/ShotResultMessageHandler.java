package com.namics.lab.dartgame.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.ShotResultMessage;
import com.namics.lab.dartgame.message.StatusMessage;
import com.namics.lab.dartgame.message.StatusType;
import com.namics.lab.dartgame.repository.GameRepository;
import com.namics.lab.dartgame.service.GameService;
import com.namics.lab.dartgame.service.MessageService;

public class ShotResultMessageHandler implements MessageHandler<ShotResultMessage> {

	private GameRepository gameRepository;

	private MessageService messageService;

	private GameService gameService;

	@Override
	public void handle(WebSocketSession session, ShotResultMessage message) {
		Game game = this.gameRepository.getGameForSession(session);

		WebSocketSession receiver = this.gameService.getOther(game, session);

		ShotResultMessage shotResultMessage = new ShotResultMessage();
		shotResultMessage.setShotId(1);
		shotResultMessage.setStrike(true);

		this.messageService.send(shotResultMessage, receiver);

		if (message.isStrike()) {
			sendEndGameStatus(receiver, session);
			this.getGameRepository().removeGame(game);
		}
	}

	private void sendEndGameStatus(WebSocketSession winner, WebSocketSession loser) {
		StatusMessage winnerMessage = new StatusMessage();
		winnerMessage.setStatusType(StatusType.WINNER);
		this.messageService.send(winnerMessage, winner);

		StatusMessage loserMessage = new StatusMessage();
		loserMessage.setStatusType(StatusType.LOSER);
		this.messageService.send(loserMessage, loser);
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

	public GameService getGameService() {
		return gameService;
	}

	@Autowired
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

}
