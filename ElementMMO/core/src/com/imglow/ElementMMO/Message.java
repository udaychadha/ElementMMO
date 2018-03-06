package com.imglow.ElementMMO;

import java.io.Serializable;

public class Message implements Serializable {
	
	public static final int BLANK = 0,
			STATUS = 1,
			MOVEMENT = 2,
			BATTLE = 3,
			TEXT = 4,
			RESET = 5,
			EVENT = 6;
	
	public String from;
	public boolean team1;
	public int type;
	public int messageType = BLANK;
}
