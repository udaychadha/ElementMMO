package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OtherPlayer extends Player{

	float startX, startY;
	
	@Override
	public void draw(SpriteBatch sb) {
		updateSprite();

		if(team1)
			sb.setColor(0.5f,0.5f,1f,1f);
		else
			sb.setColor(1f,0.5f,0.5f,1f);
		
		sb.draw(spr, (x - 0.5f - Game.getInstance().dX) * Cell.LENGTH, (y - 0.5f - Game.getInstance().dY) * Cell.LENGTH, WIDTH, HEIGHT); 	
		
		sb.setColor(Color.WHITE);
	}

	protected void updateSprite() {
		TextureSingleton ts = TextureSingleton.getInstance();
		time++;
		time %= 14;

		if(time < 7)
			frame1 = true;
		else
			frame1 = false;
		
		if(moving && (Math.abs(startX - x) > 3 || Math.abs(startY - y) > 3))
		{
			moving = false;
		}

		if(moving)
		{
			if(moveDirection == LEFT) //moving left
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_LEFT_1);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_LEFT_2);

				lw = LEFT;
				x -= SPEED;
			}
			else if(moveDirection == RIGHT) //moving right
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_RIGHT_1);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_RIGHT_2);

				lw = RIGHT;
				x += SPEED;
			}
			else if(moveDirection == DOWN) //moving down
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_DOWN);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.STAND);

				lw = DOWN;
				y -= SPEED;
			}
			else if(moveDirection == UP) //moving up
			{
				if(frame1)
					spr = ts.playerSprites.get(type).get(TextureSingleton.WALK_UP);
				else
					spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_UP);

				lw = UP;
				y += SPEED;
			}
		}
		else
		{
			if(moveDirection == UP)
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_UP);
			else if(moveDirection == DOWN)
				spr = ts.playerSprites.get(type).get(TextureSingleton.STAND);
			else if(moveDirection == RIGHT)
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_RIGHT);
			else
				spr = ts.playerSprites.get(type).get(TextureSingleton.FACE_LEFT);
		}

		px = x;
		py = y;
	}

}
