package com.namics.lab.dartgame.domain;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.message.PlayerType;

public class Game {

	public final static int NUMBER_OF_BOMBS = 10;

	public final static double DURATION = 60;

	public static final int LANDSCAPE_RESOLUTION = 1000;

	private WebSocketSession leftPlayer;

	private WebSocketSession rightPlayer;

	private Map<PlayerType, Integer> remainingShots;

	private int shotLimit;

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

	public Map<PlayerType, Integer> getRemainingShots() {
		return remainingShots;
	}

	public void setRemainingShots(Map<PlayerType, Integer> remainingShots) {
		this.remainingShots = remainingShots;
	}

	public int getShotLimit() {
		return shotLimit;
	}

	public void setShotLimit(int shotLimit) {
		this.shotLimit = shotLimit;
	}

}
