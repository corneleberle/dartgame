package com.namics.lab.dartgame.message;

public class ConnectMessage extends AbstractMessage {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
