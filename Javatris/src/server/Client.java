package server;

import java.net.*;

import javax.swing.JOptionPane;

import model.Board;
import model.GameEngine;

import java.io.*;

/**
 * The Client class is used so the game can communicate with the server,
 * it uses a delegate so the connection Handler can communicate with the gameEngine,
 * and also has functions so the rest of the game send messages to the server with the sendInt Method
 * @author Andreas Greppe
 * @since 2020-02-29
 *
 */
public class Client 
{

	private Socket socket;
	static PrintWriter output;
	
	public Client(GameEngine engine, String ip, int port) throws UnknownHostException, IOException
	{
		try {
			socket = new Socket(ip, port);
			//this.engine = engine;
			ConnectionHandler handler = new ConnectionHandler(socket);
			//Lambda function for handling the communcation between the game engine and connection handler
			handler.delegate = new ConnectionHandler.Delegate() {
				
				@Override
				public void addRow(int column) {
					System.out.println("test");
					engine.addRow(column, 1);
				}
				
				@Override
				public void gameOver(int type) {
					System.out.println("Game Over! No");
					engine.gameOver(type);
				}
				
				@Override
				public void start() 
				{
					engine.startOnline();
				}
				
				@Override
				public void pause() 
				{
					engine.pause();
				}
				
				@Override
				public void quit() 
				{
					engine.quit();
				}

				@Override
				public void connectionLost() {
					engine.connectionLost();
				}
			};
			
			
			output =  new PrintWriter(socket.getOutputStream(), true);
			new Thread(handler).start();
		} catch (UnknownHostException e) {
			throw e;
		} catch (IOException e) {
			throw e;
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
	 * returns this instance of the client
	 * @Return Returns this client
	 */
	public Client getClient() 
	{
		System.out.println("getting client");
		return this;
	}

}
