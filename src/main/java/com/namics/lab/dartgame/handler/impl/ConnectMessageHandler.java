package com.namics.lab.dartgame.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.ConnectMessage;
import com.namics.lab.dartgame.message.InitMessage;
import com.namics.lab.dartgame.message.PlayerType;
import com.namics.lab.dartgame.repository.GameRepository;
import com.namics.lab.dartgame.service.LandscapeService;
import com.namics.lab.dartgame.service.MessageService;

public class ConnectMessageHandler implements MessageHandler<ConnectMessage> {

	private GameRepository gameRepository;

	private MessageService messageService;

	private LandscapeService landscapeService;

	private Game pendingGame;

	@Override
	public void handle(WebSocketSession session, ConnectMessage message) {
		if (this.pendingGame == null) {
			this.pendingGame = new Game();
			initGame(pendingGame);
			this.pendingGame.setLeftPlayer(session);
			this.pendingGame.getPlayerNames().put(PlayerType.LEFT, message.getName());
		} else {
			this.pendingGame.setRightPlayer(session);
			this.pendingGame.getPlayerNames().put(PlayerType.RIGHT, message.getName());
			this.gameRepository.saveGame(this.pendingGame);
			sendInitMessage(this.pendingGame);
			this.pendingGame = null;
		}
	}

	private void initGame(Game game) {
		Map<PlayerType, Integer> remainingShots = new HashMap<PlayerType, Integer>();
		remainingShots.put(PlayerType.LEFT, Game.NUMBER_OF_BOMBS);
		remainingShots.put(PlayerType.RIGHT, Game.NUMBER_OF_BOMBS);
		game.setRemainingShots(remainingShots);

		Map<PlayerType, String> playerNames = new HashMap<PlayerType, String>();
		game.setPlayerNames(playerNames);
	}

	private void sendInitMessage(Game game) {
		InitMessage initMessage = new InitMessage();
		initMessage.setRemainingShots(game.getRemainingShots());
		initMessage.setCanonLeftX(Game.CANON_LEFT_X);
		initMessage.setCanonRightX(Game.CANON_RIGHT_X);
		initMessage.setWind(getWind());
		initMessage.setLandscape(landscapeService.getLandscape());
		initMessage.setPlayerNames(game.getPlayerNames());

		initMessage.setPlayerType(PlayerType.LEFT);
		messageService.send(initMessage, game.getLeftPlayer());

		initMessage.setPlayerType(PlayerType.RIGHT);
		messageService.send(initMessage, game.getRightPlayer());
	}

	private int getWind() {
		return new Random().nextInt(Game.WIND_MAX - Game.WIND_MIN + 1) + Game.WIND_MIN;
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

	public LandscapeService getLandscapeService() {
		return landscapeService;
	}

	@Autowired
	public void setLandscapeService(LandscapeService landscapeService) {
		this.landscapeService = landscapeService;
	}

}
