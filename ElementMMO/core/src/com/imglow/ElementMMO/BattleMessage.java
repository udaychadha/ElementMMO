package com.imglow.ElementMMO;

public class BattleMessage extends Message
{
	public String to;
	public String event;
	
	public BattleMessage()
	{
		messageType = BATTLE;
	}
}
