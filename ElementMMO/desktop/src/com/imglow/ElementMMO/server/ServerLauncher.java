package com.imglow.ElementMMO.server;

import java.util.*;
import java.net.*;
import java.io.*;

import com.imglow.ElementMMO.BattleMessage;
import com.imglow.ElementMMO.EventMessage;
import com.imglow.ElementMMO.Message;
import com.imglow.ElementMMO.MovmentMessage;
import com.imglow.ElementMMO.ResetMessage;
import com.imglow.ElementMMO.StatusMessage;
import com.imglow.ElementMMO.TextMessage;

public class ServerLauncher {

	public final static int WINSCORE = 10;
	static int PORT = 25565;
	
	Vector<Message> queue;
	Vector<TextMessage> textMessages;
	Vector<MovmentMessage> movementMessages;
	Vector<EventMessage> eventMessages ;
	Vector<BattleMessage> battleMessages;
	Vector<ServerThread> serverThreads;
	Vector<Message> playerPositions;
	Object msgLock = new Object();
	Runnable think, output;
	int blueScore = 0, redScore = 0;

	//This is for you josh!
	public ServerLauncher (final int port)
	{
		System.out.println("****Server Starting****");
		try {
			System.out.println("IP: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			System.out.println("IP: N/A");
		}
		
		final ServerLauncher thiss = this;
		queue = new Vector<Message>();
		textMessages = new Vector<TextMessage>();
		movementMessages = new Vector<MovmentMessage>();
		eventMessages = new Vector<EventMessage>() ;
		battleMessages = new Vector<BattleMessage>();
		serverThreads = new Vector<ServerThread>();

		output = new Runnable(){
			public void run() {
				int x = 2;
				while(x == 2)
				{
					if(queue.size() > 0)
					{
						Message msg = queue.get(0);
						if(msg != null)
						{
							synchronized(thiss)
							{
								for(ServerThread i : serverThreads)
								{
									//Was i.SendMessage(deepClone(msg));
									i.SendMessage(msg);
									
//									System.out.println("Message sent from : " + msg.from + " to " + i.user);
								}
							}
						}
						queue.remove(0);
					}
//					else
//					{
//						synchronized(msgLock)
//						{
//							try {
//								msgLock.wait();
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								return;
//							}
//						}
//					}

				}
			}
		};
		new Thread(output).start();

		think = new Runnable(){
			@Override
			public void run() {
				while(true)
				{
					synchronized(this){
						try {
							this.wait(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
//					System.out.println(serverThreads.size() + "usuers");
					if(serverThreads.size() == 0) continue;
					
					while(hasBattleMessage())
					{
						sendMessage(getBattleMessage());
					}
					
					while(hasTextMessage())
					{
						sendMessage(getTextMessage());
					}
					
					while(hasEventMessage())
					{
						EventMessage msg = getEventMessage();
						
						if(msg.to.equals("server"))
						{
							if(msg.event.equalsIgnoreCase("b"))
								blueScore++;
							else
								redScore++;
							
							if(redScore >= WINSCORE)
							{
								blueScore = 0;
								redScore = 0;
								
								ResetMessage rs = new ResetMessage();
								rs.team1win = false;
								
								sendMessage(rs);
							}
							else if(blueScore >= WINSCORE)
							{
								blueScore = 0;
								redScore = 0;
								
								ResetMessage rs = new ResetMessage();
								rs.team1win = true;
								
								sendMessage(rs);
							}
						}
						else
							sendMessage(msg);
					}	

					StatusMessage sm = new StatusMessage();
					sm.from = "server";
					sm.playerPosition = new Vector<MovmentMessage>();
					
					for(ServerThread i : serverThreads)
					{
						if(i.getLastMovmentMesssage() != null)
							sm.playerPosition.add(i.getLastMovmentMesssage());
					}
					
					sm.blueScore = blueScore;
					sm.redScore = redScore;
					
					sendMessage(sm);
				}
			}
		};
		new Thread(think).start();

		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(port);
					while (true)
					{
						Socket s = ss.accept();
						ServerThread st = new ServerThread(s, thiss);
						synchronized(thiss)
						{
							serverThreads.add(st);
						}
						st.start();
					}
				}
				catch (IOException ioe) { System.out.println("IOException in ServerLauncher Constructor: " + ioe.getMessage()); }
			}
		}).start();
	}

	public void sendMessage(Message msg)
	{
		queue.add(msg);
		synchronized(msgLock)
		{
			msgLock.notifyAll();
		}
	}

	public Message deepClone(Message m) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(m);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Message) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public boolean messageQueued()
	{
		return !queue.isEmpty();
	}
	
	public boolean hasMovmentMessage()
	{
		return !movementMessages.isEmpty();
	}

	public MovmentMessage getMovementMessage()
	{
		if(!hasMovmentMessage())
			return null;
		
		MovmentMessage temp = movementMessages.firstElement();
		movementMessages.remove(0);
		return temp;
	}
	
	public boolean hasEventMessage()
	{
		return !eventMessages.isEmpty();
	}

	public EventMessage getEventMessage()
	{
		if(!hasEventMessage())
			return null;
		
		EventMessage temp = eventMessages.firstElement();
		eventMessages.remove(0);
		return temp;
	}
	
	public boolean hasTextMessage()
	{
		return !textMessages.isEmpty();
	}

	public TextMessage getTextMessage()
	{
		if(!hasTextMessage())
			return null;
		
		System.out.println("got a text message in serverlauncher");
		TextMessage temp = textMessages.firstElement();
		textMessages.remove(0);
		return temp;
	}
	
	public boolean hasBattleMessage()
	{
		return !battleMessages.isEmpty();
	}

	public Message getBattleMessage()
	{
		if(!hasBattleMessage())
			return null;
		
		BattleMessage temp = battleMessages.firstElement();
		battleMessages.remove(0);
		return temp;
	}
	
	public static void main (String[] args)
	{
		ServerLauncher gameServer = new ServerLauncher(PORT);
	}
}


















