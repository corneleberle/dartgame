package com.namics.lab.dartgame.message;

public class StatusMessage extends AbstractMessage {

	private StatusType statusType;

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

}
