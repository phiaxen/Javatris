package server;

import java.net.*;

import model.Board;
import model.GameEngine;

import java.io.*;

public class Client 
{

	private Socket socket;
	static PrintWriter output;
	
	public Client(GameEngine engine, String ip, int port) 
	{
		try {
			socket = new Socket(ip, port);
			//this.engine = engine;
			ConnectionHandler handler = new ConnectionHandler(socket);
			//Lambda function for handling the communcation between the game engine and connection handler
			handler.delegate = new ConnectionHandler.Delegate() {
				
				@Override
				public void addRow(int column) {
					engine.addRow(column, 1);
				}
				
				@Override
				public void gameOver() {
					System.out.println("Game Over! No");
				}
				
				@Override
				public void start() 
				{
					engine.start();
				}
				
				@Override
				public void pause() 
				{
					engine.stop();
				}
			};
			
			
			output =  new PrintWriter(socket.getOutputStream(), true);
			new Thread(handler).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		Socket cSocket = new Socket(IP, PORT);

		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		

		
		while (true) 
		{
			System.out.println("--");
			String cmd = keyboard.readLine();
			
			if (cmd.equals("exit"))
			{
				 break;
			}
			else if (cmd.equals("ready")) 
			{
				sendInt(11);
			}
			else if (cmd.equals("lose")) 
			{
				sendInt(10);
			}
			else if (cmd.startsWith("add")) 
			{
				int firstSpace = cmd.indexOf(" ");
				if (firstSpace != -1) 
				{
					sendInt(Integer.parseInt(cmd.substring(firstSpace+1)));
				}
			}
			else 
			{
				output.println(cmd);
			}

		}
		

		
	}
	
	/*
	 *  Sends an integer to the other client via the server
	 *  @param n the message sent
	 */
	public void sendInt(int n)
	{
		output.println("msg " + n);
	}
	
	/*
	 * Closes the socket for server communcation
	 */
	private void exit() 
	{
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
