package com.namics.lab.dartgame.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.service.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Override
	public WebSocketSession getOther(Game game, WebSocketSession session) {
		if (game.getLeftPlayer().equals(session)) {
			return game.getRightPlayer();
		} else {
			return game.getLeftPlayer();
		}
	}

}
