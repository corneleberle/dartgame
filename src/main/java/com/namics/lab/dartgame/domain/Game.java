package com.namics.lab.dartgame.domain;

import org.springframework.web.socket.WebSocketSession;

public class Game {

	private WebSocketSession leftPlayer;

	private WebSocketSession rightPlayer;

	public WebSocketSession getLeftPlayer() {
		return leftPlayer;
	}

	public void setLeftPlayer(WebSocketSession leftPlayer) {
		this.leftPlayer = leftPlayer;
	}

	public WebSocketSession getRightPlayer() {
		return rightPlayer;
	}

	public void setRightPlayer(WebSocketSession rightPlayer) {
		this.rightPlayer = rightPlayer;
	}

}
