package com.namics.lab.dartgame.message;

import java.util.Date;

public abstract class AbstractMessage {

	private Date sent;

	private String sender;

	private MessageType messageType;

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

}
