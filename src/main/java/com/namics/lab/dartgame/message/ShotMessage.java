package com.namics.lab.dartgame.message;

public class ShotMessage extends AbstractMessage {

	private PlayerType shooter;

	private double angle;

	private double power;

	private int shotId;

	public PlayerType getShooter() {
		return shooter;
	}

	public void setShooter(PlayerType shooter) {
		this.shooter = shooter;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public int getShotId() {
		return shotId;
	}

	public void setShotId(int shotId) {
		this.shotId = shotId;
	}

}
