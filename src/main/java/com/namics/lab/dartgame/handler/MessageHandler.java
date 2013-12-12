package com.namics.lab.dartgame.handler;

public interface MessageHandler<T> {

	void handle(T message);

}
