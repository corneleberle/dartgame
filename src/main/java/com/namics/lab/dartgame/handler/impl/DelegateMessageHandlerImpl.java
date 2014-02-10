package com.namics.lab.dartgame.handler.impl;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.handler.DelegateMessageHandler;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.AbstractMessage;
import com.namics.lab.dartgame.message.ConnectMessage;
import com.namics.lab.dartgame.message.MessageType;
import com.namics.lab.dartgame.message.ShotRequestMessage;
import com.namics.lab.dartgame.message.ShotResultMessage;

public class DelegateMessageHandlerImpl implements DelegateMessageHandler {

	private MessageHandler<ConnectMessage> connectMessageHandler;

	private MessageHandler<ShotRequestMessage> shotRequestMessageHandler;

	private MessageHandler<ShotResultMessage> shotResultMessageHandler;

	@Override
	public void delegate(WebSocketSession session, AbstractMessage message) {
		if (MessageType.CONNECT.equals(message.getMessageType())) {
			connectMessageHandler.handle(session, (ConnectMessage) message);
		} else if (MessageType.SHOT_REQUEST.equals(message.getMessageType())) {
			shotRequestMessageHandler.handle(session, (ShotRequestMessage) message);
		} else if (MessageType.SHOT_RESULT.equals(message.getMessageType())) {
			shotResultMessageHandler.handle(session, (ShotResultMessage) message);
		} else {
			System.out.println("######## unknown message type");
		}
	}

	public MessageHandler<ConnectMessage> getConnectMessageHandler() {
		return connectMessageHandler;
	}

	public void setConnectMessageHandler(MessageHandler<ConnectMessage> connectMessageHandler) {
		this.connectMessageHandler = connectMessageHandler;
	}

	public MessageHandler<ShotRequestMessage> getShotRequestMessageHandler() {
		return shotRequestMessageHandler;
	}

	public void setShotRequestMessageHandler(MessageHandler<ShotRequestMessage> shotRequestMessageHandler) {
		this.shotRequestMessageHandler = shotRequestMessageHandler;
	}

	public MessageHandler<ShotResultMessage> getShotResultMessageHandler() {
		return shotResultMessageHandler;
	}

	public void setShotResultMessageHandler(MessageHandler<ShotResultMessage> shotResultMessageHandler) {
		this.shotResultMessageHandler = shotResultMessageHandler;
	}

}
