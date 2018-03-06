package com.imglow.ElementMMO;

public class MovmentMessage extends Message{

	public float x,y;
	public int direction;
	public boolean moving, invincible;
	
	public MovmentMessage()
	{
		messageType = MOVEMENT;
	}
}
