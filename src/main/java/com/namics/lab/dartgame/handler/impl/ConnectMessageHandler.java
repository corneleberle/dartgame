package com.namics.lab.dartgame.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.ConnectMessage;
import com.namics.lab.dartgame.repository.GameRepository;

public class ConnectMessageHandler implements MessageHandler<ConnectMessage> {

	private GameRepository gameRepository;

	private Game pendingGame;

	@Override
	public void handle(WebSocketSession session, ConnectMessage message) {
		if (this.pendingGame == null) {
			this.pendingGame = new Game();
			this.pendingGame.setLeftPlayer(session);
		} else {
			this.pendingGame.setRightPlayer(session);
			this.gameRepository.saveGame(this.pendingGame);
			this.pendingGame = null;
		}
	}

	public GameRepository getGameRepository() {
		return gameRepository;
	}

	@Autowired
	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Game getPendingGame() {
		return pendingGame;
	}

	public void setPendingGame(Game pendingGame) {
		this.pendingGame = pendingGame;
	}
}
