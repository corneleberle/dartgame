package com.namics.lab.dartgame.message;

public class ShotResultMessage extends AbstractMessage {

	private int shotId;

	private boolean strike;

	public int getShotId() {
		return shotId;
	}

	public void setShotId(int shotId) {
		this.shotId = shotId;
	}

	public boolean isStrike() {
		return strike;
	}

	public void setStrike(boolean strike) {
		this.strike = strike;
	}

}
