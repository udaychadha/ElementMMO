package com.imglow.ElementMMO;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.*;

public class Store implements Drawable, ClickListener
{
	// the coordinates of where the topleft of the thing is
	float x, y;
	static final float buttonWidth = 100;
	static final float buttonHeight = 30;
	static final float storeWidth = buttonWidth*3 + 10*5;
	static final float storeHeight = buttonHeight*5 + 10*6;
	
	static int[] moneyValues = {0,0,0,100,100,300,300,300,500,500,500,1000};
	
	private static TextureSingleton textures;
	private static ClickController clicker;
	private static Game game;
	// the player that knows his money and abilities
	CurrentPlayer currentPlayer;
	Sound negatory;
	
	// this arraylist stores if it is selected or not
	private ArrayList <Boolean> owned;
	int ownedCount;
	private ArrayList <Boolean> using;
	int usingCount;
	
	// these arraylists store all of the images
	// that we pulled from the texturesingleton
	
	// these ones are grayscale
	private ArrayList <TextureRegion> shopElements;
	private ArrayList <TextureRegion> boughtElements;
	
	public ArrayList <Button> buttons;
	private TextureRegion usingHighlight;
	private TextureRegion notOwnedHighlight;
	BitmapFont moneyFont;
	public Store(float x, float y)
	{
		// set up the coordinates
		this.x = x;
		this.y = y;
		
		
		// set up the data
		textures = TextureSingleton.getInstance();
		clicker = ClickController.getInstance();
		clicker.addHandler(this);
		game = Game.getInstance();
		
		currentPlayer = game.player;// game.player;
		
		// initialize all the selected to false
		if(currentPlayer != null)
		{
			owned = currentPlayer.owned;
			using = currentPlayer.using;
		}
		usingCount = 3;
		updateUsingCount();
		
		// get the arraylist from textureRegion for the appropriate things
		shopElements = textures.shopElements;
		boughtElements = textures.elements;
		
		buttons = new ArrayList<Button>();
		moneyFont = TextureSingleton.getInstance().scoreFont;
		int count = 0;
		for(int row = 0; row < 4; row ++)
		{
			for(int col = 0; col < 3; col++)
			{
				int rowActual = - ((row + 1)*((int)(buttonHeight) + 10) + (int)(this.y) + 5 + 40);
				int colActual = (col)*(int)(buttonWidth + 10) + (int)(this.x) + 15;
				TextureRegion temp;
				if(count < 3 || owned.get(count))
				{
					temp = boughtElements.get(count);
				}
				else
				{
					temp = shopElements.get(count);
				}
				buttons.add(new Button(temp,colActual,rowActual,buttonWidth,buttonHeight, new OnClickListener(){

					@Override
					public void onClick(Button source, Vector3 pos) {
						for(int i = 0; i < buttons.size(); i++)
						{
							if(buttons.get(i) == source)
							{
								// we found our button index!!!

								// System.out.println("inside the button wut");
								if(owned.get(i))
								{
									// if we are already using it
									if(using.get(i))
									{

										// don't do anything!!!
										// we need at least one left
										if(usingCount <= 1)
										{
											textures.playAccessDenied();
										}
										// get rid of it from using
										else
										{
											using.set(i,false);
										}
									}
									else // 
									{
										if(usingCount < 6)
										{
											using.set(i,true);
										}
										else
											textures.playAccessDenied();
									}
								}
								else // !owned.get(i)
								{
									if(currentPlayer.money < moneyValues[i])
									{
										textures.playAccessDenied();
									}
									else // if you have enuf moneys
									{
										owned.set(i,true);
										buttons.get(i).spr = boughtElements.get(i);
										if(usingCount < 6)
										{
											using.set(i, true);
										}
										currentPlayer.money -= moneyValues[i];
									}
								}
							}
							updateUsingCount();
							// System.out.println("usingCount is " + usingCount);
							currentPlayer.using = using;
							currentPlayer.owned = owned;
						}

					}} )
						);
				count++;
			}

			
		}
	}
	// get the negation sound



	@Override
	public void draw(SpriteBatch sb)
	{

		// there is also a banner on top that has width 80*3, height 144
		// sb.draw(imagetodraw, xcoordinate, coordinate, width, height)
		
		
		//draw the background
		sb.setColor(0.0f,0.0f,0.0f,1.0f);
		sb.draw(new TextureRegion(textures.white), x, y, storeWidth, storeHeight);
		sb.setColor(1.0f,1.0f,1.0f,1.0f);


		// draw the banner
		sb.draw(new TextureRegion(textures.shop), x, storeHeight/2 - 40, storeWidth, buttonHeight);

		
		// draw the money
		// moneyFont.setColor(0.0f,0.0f,0.0f,1.0f);
		// moneyFont.draw(sb , "" + currentPlayer.money , x , y + storeHeight - 15);
		sb.setColor(1.0f,1.0f,1.0f,1.0f);
		
		// draw each button
		for(int i = 0; i < buttons.size(); i++)
		{
			Button b = buttons.get(i);
			
			if(using.get(i))
			{
//				sb.setColor(1.0f,1.0f,1.0f,0.7f);
				sb.draw(textures.yellow,b.x - 4,b.y - 4,b.width + 8,b.height + 8);
				sb.draw(textures.black,b.x - 2,b.y - 2,b.width + 4,b.height + 4);
			}
			buttons.get(i).draw(sb);
//			sb.setColor(1.0f,1.0f,1.0f,1.0f);
		}
	}

	public void updateUsingCount()
	{
		usingCount = 0; 
		for(int i = 0; i < using.size(); i++)
		{
			if(using.get(i))
				usingCount++;
		}
	}
	
	public void dispose()
	{
		// get rid of all the buttons
		for(int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).dispose();
		}
		
		// remove the reference to the store
		game.store = null;// set game's reference to store = null
	}
	
	@Override
	public void onClick(Vector3 clickPos)
	{
		float xclick = clickPos.x;
		float yclick = clickPos.y;
		if(yclick < 95 && yclick > 85)
		{
			if(xclick > 150 && xclick < 165)
			{
				// kill the thing!!!
				// System.out.println("die");
				
				dispose();
				
				
			}
		}
	}

	@Override
	public boolean isInside(Vector3 clickPos)
	{
		
		// click exists inside the store!!!
		float xclick = clickPos.x;
		float yclick = clickPos.y;
		// System.out.println("xclick is " + xclick + " yclick is " + yclick);
		// System.out.println("x is " + x + "y is " + y);
		if(xclick > x && xclick < x + storeWidth && yclick > y && yclick < y + storeHeight)
		{
			// System.out.println("true\n");
			return true;
		}
		// System.out.println("false\n");
		return false;
	}

	@Override
	public int getDepth() {
		return 0;
	}
	
}