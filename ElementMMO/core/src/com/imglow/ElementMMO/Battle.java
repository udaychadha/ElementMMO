package com.imglow.ElementMMO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Battle implements Drawable
{	
	TextureRegion currentPlayerImage;
	TextureRegion otherPlayerImage;

	TextureRegion halfHeartTexture;
	TextureRegion fullHeartTexture;
	TextureRegion noHeartTexture;

	TextureRegion currentPlayerHeartImage1;
	TextureRegion currentPlayerHeartImage2;
	TextureRegion currentPlayerHeartImage3;

	TextureRegion otherPlayerHeartImage1;
	TextureRegion otherPlayerHeartImage2;
	TextureRegion otherPlayerHeartImage3;

	TextureRegion[] currentPlayerInventoryImages;
	Button[] currentPlayerInventoryButtons;

	// Button goButton;

	TextureRegion currentPlayerBattleElementImage;
	TextureRegion otherPlayerBattleElementImage;
	TextureRegion CombatResult;

	int currentPlayerBattleElementNum;
	int otherPlayerBattleElementNum;

	// don't have to worry about references
	// and modify the game health
	// only the current battle health
	int currentPlayerHealth;
	int otherPlayerHealth;

	CurrentPlayer currentPlayer;
	OtherPlayer otherPlayer;

	int TDMcount = 0;
	// SpriteBatch sb;
	String messageToSend;
	String messageReceived;
	boolean gameOver;

	static int MAX_TIME = 60*10; // 60 times per second * 10
	static int PAUSED_TIME = 60;
	int pausedRemaining = 0;
	int timeRemaining = MAX_TIME;

	public Battle(final CurrentPlayer currentPlayer, final OtherPlayer otherPlayer)
	{
		System.out.println("battle started! for player " + currentPlayer.name);
		gameOver = false;
		messageToSend = "";
		messageReceived = "";
		// this.sb = sb;
		this.currentPlayerHealth = currentPlayer.health;
		this.otherPlayerHealth = otherPlayer.health;

		this.currentPlayer = currentPlayer;
		this.otherPlayer = otherPlayer;

		currentPlayerInventoryImages = new TextureRegion[6];
		currentPlayerInventoryButtons = new Button[6];

		fullHeartTexture = TextureSingleton.getInstance().fullHeart;
		halfHeartTexture = TextureSingleton.getInstance().halfHeart;
		noHeartTexture = TextureSingleton.getInstance().noHeart;

		currentPlayerBattleElementNum = -1;
		/*
		goButton = new Button(TextureSingleton.getInstance().go, -40, 0, 60, 60, new OnClickListener()
		{
			@Override
			public void onClick(Button source, Vector3 pos) 
			{
				source.spr = TextureSingleton.getInstance().goGrayed;
				BattleMessage startBattleMessage = new BattleMessage();
				startBattleMessage.to = otherPlayer.name;
				startBattleMessage.event = "" + currentPlayerBattleElementNum;
				MessageManager.getInstance().sendMessageToServer(startBattleMessage);
			}
		});
		 */
		TextureSingleton.getInstance().EnterBattle();
		//load the players information into the GUI.
		assignTextures();

		//client-event of choosing an element to use is sent to the server.

		//other player's message is taken from the server

		//update client's information based on other player's message

		//conditionals 
	}

	@Override
	public void draw(SpriteBatch sb) 
	{

		// when doing the battle, only calculate your own shit
		// don't worry about your opponent's stuff.
		// trust his to work
		Game game = Game.getInstance();
		MessageManager messageManager = MessageManager.getInstance();
		TextureSingleton textureSingleton = TextureSingleton.getInstance();
		if(pausedRemaining == 1)
		{
			// right before pause,
			// reset the images
			otherPlayerBattleElementImage = null;
			currentPlayerBattleElementImage = null;
			timeRemaining = MAX_TIME;
		}
		if(pausedRemaining > 0)
		{
			pausedRemaining--;
		}
		else
		{
			CombatResult = null;
			if(gameOver)
			{
				// player makes themselves invulnerable to interaction
				// for a bit more than a second
				System.out.println("in game over");
				game.player.invincible = true;
				game.player.invincibleTimeRemaining = CurrentPlayer.INVINCIBLE_TIME;
				if(currentPlayerHealth <= 0)
				{
					System.out.println("ok we died");
					// do nothing
					game.player.health = 6;
					System.out.println("sent to spawn");
//					game.player.moving = false;
					game.player.sendToSpawn();
					
					for(int i = 0; i < game.otherPlayers.size(); i++)
					{
						if(game.otherPlayers.get(i).name.equals(otherPlayer.name))
						{
							game.otherPlayers.get(i).health = otherPlayerHealth;
							break;
						}
					}
					// CombatResult = TextureSingleton.getInstance().defeat;
					
				}
				else if(otherPlayerHealth <= 0)
				{
					// do nothing
					// save your current health and collect the moolah
					
					System.out.println("we won!");
					game.player.health = currentPlayerHealth;
					
					game.player.money+= 200;
					tellSeverToIncrementScore(Game.getInstance().player.team1);
					
					// we don't have to reset the health for otherplayer
					// because player's are automatically created
					// with full health
					

				}
				game.hud.assignHealth();
				
				// stop the calls
				messageManager.emptyBattleMessages();
				messageManager.emptyEventMessages();
				
				dispose();
			}
			else
			{

				// we can't see the other player's image after
				// the pause

				//draw window
				// reset the timer
				timeRemaining--;
				if(timeRemaining <= 0)
				{
					// if MAX_TIME seconds elapses
					// if we have not yet received a message from enemy
					// then damage them
					if(messageReceived.equals(""))
					{
						otherPlayerHealth--;
						BattleMessage toSend = new BattleMessage();
						toSend.from = currentPlayer.name;
						toSend.to = otherPlayer.name;
						toSend.event = "TDM";
						messageManager.sendMessageToServer(toSend);
						// resett the images
						currentPlayerBattleElementImage = null;
						otherPlayerBattleElementImage = null;
					}

					// reset the timer
					timeRemaining = MAX_TIME;
				}

				if(TDMcount > 2)
				{
					// we are afk!!!
					// time to leave
					game.player.sendToSpawn();
					// you died!!
					game.player.health = 6;
					// currentPlayer.health = 6;
					BattleMessage toSend = new BattleMessage();
					toSend.from = currentPlayer.name;
					toSend.to = otherPlayer.name;
					// we lost
					// let them know
					toSend.event = "OHL";
					messageManager.sendMessageToServer(toSend);
					pausedRemaining = PAUSED_TIME;
					gameOver = true;
					//dispose();
				}
				// if u have no health left
				// game over man
				else if(otherPlayerHealth <= 0 || currentPlayerHealth <= 0)
				{

					// game over man
					// each player shud calc it themselves
					if(otherPlayerHealth <= 0)
					{
						CombatResult = textureSingleton.victory;
					}
					else if(currentPlayerHealth <= 0)
					{
						CombatResult = textureSingleton.defeat;
						game.player.sendToSpawn();
					}
						
					game.player.invincible = true;
					game.player.invincibleTimeRemaining = CurrentPlayer.INVINCIBLE_TIME;
					pausedRemaining = PAUSED_TIME;
					gameOver = true;


				}
				/*
				else if(currentPlayer.health <= 0)
				{
					BattleMessage toSend = new BattleMessage();
					toSend.to = otherPlayer.name;
					toSend.from = currentPlayer.name;
					toSend.event = "GOW"; // Game Opponent Win (opponent wins)
					MessageManager.getInstance().sendMessageToServer(toSend);

					// close the battle thing

					dispose();
				}
				 */
				else
				{
					// get a message
					// get the latest update from the front
					BattleMessage fromEnemy = messageManager.getBattleMessage();
					if(fromEnemy != null)
					{
						// is it to us
						System.out.println("fromEnemy.to is " + fromEnemy.to);
						System.out.println("fromEnemy.event is " + fromEnemy.event);
						if(fromEnemy.to.equals(currentPlayer.name) && fromEnemy.from.equals(otherPlayer.name))
						{
							// enemy has sent us something!!
							messageReceived = fromEnemy.event;
						}
						
					}


					/*
				// now check the messageReceived stuff
				if(messageReceived.equals("GPW")) // game over
				{
					// we lost boys
					BattleMessage toSend = new BattleMessage();
					toSend.to = otherPlayer.name;
					toSend.from = currentPlayer.name;
					toSend.event = "GOW"; // Game Opponent Win (opponent wins)
					MessageManager.getInstance().sendMessageToServer(toSend);
					Game.getInstance().player.sendToSpawn();
					// you died!!
					Game.getInstance().player.health = 6;
					// close the battle thing
					dispose();

				}
				else if(messageReceived.equals("GOW"))
				{
					// VICTORY !111!!!!1!1!
					// they ran out of health
					Game.getInstance().player.money+= 200;
					// we stay where we are
					// we do not go back to spawn
					BattleMessage toSend = new BattleMessage();
					toSend.to = otherPlayer.name;
					toSend.from = currentPlayer.name;
					toSend.event = "GPW"; // Game Player Win 
					MessageManager.getInstance().sendMessageToServer(toSend);
					dispose();
				}
					 */
					if(messageReceived.equals("TDM"))
					{
						// the enemy waited 10 seconds
						// and we did not send a message
						// we are punished for our transgression
						timeRemaining = MAX_TIME;
						// make sure everytime we do dis

						currentPlayerHealth--;
						otherPlayerBattleElementImage = null;
						currentPlayerBattleElementImage = null;
						// currentPlayerBattleElementImage = TextureSingleton.getInstance().whiteRegion;
						// reset messagereceived
						messageReceived = "";
						messageToSend = "";
						TDMcount++;
					}
					else if(messageReceived.equals("OHL"))
					{
						// our opponent is has lost!!!!!!

						// time to leave
						game.player.money+= 200;
						game.player.invincible = true;
						game.player.invincibleTimeRemaining = CurrentPlayer.INVINCIBLE_TIME;
						pausedRemaining = PAUSED_TIME;
						gameOver = true;
						//dispose();



					}
					else if(!messageReceived.equals(""))
					{
						// messageRecieved = RA#
						if(!messageToSend.equals(""))
						{
							// messageToSend = RA#
							// DANNG
							// compare the messageReceived
							// with our messageToSend
							int playerAttack = Integer.parseInt(messageToSend.substring(2));
							int enemyAttack = Integer.parseInt(messageReceived.substring(2));

							System.out.println("messageToSend being parsed is " + messageToSend.substring(2) +
									" and playerAttack is " + playerAttack);
							System.out.println("messageReceived being parsed is " + messageReceived.substring(2) +
									" and enemyAttack is " + enemyAttack);
							int battleResult = BattleLogics.battle(playerAttack, enemyAttack);

							// only calculate the battle result for your own self!!!
							// do not calculate for opponent
							if(battleResult == -1)
							{
								// we lost boys
								currentPlayerHealth-= 2;
								CombatResult = textureSingleton.lose;

							}
							else if(battleResult == 0)
							{
								// a tie is acceptable
								// i guess
								currentPlayerHealth--;
								otherPlayerHealth--;
								CombatResult = textureSingleton.tie;
							}
							else // battleResult == 1
							{
								// WE WON!!!
								// enemy damages himself pew pew
								otherPlayerHealth-= 2;
								CombatResult = textureSingleton.win;
							}
							// reset our messagelistener things
							messageToSend = "";
							messageReceived = "";
							timeRemaining = MAX_TIME;
							currentPlayerBattleElementImage = textureSingleton.elements.get(playerAttack);
							otherPlayerBattleElementImage = textureSingleton.elements.get(enemyAttack);
							pausedRemaining = PAUSED_TIME;
						}
						else // (!messageToSend.equals("")) // still waiting on a msg from currentPlayer
						{
							// still waiting for the currentPlayer to send a msg
						}
					}
				}

			}
		}
		assignHealth();
		sb.draw(textureSingleton.msgBox, -MainClient.WIDTH/4, -MainClient.HEIGHT/4, MainClient.WIDTH/2, MainClient.HEIGHT/2);		

		//draw BATTLE!
		
		sb.draw(textureSingleton.battle, -100, 100, 200, 60);

		// sb.draw(goButton.spr, -40, 0, 60, 60);

		//draw player sprites

		sb.draw(currentPlayerImage, -MainClient.HEIGHT/2+75, 0, Player.WIDTH, Player.HEIGHT);
		sb.draw(otherPlayerImage, MainClient.HEIGHT/2-150, 0, Player.WIDTH, Player.HEIGHT);

		//draw health amounts
		//this player
		sb.draw(currentPlayerHeartImage1, -MainClient.WIDTH/4+40, 100, 16, 16);
		sb.draw(currentPlayerHeartImage2, -MainClient.WIDTH/4+60, 100, 16, 16);
		sb.draw(currentPlayerHeartImage3, -MainClient.WIDTH/4+80, 100, 16, 16);
		//other player
		sb.draw(otherPlayerHeartImage3, MainClient.WIDTH/4-70, 100, 16, 16);
		sb.draw(otherPlayerHeartImage2, MainClient.WIDTH/4-90, 100, 16, 16);
		sb.draw(otherPlayerHeartImage1, MainClient.WIDTH/4-110, 100, 16, 16);

		//draw space for element slots
//		sb.draw(textureSingleton.gray, -MainClient.WIDTH/4+160, -MainClient.HEIGHT/4+10, MainClient.WIDTH/4 - 20, MainClient.HEIGHT/4-50);

		//draw element slots
		currentPlayerInventoryButtons[0].width = 100;
		currentPlayerInventoryButtons[0].height = 30;
		currentPlayerInventoryButtons[0].x = -MainClient.WIDTH/4+180;
		currentPlayerInventoryButtons[0].y = -80;
		sb.draw(currentPlayerInventoryImages[0], -MainClient.WIDTH/4+180, -80, 100, 30);

		currentPlayerInventoryButtons[1].width = 100;
		currentPlayerInventoryButtons[1].height = 30;
		currentPlayerInventoryButtons[1].x = -MainClient.WIDTH/4+180;
		currentPlayerInventoryButtons[1].y = -120;
		sb.draw(currentPlayerInventoryImages[1], -MainClient.WIDTH/4+180, -120, 100, 30);

		currentPlayerInventoryButtons[2].width = 100;
		currentPlayerInventoryButtons[2].height = 30;
		currentPlayerInventoryButtons[2].x = -MainClient.WIDTH/4+180;
		currentPlayerInventoryButtons[2].y = -160;
		sb.draw(currentPlayerInventoryImages[2], -MainClient.WIDTH/4+180, -160, 100, 30);

		currentPlayerInventoryButtons[3].width = 100;
		currentPlayerInventoryButtons[3].height = 30;
		currentPlayerInventoryButtons[3].x = -MainClient.WIDTH/4+340;
		currentPlayerInventoryButtons[3].y = -80;
		sb.draw(currentPlayerInventoryImages[3], -MainClient.WIDTH/4+340, -80, 100, 30);

		currentPlayerInventoryButtons[4].width = 100;
		currentPlayerInventoryButtons[4].height = 30;
		currentPlayerInventoryButtons[4].x = -MainClient.WIDTH/4+340;
		currentPlayerInventoryButtons[4].y = -120;
		sb.draw(currentPlayerInventoryImages[4], -MainClient.WIDTH/4+340, -120, 100, 30);

		currentPlayerInventoryButtons[5].width = 100;
		currentPlayerInventoryButtons[5].height = 30;
		currentPlayerInventoryButtons[5].x = -MainClient.WIDTH/4+340;
		currentPlayerInventoryButtons[5].y = -160;
		sb.draw(currentPlayerInventoryImages[5], -MainClient.WIDTH/4+340, -160, 100, 30);

		//draw battle element slots
		if(currentPlayerBattleElementImage != null)
			sb.draw(currentPlayerBattleElementImage, -180, 30);

		if(otherPlayerBattleElementImage != null)
			sb.draw(otherPlayerBattleElementImage, 80, 30);
		
		// sb.draw(new Texture(Gdx.files.internal("vs.jpg")), -25, 0, 30, 30);
		// sb.draw(TextureSingleton.getInstance().whiteRegion, 75, 0);

		if(pausedRemaining <= 0)
		{
			BitmapFont timerDraw = textureSingleton.scoreFont;
			sb.setColor(Color.BLACK);
			timerDraw.setColor(Color.BLACK);
			int timeRemainingDigit = (timeRemaining+1)/60;
			timerDraw.draw(sb , "" + timeRemainingDigit, -30, MainClient.HEIGHT/2.0f - 50);
			sb.setColor(Color.WHITE);
		}
		else
		{
			if(CombatResult != null)
					sb.draw(CombatResult, -60,0,100,100);
			
		}

		//sb.setColor(Color.WHITE);

		// scoreFont.draw
	}



	public void assignHealth()
	{
		switch(currentPlayerHealth)
		{
		case 0:
			currentPlayerHeartImage1 = noHeartTexture;
			currentPlayerHeartImage2 = noHeartTexture;
			currentPlayerHeartImage3 = noHeartTexture;
			break;
		case 1:
			currentPlayerHeartImage1 = halfHeartTexture;
			currentPlayerHeartImage2 = noHeartTexture;
			currentPlayerHeartImage3 = noHeartTexture;
			break;
		case 2:
			currentPlayerHeartImage1 = fullHeartTexture;
			currentPlayerHeartImage2 = noHeartTexture;
			currentPlayerHeartImage3 = noHeartTexture;
			break;
		case 3:
			currentPlayerHeartImage1 = fullHeartTexture;
			currentPlayerHeartImage2 = halfHeartTexture;
			currentPlayerHeartImage3 = noHeartTexture;
			break;
		case 4:
			currentPlayerHeartImage1 = fullHeartTexture;
			currentPlayerHeartImage2 = fullHeartTexture;
			currentPlayerHeartImage3 = noHeartTexture;
			break;
		case 5: 
			currentPlayerHeartImage1 = fullHeartTexture;
			currentPlayerHeartImage2 = fullHeartTexture;
			currentPlayerHeartImage3 = halfHeartTexture;
			break;
		case 6:
			currentPlayerHeartImage1 = fullHeartTexture;
			currentPlayerHeartImage2 = fullHeartTexture;
			currentPlayerHeartImage3 = fullHeartTexture;
			break;
		}

		switch(otherPlayerHealth)
		{
		case 0:
			otherPlayerHeartImage1 = noHeartTexture;
			otherPlayerHeartImage2 = noHeartTexture;
			otherPlayerHeartImage3 = noHeartTexture;
			break;
		case 1:
			otherPlayerHeartImage1 = halfHeartTexture;
			otherPlayerHeartImage2 = noHeartTexture;
			otherPlayerHeartImage3 = noHeartTexture;
			break;
		case 2:
			otherPlayerHeartImage1 = fullHeartTexture;
			otherPlayerHeartImage2 = noHeartTexture;
			otherPlayerHeartImage3 = noHeartTexture;
			break;
		case 3:
			otherPlayerHeartImage1 = fullHeartTexture;
			otherPlayerHeartImage2 = halfHeartTexture;
			otherPlayerHeartImage3 = noHeartTexture;
			break;
		case 4:
			otherPlayerHeartImage1 = fullHeartTexture;
			otherPlayerHeartImage2 = fullHeartTexture;
			otherPlayerHeartImage3 = noHeartTexture;
			break;
		case 5: 
			otherPlayerHeartImage1 = fullHeartTexture;
			otherPlayerHeartImage2 = fullHeartTexture;
			otherPlayerHeartImage3 = halfHeartTexture;
			break;
		case 6:
			otherPlayerHeartImage1 = fullHeartTexture;
			otherPlayerHeartImage2 = fullHeartTexture;
			otherPlayerHeartImage3 = fullHeartTexture;
			break;
		}
	}
	public void assignTextures()
	{
		//assign player sprites
		TextureSingleton textureSingleton = TextureSingleton.getInstance();
		currentPlayerImage = textureSingleton.playerSprites.get(currentPlayer.type).get(TextureSingleton.STAND);
		otherPlayerImage = textureSingleton.playerSprites.get(otherPlayer.type).get(TextureSingleton.STAND);

		//assign health amounts

		assignHealth();
		//assign element textures

		int inventoryElementNum = 0;
		for(int i = 0; i < 12; i++)
		{
			//if all inventory spots have been exhausted
			if(inventoryElementNum >= 6)
			{
				//stop checking whether further elements are owned by current player
				break;
			}

			//if the player owns a given item
			if(currentPlayer.using.get(i))
			{
				//add that item to their inventory display at the next available spot
				currentPlayerInventoryImages[inventoryElementNum] = textureSingleton.elements.get(i);
				//indicate that one should now move to the next available spot
				currentPlayerInventoryButtons[inventoryElementNum] = new Button(currentPlayerInventoryImages[inventoryElementNum], 0, 0, 0, 0, 
						new OnClickListener()
				{
					@Override
					public void onClick(Button source, Vector3 pos) 
					{
						// assignHealth();
						// if not paused, then do stuff
						if(pausedRemaining <= 0)
						{

							// figure out which one is selected
							int selected = 0;
							for(int i = 0; i < currentPlayerInventoryButtons.length; i++)
							{
								if(source == currentPlayerInventoryButtons[i])
									selected = i;
							}
							// now we have a selected
							for(int i = 0; i < 12; i++)
							{
								if(currentPlayerInventoryImages[selected] == TextureSingleton.getInstance().elements.get(i))
									currentPlayerBattleElementNum = i;
							}
							// set up the thing to send

							// now we know the proper value to send to the bad guy
							// let him know what's comin to him
							currentPlayerBattleElementImage = source.spr;
							// source.spr = TextureSingleton.getInstance().goGrayed;
							messageToSend = "RA" + currentPlayerBattleElementNum;
							BattleMessage battleMessage = new BattleMessage();

							battleMessage.to = otherPlayer.name;
							battleMessage.from = currentPlayer.name;
							battleMessage.event = messageToSend;

							MessageManager.getInstance().sendMessageToServer(battleMessage);
							// messages to send
							// RA# means that you are doing a regular attack of type #
							// TDM take damage man, opp has stalled, so opp takes damage
							// otherwise end the battle
							// based upon health

						}
					}
				}
						);
				inventoryElementNum++;
			}
		}

		//if not all inventory spots have been exhausted
		while(inventoryElementNum < 6)
		{
			//fill remaining inventory elements with white space
			currentPlayerInventoryImages[inventoryElementNum] = textureSingleton.spot;
			currentPlayerInventoryButtons[inventoryElementNum] = new Button(currentPlayerInventoryImages[inventoryElementNum], 0, 0, 0, 0, 
					new OnClickListener()
			{
				@Override
				public void onClick(Button source, Vector3 pos) 
				{
					// does jack shit
					// this is filler because this player is nub
					System.out.println("White space clicked");
				}
			}
					);
			inventoryElementNum++;
		}

		//assign battle element slots as whitespace until event causes them to be otherwise
		// currentPlayerBattleElementImage = TextureSingleton.getInstance().whiteGrass;
		// otherPlayerBattleElementImage = TextureSingleton.getInstance().whiteGrass;
	}

	//static helps debugging
	public static void tellSeverToIncrementScore(boolean team1)
	{
		
		// let them know
		// of your crimes
		EventMessage msg = new EventMessage();
		msg.to = "server";

		if(team1)
			msg.event = "b";
		else
			msg.event = "r";

		MessageManager.getInstance().sendMessageToServer(msg);
	}


	public void dispose()
	{
		// taking out the trash
		for(int i = 0; i < 6; i++)
		{
			currentPlayerInventoryButtons[i].dispose();
		}
		// dun dun dun
		TextureSingleton.getInstance().ExitBattle();
		
		// obituary
		Game.getInstance().battle = null;
	}

	public void forceEnd()
	{
		// plz die plz
		// plz i need u to
		// shhhhh
		// itll all be over soon
		dispose();
		
	}
}
