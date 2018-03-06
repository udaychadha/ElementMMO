package com.imglow.ElementMMO;

import java.util.Vector;

public class StatusMessage extends Message {
	public Vector<MovmentMessage> playerPosition;
	public int redScore, blueScore;
	
	public StatusMessage()
	{
		messageType = STATUS;
	}
}
