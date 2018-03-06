package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cell implements Drawable {
	
	public static int GRASS = 0, LENGTH = 64;
	public static int TREE = 1;
	public static int BUSH = 2;
	public static int STORE = 3;
	int x, y, type;
	TextureRegion spr;
	
	public Cell(int x, int y, int type)
	{
		this.x = x;
		this.y = y;
		this.type = type;
		if(type == GRASS)
		{
			this.spr = TextureSingleton.getInstance().grassArray.get((int)(TextureSingleton.getInstance().grassArray.size() * Math.random()));
		}
		else if(type == TREE)
		{
			int rand = (int)(Math.random()*2);
			if(rand == 0)
				this.spr = TextureSingleton.getInstance().tree1;
			else
				this.spr = TextureSingleton.getInstance().tree2;
		}
		else if(type == BUSH)
		{
			this.spr = TextureSingleton.getInstance().bush;
		}
		else if(type == STORE)
		{
			this.spr = TextureSingleton.getInstance().storeArray.get((int)(TextureSingleton.getInstance().storeArray.size() * Math.random()));
		}
	}

	@Override
	public void draw(SpriteBatch sb)
	{
		float dx = Game.getInstance().dX,
				dy = Game.getInstance().dY;
		
		float tempx = (x - dx) * Cell.LENGTH - 0.5f * Cell.LENGTH,
				tempy = (y - dy) * Cell.LENGTH - 0.5f * Cell.LENGTH;
		
		
		if(tempx > MainClient.WIDTH/2 || tempy > MainClient.HEIGHT /2 ||
				tempx + LENGTH < -MainClient.WIDTH/2 || tempy + LENGTH < -MainClient.HEIGHT/2)
			return;
		
		if(y <= Game.HEIGHT / 4)
		{
			sb.setColor(0.7f,0.7f,1.0f,1.0f);
		}
		
		if(y >= 3*Game.HEIGHT / 4)
		{
			sb.setColor(1.0f,0.7f,0.7f,1.0f);
		}
		
		// always draw the grass underneath
		if(type != GRASS && type != STORE)
		{
			sb.draw(TextureSingleton.getInstance().grass,
					tempx,
					tempy,
					LENGTH,
					LENGTH);
			// draw grasses smushy smush
		}
		// draw the store bottom underneath all the store creations
		if(type == STORE)
		{
			sb.draw(TextureSingleton.getInstance().storeEmpty,
					tempx,
					tempy,
					LENGTH,
					LENGTH);
		}
		sb.draw(spr,
				tempx,
				tempy,
				LENGTH,
				LENGTH); 
		
		sb.setColor(Color.WHITE);
	}

}
