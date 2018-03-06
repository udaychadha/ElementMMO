package com.imglow.ElementMMO;

public class EventMessage extends Message{
	public int x, y;
	public String event;
	public String result;
	public String to;
	
	public EventMessage()
	{
		messageType = EVENT;
	}
}
