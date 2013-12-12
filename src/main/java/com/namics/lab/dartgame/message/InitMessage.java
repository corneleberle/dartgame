package com.namics.lab.dartgame.message;

public class InitMessage extends AbstractMessage {

	private double duration;

	private int numberOfBombs;

	private PlayerType playerType;

	private double[] landscape;

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

	public double[] getLandscape() {
		return landscape;
	}

	public void setLandscape(double[] landscape) {
		this.landscape = landscape;
	}

}
