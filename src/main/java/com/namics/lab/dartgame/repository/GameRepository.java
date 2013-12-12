package com.namics.lab.dartgame.repository;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;

public interface GameRepository {

	void saveGame(Game game);

	Game getGameForSession(WebSocketSession session);

}
