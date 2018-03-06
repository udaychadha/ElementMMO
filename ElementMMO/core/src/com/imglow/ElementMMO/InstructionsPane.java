package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class InstructionsPane implements Drawable
{
	static final BitmapFont INSTRUCTION_FONT = TextureSingleton.getInstance().nameFont;
		
	public InstructionsPane()
	{
		
	}

	@Override
	public void draw(SpriteBatch sb) 
	{
		Button closeThisIsh = new Button(TextureSingleton.getInstance().nothing, -MainClient.WIDTH/2, -MainClient.HEIGHT/2, MainClient.WIDTH, MainClient.HEIGHT, 
		new OnClickListener()
		{

			@Override
			public void onClick(Button source, Vector3 pos) 
			{
				if((pos.x < -250 || pos.x > 150) || (pos.y < 100 || pos.y > 300))
				{
					close();
				}
			}

		});
		
		sb.draw(TextureSingleton.getInstance().msgBox, -275, 0, 550, 250);
		sb.setColor(Color.WHITE);
		INSTRUCTION_FONT.setColor(Color.BLACK);
		INSTRUCTION_FONT.setScale(1f);
		INSTRUCTION_FONT.draw(sb, "Welcome to our game!", -240, 230);
		INSTRUCTION_FONT.draw(sb, "Here's how to play:", -240, 210);
		INSTRUCTION_FONT.draw(sb, "- Run around and explore the world! (WASD keys)", -210, 190);
		INSTRUCTION_FONT.draw(sb, "- Find people on the other team and battle!", -210, 170);
		INSTRUCTION_FONT.draw(sb, "- Buy battle elements at the stores!", -210, 150);
		INSTRUCTION_FONT.draw(sb, "- Talk! Press enter to open chat, and enter to send", -210, 130);
		INSTRUCTION_FONT.draw(sb, "- Use \"/all [msg]\" or \"/msg user1, user2/ [msg]\"", -210, 110);
		INSTRUCTION_FONT.draw(sb, "- Made by: Josh Bollar, Ian Glow,", -210, 90);
		INSTRUCTION_FONT.draw(sb, "           Joel Grattan, Uday Chadha, Liam Duffy", -210, 70);
		INSTRUCTION_FONT.draw(sb, "Close this by clicking anywhere outside the window!", -210, 40);
	}
	
	public void close()
	{
		Game.getInstance().instructions = null;
	}
}
