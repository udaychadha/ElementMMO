package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Player implements Drawable{
	
	public static int LEFT = 0, 
			UP = 1, 
			RIGHT = 2, 
			DOWN = 3, 
			NOT_MOVING = 4, 
			WIDTH = 64,
			HEIGHT = 84;
	
	public static float SPEED = 0.1f;
	public boolean moving = false;
	
	
	String name;
	
	float y = 1;
	float x, px, py, time = 0;
	
	int moveDirection = NOT_MOVING;
	
	int type, 
		lw = DOWN, 
		health = 6;
	
	boolean frame1 = true, team1 = true, invincible = false;
	
	TextureRegion spr;

	protected void updateSprite() {
		TextureSingleton ts = TextureSingleton.getInstance();
		time++;
		time %= 14;
		
		if(time < 7)
			frame1 = true;
		else
			frame1 = false;
			
		if(px > x) //moving left
		{
			if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_LEFT_1);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_LEFT_2);
			
			lw = LEFT;
		}
		else if(px < x) //moving right
		{
			if(frame1)
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_RIGHT_1);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_RIGHT_2);
			
			lw = RIGHT;
		}
		else if(py > y) //moving down
		{
			if(frame1)
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_DOWN);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_DOWN_2);
			
			lw = DOWN;
		}
		else if(py < y) //moving up
		{
			if(frame1)
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_UP);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_UP_2);
			
			lw = UP;
		}
		else
		{
			if(lw == UP)
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_UP);
			else if(lw == DOWN)
				spr = ts.playerSprites.get(type).get(TextureSingleton.STAND);
			else if(lw == RIGHT)
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_RIGHT);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_LEFT);
		}
		
		px = x;
		py = y;
	}

}
