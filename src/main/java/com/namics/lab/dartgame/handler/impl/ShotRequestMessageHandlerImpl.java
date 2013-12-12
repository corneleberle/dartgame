package com.namics.lab.dartgame.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import com.namics.lab.dartgame.domain.Game;
import com.namics.lab.dartgame.handler.MessageHandler;
import com.namics.lab.dartgame.message.MessageType;
import com.namics.lab.dartgame.message.PlayerType;
import com.namics.lab.dartgame.message.ShotMessage;
import com.namics.lab.dartgame.message.ShotRequestMessage;
import com.namics.lab.dartgame.repository.GameRepository;
import com.namics.lab.dartgame.service.MessageService;

public class ShotRequestMessageHandlerImpl implements MessageHandler<ShotRequestMessage> {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private MessageService messageService;

	@Override
	public void handle(WebSocketSession session, ShotRequestMessage message) {
		Game game = gameRepository.getGameForSession(session);

		PlayerType shooter = null;
		if (game.getLeftPlayer().equals(session)) {
			shooter = PlayerType.LEFT;
		} else {
			shooter = PlayerType.RIGHT;
		}

		sendShotMessage(game, message, shooter, 1);
	}

	private void sendShotMessage(Game game, ShotRequestMessage message, PlayerType shooter, int shotId) {
		ShotMessage shotMessage = new ShotMessage();
		shotMessage.setMessageType(MessageType.SHOT);

		shotMessage.setShooter(shooter);
		shotMessage.setAngle(message.getAngle());
		shotMessage.setPower(message.getPower());
		shotMessage.setShotId(shotId);

		messageService.send(shotMessage, game.getLeftPlayer(), game.getRightPlayer());
	}

}