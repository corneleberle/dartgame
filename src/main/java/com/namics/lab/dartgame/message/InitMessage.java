package com.namics.lab.dartgame.message;

import java.util.List;
import java.util.Map;

public class InitMessage extends AbstractMessage {

	public InitMessage() {
		super();
		setMessageType(MessageType.INIT);
	}

	private Map<PlayerType, Integer> remainingShots;

	private Map<PlayerType, String> playerNames;

	private PlayerType playerType;

	private int canonLeftX;

	private int canonRightX;

	private int wind; // -1000 to 1000

	private List<Double> landscape;

	public Map<PlayerType, Integer> getRemainingShots() {
		return remainingShots;
	}

	public void setRemainingShots(Map<PlayerType, Integer> remainingShots) {
		this.remainingShots = remainingShots;
	}

	public Map<PlayerType, String> getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(Map<PlayerType, String> playerNames) {
		this.playerNames = playerNames;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public int getCanonLeftX() {
		return canonLeftX;
	}

	public void setCanonLeftX(int canonLeftX) {
		this.canonLeftX = canonLeftX;
	}

	public int getCanonRightX() {
		return canonRightX;
	}

	public void setCanonRightX(int canonRightX) {
		this.canonRightX = canonRightX;
	}

	public int getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public List<Double> getLandscape() {
		return landscape;
	}

	public void setLandscape(List<Double> landscape) {
		this.landscape = landscape;
	}

}
