package com.imglow.ElementMMO;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CurrentPlayer extends Player{

	int money, moveDirection = NOT_MOVING;

	int usingCount;
	boolean blocked = false;
	static int INVINCIBLE_TIME = 60*5; // 5 seconds
	int invincibleTimeRemaining = 0;
	static int WAITING_TIME = 60*2;
	int waitingTime = 0;
	boolean waiting;
	// should only enter store
	// from the edge!!!
	boolean shouldEnterStore;
	ArrayList<Boolean> owned;
	ArrayList<Boolean> using;
	float dist = 0;
	int currX, currY;

	public CurrentPlayer()
	{
		owned = new ArrayList<Boolean>();
		using = new ArrayList<Boolean>();
		money = 200;
		for(int i = 0; i < 12; i++)
		{
			owned.add(false);
			using.add(false);
		}
		owned.set(0,true);
		using.set(0,true);
		owned.set(1,true);
		using.set(1,true);
		owned.set(2,true);
		using.set(2,true);

		currX = (int) x;
		currY = (int) y;
		Game.getInstance().dX = currX;
		Game.getInstance().dY = currY;
	}

	public CurrentPlayer(int type, String name)
	{
		this();
		this.type = type;
		this.name = name;

	}

	public CurrentPlayer(int type, String name, boolean team1)
	{
		this(type, name);
		this.team1 = team1;
		sendToSpawn();
	}

	public void sendToSpawn() {
		moving = false;
		if(team1)
		{
			x = Game.WIDTH/2;
			y = 5;
		}
		else
		{
			x = Game.WIDTH/2;
			y = Game.HEIGHT - 5;
		}

	}

	protected void moveNudge()
	{
		if(moveDirection == UP)
			y = dist + currY;
		if(moveDirection == DOWN)
			y = currY - dist;
		if(moveDirection == RIGHT)
			x = currX + dist;
		if(moveDirection == LEFT)
			x = currX - dist;
		dist += SPEED;
	}

	public void move(int direction){
		if(moving) return;
		currX = (int) x;
		currY = (int) y;
		dist = SPEED;
		blocked = false;
		moveDirection = direction;
		// standing in the store!!
		Game game = Game.getInstance();
		if(game.grid[currX][currY].type != Cell.STORE)
			shouldEnterStore = true;

		if(direction == UP)
		{
			if(game.grid[currX][currY+1].type == Cell.STORE)
			{
				if(shouldEnterStore)
				{
					game.store = new Store(-Store.storeWidth/2,-Store.storeHeight/2);
					shouldEnterStore = false;
				}
			}
			else // not standing in store anymore, kill it
			{
				if(game.store != null)
					game.store.dispose();
			}
			if(game.grid[currX][currY+1].type == Cell.TREE ||
					game.grid[currX][currY+1].type == Cell.BUSH)
			{
				// System.out.println("tree!");
				blocked = true;
				py--;
			}
			if(team1)
			{
				if(currY + 1 >= 3 * Game.HEIGHT / 4)
				{
					blocked = true;
					py--;
				}

			}

		}
		else if(direction == DOWN)
		{
			if(game.grid[currX][currY-1].type == Cell.STORE)
			{
				if(shouldEnterStore)
				{
					game.store = new Store(-Store.storeWidth/2,-Store.storeHeight/2);
					shouldEnterStore = false;
				}
			}
			else // not standing in store anymore, kill it
			{
				if(game.store != null)
					game.store.dispose();
			}
			if(game.grid[currX][currY-1].type == Cell.TREE ||
					game.grid[currX][currY-1].type == Cell.BUSH)
			{
				// System.out.println("tree!");
				blocked = true;
				py++;
			}
			if(!team1) // so you are team2 lol
			{
				if(currY - 1 <= Game.HEIGHT / 4)
				{
					blocked = true;
					py++;
				}
			}

		}
		else if(direction == LEFT)
		{
			if(game.grid[currX-1][currY].type == Cell.STORE)
			{
				if(shouldEnterStore)
				{
					game.store = new Store(-Store.storeWidth/2,-Store.storeHeight/2);
					shouldEnterStore = false;
				}
			}
			else // not standing in store anymore, kill it
			{
				if(game.store != null)
					game.store.dispose();
			}
			if(game.grid[currX-1][currY].type == Cell.TREE ||
					game.grid[currX - 1][currY].type == Cell.BUSH)
			{
				// System.out.println("tree!");
				blocked = true;
				px++;
			}


		}
		else if(direction == RIGHT)
		{
			if(game.grid[currX+1][currY].type == Cell.STORE)
			{
				if(shouldEnterStore)
				{
					game.store = new Store(-Store.storeWidth/2,-Store.storeHeight/2);
					shouldEnterStore = false;
				}
			}
			else // not standing in store anymore, kill it
			{
				if(game.store != null)
					game.store.dispose();
			}
			if(game.grid[currX+1][currY].type == Cell.TREE ||
					game.grid[currX+1][currY].type == Cell.BUSH)
			{
				// System.out.println("tree!");
				blocked = true;
				px--;
			}

		}

		if(blocked)
		{
			dist = 0;
			// this lets us wiggle against the wall
			// otherwise we just don't move :(
		}
		if(!blocked)
		{
			moving = true;
			//			System.out.println("move");
		}

		moveNudge();

	}


	@Override
	public void draw(SpriteBatch sb)
	{
		updateSprite();
		Game game = Game.getInstance();
		MessageManager messageManager = MessageManager.getInstance();
		
		EventMessage received = messageManager.getEventMessage();
		// if we get mail!!!
		// and it is addressed to us
		if(received != null && received.to.equals(game.player.name))
		{
			// gotta respond to the kind gentleman
			// courtesy and such
			System.out.println("received message");
			EventMessage toSend = new EventMessage();
			toSend.to = received.from; // send it to the gent
			toSend.from = game.player.name; // sign at the bottom

			// now figure out what to write for the msg
			if(received.event.substring(0,2).equals("FM")) // FIGHT ME BRO
			{
				System.out.println("received " + received.event);
				// lets check to see if we can
				if(waiting == false && invincible == false && game.battle == null)
				{
					// not waiting, not invincible, and not in a fight
					toSend.event = "OK" + health; // ok bro lets go
					System.out.println("sent message OK" + health);
					if(game.battle == null) // doublecheck cuz shes a looker
					{
						// lets go find the mofo
						// start dis fite
						OtherPlayer other = null;
						// find him
						for(int i = 0; i < game.otherPlayers.size(); i++)
						{
							if(game.otherPlayers.get(i).name.equals(received.from))
							{
								// if the names on the wall
								// rite it down
								System.out.println("joined the fight with them");
								other = game.otherPlayers.get(i);
								other.health = Integer.parseInt(received.event.substring(2,3));
								game.battle = new Battle(this,other);
								break;
							}
						}
						// go to your date
						if(other == null)
						{
							// THSI SHUD NEVER HAPPEN
							// I TRUST YOU IAN
							// PLZ BE GENTLE
						}

					}
				}
				else // im not rdy plz no stahp
				{
					System.out.println("sent message NWB");
					toSend.event = "NWB"; // no way bro cannae
				}
				messageManager.sendMessageToServer(toSend);

			}
			else if(received.event.equals("NWB") && waiting)
			{
				// take the rejection in stride
				System.out.println("received message NWB");
				waiting = false;
				waitingTime = 0;
			}
			else if(received.event.substring(0,2).equals("OK") && waiting) // she wants it
			{
				System.out.println("received message " + received.event);
				waiting = false;
				waitingTime = 0;
				if(game.battle == null)
				{
					OtherPlayer other = null;
					for(int i = 0; i < game.otherPlayers.size(); i++)
					{
						if(game.otherPlayers.get(i).name.equals(received.from))
						{
							// if the names on the wall
							// rite it down
							System.out.println("joined the fight");
							other = game.otherPlayers.get(i);
							other.health = Integer.parseInt(received.event.substring(2,3));
							game.battle = new Battle(this,other);
							break;
						}
					}
					// go to your date
					if(other == null)
					{
						// THSI SHUD NEVER HAPPEN
						// I TaRUST YOU IAN
						// PLZ BE GENTLE
					}

				}
			}

		}


		if(invincible)
		{
			// cut off all contacts with the scrubs
			messageManager.emptyBattleMessages();
			messageManager.emptyEventMessages();
			// make sure it is dead plz
			if(game.battle != null)
				game.battle.dispose();
			invincibleTimeRemaining--;
			if(invincibleTimeRemaining <= 0)
			{
				System.out.println("no longer invincible");
				invincible = false;
			}
		}
		
		else // if(!invincible)
		{
			if(!waiting)
			{
				OtherPlayer other = null;
				for(int i = 0; i < game.otherPlayers.size(); i++)
				{
					other = game.otherPlayers.get(i);
					if(other.x == this.x &&
							other.y == this.y && // we are next to someone!!!
							other.team1 != this.team1  &&// only fight the bad guys now
							game.player.invincible == false) // make sure we wanna
					{
						// we gotta call him out
						System.out.println("encountered an enemy, sent FM" + health);
						EventMessage toSend = new EventMessage();
						toSend.from = game.player.name;
						toSend.to = other.name;
						toSend.event = "FM" + health; // fight me bro
						messageManager.sendMessageToServer(toSend);
						waiting = true;
						waitingTime = WAITING_TIME; // wait for her to call u bak
						break;
					}

				}
				if(other == null)
				{
					// bby plz no
					// i trust
				}
			}
			else // if(waiting)
			{
				waitingTime--;
				if(waitingTime <= 0)
				{
					System.out.println("no longer waiting");
					waiting = false;
				}
			}
		}

		// a previous attempt
		//			else
		//			{
		//				
		//						if(Game.getInstance().battle == null)
		//						{
		//							EventMessage message = new EventMessage();
		//							message.from = Game.getInstance().player.name;
		//							message.to = Game.getInstance().otherPlayers.get(i).name;
		//							message.event = "StartBattle";
		//							System.out.println("sentMessage in currentPlayer for battle Entering");
		//							System.out.println("sm.to = " + message.to);
		//							System.out.println("sm.from = " + message.from);
		//							System.out.println("sm.event = " + message.event);
		//							MessageManager.getInstance().sendMessageToServer(message);
		//							EventMessage received = null;
		//							while(received == null)
		//							{
		//								received = MessageManager.getInstance().getEventMessage();
		//								if(!received.to.equals(Game.getInstance().player.name))
		//								{
		//									received = null;
		//								}
		//											
		//							}
		//							// check the received
		//							if(received.to.equals(Game.getInstance().player.name))
		//							{
		//								if(received.event.equals("StartBattle"))
		//								{
		//									Game.getInstance().battle = new Battle(this, Game.getInstance().otherPlayers.get(i));
		//								}
		//								else
		//								{
		//									
		//								}
		//							}
		//							
		//							// start the other player
		//							
		//						}
		//					}
		//				}
		//				
		//				// this makes sure that if your opponent has started a fight,
		//				
		//				// you start a fight
		//				EventMessage receivedMessage = MessageManager.getInstance().getEventMessage();
		//				if(receivedMessage != null)
		//				{
		//					System.out.println("receivedMessage in currentPlayer for battle Entering");
		//					System.out.println("rm.to = " + receivedMessage.to);
		//					System.out.println("rm.from = " + receivedMessage.from);
		//					System.out.println("rm.event = " + receivedMessage.event);
		//					if(receivedMessage.to.equals(Game.getInstance().player.name) )
		//					{
		//						// he's called you out boyo!!
		//						// figure out which other
		//						for(int i = 0; i < Game.getInstance().otherPlayers.size(); i++)
		//						{
		//							if(Game.getInstance().otherPlayers.get(i).name.equals(receivedMessage.from))
		//							{
		//								
		//								// we found the mofo
		//								if(Game.getInstance().battle == null)
		//								{
		//									Game.getInstance().battle = new Battle(this, Game.getInstance().otherPlayers.get(i));
		//								}
		//								else
		//								{
		//									EventMessage cantGo = new EventMessage();
		//									cantGo.from = Game.getInstance().player.name;
		//									cantGo.to = Game.getInstance().otherPlayers.get(i).name;
		//									cantGo.event = "cantGo";
		//									MessageManager.getInstance().sendMessageToServer(cantGo);
		//									
		//								}
		//							}
		//						}
		//					}
		//							
		//				}
		//			}
		//		}


		if(!blocked)
		{
			if(moving)
			{
				moveNudge();
				if(dist >= 1.0f)
				{
					moving = false;
					dist = 0;
					y = currY;
					x = currX;

					if(moveDirection == UP)
						y++;
					if(moveDirection == DOWN)
						y--;
					if(moveDirection == RIGHT)
						x++;
					if(moveDirection == LEFT)
						x--;
				}
			}
		}

		game.dX = x;
		game.dY = y;

		if(team1)
			sb.setColor(0.5f,0.5f,1f,1f);
		else
			sb.setColor(1f,0.5f,0.5f,1f);

		sb.draw(spr, - WIDTH / 2.0f, - HEIGHT / 2.0f, WIDTH, HEIGHT);

		sb.setColor(Color.WHITE);

		sendPosToServer();
	}

	private void sendPosToServer() {
		MovmentMessage mm = new MovmentMessage();
		mm.direction = moveDirection;
		mm.x = x;
		mm.y = y;
		mm.moving = moving;
		mm.from = name;
		mm.type = type;
		mm.team1 = team1;
		mm.invincible = invincible;
		MessageManager.getInstance().sendMessageToServer(mm);
	}

	public void reset() {
		money = 200;
		health = 6;
		
		owned = new ArrayList<Boolean>();
		using = new ArrayList<Boolean>();
		
		for(int i = 0; i < 12; i++)
		{
			owned.add(false);
			using.add(false);
		}
		owned.set(0,true);
		using.set(0,true);
		owned.set(1,true);
		using.set(1,true);
		owned.set(2,true);
		using.set(2,true);
		
		sendToSpawn();
	}
}
