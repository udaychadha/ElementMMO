package com.imglow.ElementMMO;

public class ResetMessage extends Message 
{
	public boolean team1win = true;
	
	public ResetMessage()
	{
		messageType = RESET;
	}
}

