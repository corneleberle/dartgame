package com.namics.lab.dartgame.chat.message;

public enum MessageType {

	CONNECT, // C -> S
	INIT, // S -> C
	SHOT_REQUEST, // C -> S
	SHOT, // S -> C
	SHOT_RESULT, // C -> S
	STATUS // S -> C

}
