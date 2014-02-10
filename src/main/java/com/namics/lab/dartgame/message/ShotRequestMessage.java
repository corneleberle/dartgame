package com.namics.lab.dartgame.message;

public class ShotRequestMessage extends AbstractMessage {

	private double angle;

	private double power;

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

}
