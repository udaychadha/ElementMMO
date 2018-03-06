package com.imglow.ElementMMO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class HUD implements Drawable {
	
	// VARS START ----------------------------
	
	// position & dimension data
	static final float screenW = 1280, screenH = 720;
	//static final float moneyW = 0, moneyH = 0;	// TODO - ADJUST MONEY DRAW POSITION SO UP IN TOP LEFT CORNER OF SCREEN
	
	// references
	private static TextureSingleton textures;
	private static Game game;
	CurrentPlayer currentPlayer;
	
	// visual data
//	int team1Score, team2Score;
	//int money;
	BitmapFont HUDFont;
	
	// VARS END ------------------------------
	
	TextureRegion currentPlayerHeartImage1;
	TextureRegion currentPlayerHeartImage2;
	TextureRegion currentPlayerHeartImage3;
	
	
	public HUD ()
	{
		// initialize singleton references
		textures = TextureSingleton.getInstance();
		game = Game.getInstance();
		currentPlayer = game.player;
		HUDFont = TextureSingleton.getInstance().scoreFont;
		
		currentPlayerHeartImage1 = TextureSingleton.getInstance().fullHeart;
		currentPlayerHeartImage2 = TextureSingleton.getInstance().fullHeart;
		currentPlayerHeartImage3 = TextureSingleton.getInstance().fullHeart;
		// get hud values
		// TODO - SCORE IS NOT YET BEING KEPT IN GAME TO BE GRABBED HERE FOR THE HUD DISPLAY
		//money = game.player.money;
	}
	
	
	
	@Override
	public void draw (SpriteBatch sb)
	{
		Game g = Game.getInstance();
		assignHealth();
		// draw the money at money coordinates
		//HUDFont.setColor(0.0f,0.0f,0.0f,1.0f);
		sb.setColor(Color.WHITE);
		HUDFont.setColor(1.0f,1.0f,1.0f,1.0f);
		HUDFont.setScale(2.0f);
		HUDFont.draw(sb , "$ " + currentPlayer.money , -(screenW/2) + 20, (screenH/2) - 20);
		
		// draw team scores
		sb.setColor(Color.WHITE);
		HUDFont.setColor(0.0f,0.0f,1.0f,1.0f);	// blue
		HUDFont.setScale(3.0f);
		HUDFont.draw(sb, "" + g.blueScore, -100 - 20, (screenH/2) - 20);	// subtracting roughly text width from x
		HUDFont.setColor(1.0f,0.0f,0.0f,1.0f);	// red
		HUDFont.setScale(3.0f);
		HUDFont.draw(sb, "" + g.redScore, 100 - 20, (screenH/2) - 20);	// subtracting roughly text width from x
		
		sb.draw(currentPlayerHeartImage1, -(screenW/2) + 20, (screenH/2) - 100, 40, 40);
		
		sb.draw(currentPlayerHeartImage2, -(screenW/2) + 70, (screenH/2) - 100, 40, 40);
		
		sb.draw(currentPlayerHeartImage3, -(screenW/2) + 120, (screenH/2) - 100, 40, 40);
	}
	
	public void assignHealth()
	{
		switch(Game.getInstance().player.health)
		{
		case 0:
			currentPlayerHeartImage1 = TextureSingleton.getInstance().noHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().noHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().noHeart;
			break;
		case 1:
			currentPlayerHeartImage1 = TextureSingleton.getInstance().halfHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().noHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().noHeart;
			break;
		case 2:
			currentPlayerHeartImage1 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().noHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().noHeart;
			break;
		case 3:
			currentPlayerHeartImage1 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().halfHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().noHeart;
			break;
		case 4:
			currentPlayerHeartImage1 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().noHeart;
			break;
		case 5: 
			currentPlayerHeartImage1 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().halfHeart;
			break;
		case 6:
			currentPlayerHeartImage1 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage2 = TextureSingleton.getInstance().fullHeart;
			currentPlayerHeartImage3 = TextureSingleton.getInstance().fullHeart;
			break;
		}
	}

}

























