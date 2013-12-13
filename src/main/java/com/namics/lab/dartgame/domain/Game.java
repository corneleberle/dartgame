package com.namics.lab.dartgame.domain;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.message.PlayerType;
import com.namics.lab.dartgame.service.impl.LandscapeServiceImpl;

public class Game {

	public final static int NUMBER_OF_BOMBS = 10;
	public final static double DURATION = 60;
	public final static int CANON_LEFT_X = LandscapeServiceImpl.LANDSCAPE_RESOLUTION / LandscapeServiceImpl.PARTS;
	public final static int CANON_RIGHT_X = LandscapeServiceImpl.LANDSCAPE_RESOLUTION / LandscapeServiceImpl.PARTS * (LandscapeServiceImpl.PARTS - 1);

	public final static int WIND_MIN = -1000;
	public final static int WIND_MAX = 1000;

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
