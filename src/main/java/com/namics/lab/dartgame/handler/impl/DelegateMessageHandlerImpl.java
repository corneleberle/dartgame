package com.namics.lab.dartgame.handler.impl;

import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.handler.DelegateMessageHandler;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.AbstractMessage;
import com.namics.lab.dartgame.message.ConnectMessage;
import com.namics.lab.dartgame.message.InitMessage;
import com.namics.lab.dartgame.message.MessageType;
import com.namics.lab.dartgame.message.ShotMessage;
import com.namics.lab.dartgame.message.ShotRequestMessage;
import com.namics.lab.dartgame.message.ShotResultMessage;
import com.namics.lab.dartgame.message.StatusMessage;

public class DelegateMessageHandlerImpl implements DelegateMessageHandler {

	private MessageHandler<ConnectMessage> connectMessageHandler;

	private MessageHandler<InitMessage> initMessageHandler;

	private MessageHandler<ShotRequestMessage> shotRequestMessageHandler;

	private MessageHandler<ShotMessage> shotMessageHandler;

	private MessageHandler<ShotResultMessage> shotResultMessageHandler;

	private MessageHandler<StatusMessage> statusMessageHandler;

	@Override
	public void delegate(WebSocketSession session, AbstractMessage message) {
		if (MessageType.CONNECT.equals(message.getMessageType())) {
			connectMessageHandler.handle(session, (ConnectMessage) message);
		} else if (MessageType.INIT.equals(message.getMessageType())) {
			initMessageHandler.handle(session, (InitMessage) message);
		} else if (MessageType.SHOT_REQUEST.equals(message.getMessageType())) {
			shotRequestMessageHandler.handle(session, (ShotRequestMessage) message);
		} else if (MessageType.SHOT.equals(message.getMessageType())) {
			shotMessageHandler.handle(session, (ShotMessage) message);
		} else if (MessageType.SHOT_RESULT.equals(message.getMessageType())) {
			shotResultMessageHandler.handle(session, (ShotResultMessage) message);
		} else if (MessageType.STATUS.equals(message.getMessageType())) {
			statusMessageHandler.handle(session, (StatusMessage) message);
		}
	}

	public MessageHandler<ConnectMessage> getConnectMessageHandler() {
		return connectMessageHandler;
	}

	public void setConnectMessageHandler(MessageHandler<ConnectMessage> connectMessageHandler) {
		this.connectMessageHandler = connectMessageHandler;
	}

	public MessageHandler<InitMessage> getInitMessageHandler() {
		return initMessageHandler;
	}

	public void setInitMessageHandler(MessageHandler<InitMessage> initMessageHandler) {
		this.initMessageHandler = initMessageHandler;
	}

	public MessageHandler<ShotRequestMessage> getShotRequestMessageHandler() {
		return shotRequestMessageHandler;
	}

	public void setShotRequestMessageHandler(MessageHandler<ShotRequestMessage> shotRequestMessageHandler) {
		this.shotRequestMessageHandler = shotRequestMessageHandler;
	}

	public MessageHandler<ShotMessage> getShotMessageHandler() {
		return shotMessageHandler;
	}

	public void setShotMessageHandler(MessageHandler<ShotMessage> shotMessageHandler) {
		this.shotMessageHandler = shotMessageHandler;
	}

	public MessageHandler<ShotResultMessage> getShotResultMessageHandler() {
		return shotResultMessageHandler;
	}

	public void setShotResultMessageHandler(MessageHandler<ShotResultMessage> shotResultMessageHandler) {
		this.shotResultMessageHandler = shotResultMessageHandler;
	}

	public MessageHandler<StatusMessage> getStatusMessageHandler() {
		return statusMessageHandler;
	}

	public void setStatusMessageHandler(MessageHandler<StatusMessage> statusMessageHandler) {
		this.statusMessageHandler = statusMessageHandler;
	}

}
