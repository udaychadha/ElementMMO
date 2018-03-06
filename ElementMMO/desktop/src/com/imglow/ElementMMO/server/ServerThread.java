package com.imglow.ElementMMO.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.imglow.ElementMMO.BattleMessage;
import com.imglow.ElementMMO.EventMessage;
import com.imglow.ElementMMO.Message;
import com.imglow.ElementMMO.MovmentMessage;
import com.imglow.ElementMMO.StatusMessage;
import com.imglow.ElementMMO.TextMessage;

class ServerThread extends Thread {
	
	String user;
	Socket mySocket;
	ServerLauncher sl;
	Vector<Message> queue;
	Timer timer;
	TimerTask timerTask;
	private Thread output, input;
	MovmentMessage lastMVMessage;

	Object msgLock = new Object();

	public ServerThread (Socket initSocket, final ServerLauncher sl)
	{
		mySocket = initSocket;
		this.sl = sl;

		final ServerThread thiss = this;
		queue = new Vector<Message>();
//		movementMessages = new Vector<MovmentMessage>(); 

		timerTask = new TimerTask(){
			@Override
			public void run() {
				output.interrupt();
				input.interrupt();
				synchronized(sl)
				{
					sl.serverThreads.remove(thiss);
				} 
			}};
	}

	public void run()
	{		
		output= new Thread(new Runnable(){
			public void run() {
				try {
					ObjectOutputStream br = new ObjectOutputStream(mySocket.getOutputStream());
					int x = 2;
					while(x == 2)
					{
						if(queue.size() > 0)
						{
							br.writeObject(queue.get(0));
							queue.remove(0);
						}
						else
						{
							synchronized(msgLock)
							{
								try {
									msgLock.wait();
								} catch (InterruptedException e) {
									break;
								}
							}
						}
					}
					br.close();
				} catch (IOException ioe) {
					System.out.println( "IOExceptionin Client constructor: " + ioe.getMessage() );
				}
			}
		});
		output.start();

		input = new Thread(new Runnable(){

			@Override
			public void run() {
				ObjectInputStream is;
				
				try
				{	
					is = new ObjectInputStream(mySocket.getInputStream());
				}
				catch (Exception ioe) { 
						System.out.println("Exception in ServerThread.input.run(): "); 
						ioe.printStackTrace();
						timerTask.run();
						return;
					};
				

				while (true)
				{
					Message msg;
					try {
						msg = (Message) is.readObject();
						
						if(user == null)
							user = msg.from;
						
//						System.out.println("Msg from:" + msg.from);
						if(msg.messageType == Message.MOVEMENT)
							lastMVMessage = (MovmentMessage) msg;
						else if(msg.messageType == Message.EVENT)
							sl.eventMessages.add((EventMessage) msg);
						else if(msg.messageType == Message.BATTLE)
							sl.battleMessages.add((BattleMessage) msg);
						else
							sl.textMessages.add((TextMessage) msg);
						
					}catch (SocketException e) {
						System.out.println("Socket Closed");
						timerTask.run();
						break;
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		input.start();
	}

	public void SendMessage(Message msg)
	{
		queue.add(msg);
		synchronized(msgLock)
		{
			msgLock.notifyAll();
		}
	}

	public MovmentMessage getLastMovmentMesssage()
	{
//		if(movementMessages.isEmpty()) return null;

		return lastMVMessage;
	}
}