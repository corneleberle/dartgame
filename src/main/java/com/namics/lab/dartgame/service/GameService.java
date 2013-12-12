package com.namics.lab.dartgame.service;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;

public interface GameService {

	WebSocketSession getOther(Game game, WebSocketSession session);

}
