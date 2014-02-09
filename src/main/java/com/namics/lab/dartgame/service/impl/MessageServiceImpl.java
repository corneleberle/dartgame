package com.namics.lab.dartgame.service.impl;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namics.lab.dartgame.message.AbstractMessage;
import com.namics.lab.dartgame.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Override
	public void send(AbstractMessage message, WebSocketSession... sessions) {
		message.setSent(new Date());

		ObjectMapper mapper = new ObjectMapper();
		String payload;
		try {
			payload = mapper.writer().writeValueAsString(message);
			TextMessage textMessage = new TextMessage(payload);

			for (WebSocketSession session : sessions) {
				session.sendMessage(textMessage);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
