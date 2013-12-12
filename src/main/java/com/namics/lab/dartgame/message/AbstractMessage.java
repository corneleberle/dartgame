package com.namics.lab.dartgame.message;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "messageType", visible = true)
@JsonSubTypes({ @Type(value = ConnectMessage.class, name = "CONNECT"), @Type(value = InitMessage.class, name = "INIT"),
		@Type(value = ShotRequestMessage.class, name = "SHOT_REQUEST"), @Type(value = ShotMessage.class, name = "SHOT"),
		@Type(value = ShotResultMessage.class, name = "SHOT_RESULT"), @Type(value = StatusMessage.class, name = "STATUS") })
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
