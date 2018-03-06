package com.imglow.ElementMMO;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements Drawable{
	public static int WIDTH = 40, HEIGHT = 80;
	
	int blueScore = 0, redScore = 0;
	float dX = 1, dY = 1;
	Cell[][] grid;
	Cell[][] border;
	MainClient client;
	Timer timer;
	TimerTask timerTask;
	
	public CurrentPlayer player;
	public Vector<OtherPlayer> otherPlayers;
	public ChatArea chat;
	private static Game instance;
	public Battle battle;
	public Store store;
	public InstructionsPane instructions;
	public HUD hud;
	
	boolean showWin = false;
	boolean teamWon = false;

	protected Game()
	{
		otherPlayers = new Vector<OtherPlayer>();
	}
	
	public void init(final MainClient client) {
		this.client = client;
		grid = new Cell[WIDTH][HEIGHT];
		chat = new ChatArea();
		
		instructions = new InstructionsPane();
		hud = new HUD();
		timerTask = new TimerTask(){
			@Override
			public void run() {
				client.sql.disconnected();
				Gdx.app.exit();
			}};
		
		// make the stores
		for(int x = (WIDTH / 10)*4; x < (WIDTH / 10)*6; x++)
		{
			for(int y = (HEIGHT/10); y < (HEIGHT/10)*2; y++)
			{
				grid[x][y] = new Cell(x,y,Cell.STORE);
			}
		}
		
		for(int x = (WIDTH/10)*4; x < WIDTH/10*6; x++)
		{
			for(int y = HEIGHT/10*8; y < (HEIGHT/10)*9; y++)
			{
				grid[x][y] = new Cell(x,y,Cell.STORE);
			}
		}
		for(int y = 0; y < HEIGHT; y++)
		{
			// create the left and right trees
			if(grid[0][y] == null)
				grid[0][y] = new Cell(0,y,Cell.TREE);
			if(grid[WIDTH-1][y] == null)
				grid[WIDTH-1][y] = new Cell(WIDTH-1,y,Cell.TREE);
		}
		for(int x = 1; x < WIDTH-1; x++)
		{
			// create the top and bottom trees
			if(grid[x][0] == null)
				grid[x][0] = new Cell(x,0,Cell.TREE);
			if(grid[x][HEIGHT-1] == null)
				grid[x][HEIGHT-1] = new Cell(x, HEIGHT-1, Cell.TREE);
			
		}
		
		// draw the bushes
		for(int x = 1; x < WIDTH - 1; x++)
		{
			if(x < WIDTH/4 || x > 3*WIDTH/4)
			{
				if(grid[x][HEIGHT/4] == null)
					grid[x][HEIGHT/4] = new Cell(x,HEIGHT/4,Cell.BUSH);
				if(grid[x][3*HEIGHT/4] == null)
					grid[x][3*HEIGHT/4] = new Cell(x, 3*HEIGHT/4,Cell.BUSH);
			}
			else
			{
				if(grid[x][HEIGHT/4] == null)
					grid[x][HEIGHT/4] = new Cell(x,HEIGHT/4,Cell.GRASS);
				if(grid[x][3*HEIGHT/4] == null)
					grid[x][3*HEIGHT/4] = new Cell(x, 3*HEIGHT/4,Cell.GRASS);
			}
		}
		
		// fill in the rest with grass
		for(int y = 1; y < HEIGHT - 1; y++)
		{
			for(int x = 1; x < WIDTH - 1; x++)
			{
				if(grid[x][y] == null)
				{
					grid[x][y] = new Cell(x,y,Cell.GRASS);
				}
			}
		}
	}
	
	public static Game getInstance() {
		if(instance == null) {
			instance = new Game();
		}
		return instance;
	}

	@Override
	public void draw(SpriteBatch sb) {
		
		for(int y = 0; y < HEIGHT; y++)
		{
			for(int x = 0; x < WIDTH; x++)
			{
				grid[x][y].draw(sb);
			}
		}
		
		//if battle draw battle
		
//		if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
//		{
//			Battle.tellSeverToIncrementScore(true);
//		}
//		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
//		{
//			Battle.tellSeverToIncrementScore(false);
//		}
		
		StatusUpdate();
		
		// bg.draw(sb);
		if(battle != null)
			battle.draw(sb);
		else
		{
			if(!chat.visible)
			{
				if(player !=null) {
					if(Gdx.input.isKeyPressed(Input.Keys.W))
						player.move(Player.UP);
					else 	if(Gdx.input.isKeyPressed(Input.Keys.S))
						player.move(Player.DOWN);
					else if(Gdx.input.isKeyPressed(Input.Keys.D))
						player.move(Player.RIGHT);
					else if(Gdx.input.isKeyPressed(Input.Keys.A))
						player.move(Player.LEFT);
					
					///////////////DEBUG CODE//////////////////
					
//					}
					///////////////DEBUG CODE//////////////////
						
				}
//				if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
//					chat.visible = true;
//					chat.chatText = ">ew ";
			}
			
			
//			StatusUpdate();
			for(OtherPlayer i : otherPlayers)
			{
				i.draw(sb);
			}
			
			if(player != null)
				player.draw(sb);
			
			if(chat != null)
				chat.draw(sb);
			
			if(store != null)
				store.draw(sb);
				
			if(instructions != null)
				instructions.draw(sb);
			
			if(hud != null)
				hud.draw(sb);
			
			if(showWin)
				drawWin(sb);
		}
	}

	private void drawWin(SpriteBatch sb) {
		BitmapFont font = TextureSingleton.getInstance().scoreFont;
		font.setScale(5.0f);
		
		if(teamWon)
		{
			font.setColor(0.0f,0.0f,1.0f,1.0f);	// blue
			font.draw(sb, "BLUE WINS!", -font.getBounds("BLUE WINS!").width/2, 100);
		}
		else
		{
			font.setColor(1.0f,0.0f,0.0f,1.0f);	// blue
			font.draw(sb, "RED WINS!", -font.getBounds("RED WINS!").width/2, 100);
		}
	}

	public void StatusUpdate()
	{	
		if(player == null) return;
		
		if(MessageManager.getInstance().hasResetMessage())
		{
			teamWon(MessageManager.getInstance().getWinningTeam());
			return;
		}
		
		if(MessageManager.getInstance().hasStatusMessage())
		{
			
//			timer.cancel();
//			timer = new Timer();
//			timer.schedule(timerTask, 1000);
			
			StatusMessage ms = MessageManager.getInstance().getLastStatusMessage();
			
			otherPlayers.removeAllElements();
			
//			System.out.println("Other Players: " + ms.playerPosition.size());
			
			for(MovmentMessage i : ms.playerPosition)
			{
	//								System.out.println(i.toString());
				if(i == null) continue;
				if(i.from.equals(player.name)) continue;
				
				OtherPlayer op = new OtherPlayer();
				op.team1 = i.team1;
				op.type = i.type;
				op.x = i.x;
				op.y = i.y;
				op.startX = i.x;
				op.startY = i.y;
				op.moving = i.moving;
				op.moveDirection = i.direction;
				op.name = i.from;
				op.invincible = i.invincible;
				
				otherPlayers.add(op);
			}
			
			redScore = ms.redScore;
			blueScore = ms.blueScore;
		}
	
	}

	private void teamWon(boolean winningTeam) {
		teamWon = winningTeam;
		showWin = true;
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				showWin = false;
		}}, 3000);
		
		if(battle != null)
			battle.forceEnd();
		
		player.reset();
	}
}
