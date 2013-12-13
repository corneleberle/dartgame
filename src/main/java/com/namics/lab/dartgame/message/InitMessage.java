package com.namics.lab.dartgame.message;

import java.util.List;

public class InitMessage extends AbstractMessage {

	public InitMessage() {
		super();
		setMessageType(MessageType.INIT);
	}

	private double duration;

	private int numberOfBombs;

	private PlayerType playerType;

	private List<Double> landscape;

	private int canonLeftX;

	private int canonRightX;

	private int wind; // -1000 to 1000

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public int getNumberOfBombs() {
		return numberOfBombs;
	}

	public void setNumberOfBombs(int numberOfBombs) {
		this.numberOfBombs = numberOfBombs;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public List<Double> getLandscape() {
		return landscape;
	}

	public void setLandscape(List<Double> landscape) {
		this.landscape = landscape;
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

}
