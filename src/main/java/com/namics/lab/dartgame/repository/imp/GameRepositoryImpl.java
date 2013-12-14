package com.namics.lab.dartgame.repository.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.repository.GameRepository;

@Repository
public class GameRepositoryImpl implements GameRepository {

	List<Game> games = new ArrayList<Game>();

	@Override
	public void saveGame(Game game) {
		games.add(game);
	}

	@Override
	public Game getGameForSession(WebSocketSession session) {
		for (Game game : games) {
			if (game.getLeftPlayer().equals(session) || game.getRightPlayer().equals(session)) {
				return game;
			}
		}

		return null;
	}

	@Override
	public void removeGame(Game game) {
		if (games != null && games.contains(game)) {
			games.remove(game);
		}
	}

}
