package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server 
{
 	private static final int port = 6969;
	private static ServerSocket sSocket;
	//Listan med de olika ClientHandlers så att server kan komma åt dem enkelt
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	//Begränsar antalet uppkopplade Clienter till 2
	private static ExecutorService pool = Executors.newFixedThreadPool(2);
	//A varaible that is false before the game has started, when both clients are connected it's set to true
	private static Boolean running = false;
	
	
	
	public static void main(String[] args) throws IOException 
	{
		sSocket = new ServerSocket(port);
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		//O�ndlig Loop som k�r servern
		while(true) 
		{
			//Sever looks for input if both the clients are connected
			if(clients.size() == 2 && !running) 
			{
					System.out.println("Starting game");
					//messages the clients to start running
					messageClients(11);
					running = true;
			}
			else if (clients.size() < 2 && running ) 
			{		
					System.out.println("I should quit");
					//Stops the clients if either stops, should be a quit the mulitplayer session in the future or atleast give a notice to the player.
					messageClients(12);
					System.exit(1);
			}
			else if (clients.size() < 2) 
			{
				System.out.println("Waiting for Client");
				//Server get a new client connection
				Socket client = sSocket.accept();
				System.out.println("Client connected");
				// Createds a new instance of Clienthandler with it's own thread to handle the connected client
				ClientHandler clientThread = new ClientHandler(client, clients);
				//Adds the client to the list of all connected clients
				clients.add(clientThread);
				
				//Starts the thread with the new client
				pool.execute(clientThread);
			}
		}

	}
	
	/*
	 * Sends a message to all the clients connected to the server
	 * @param msg the message that is sent to all the clients
	 */
	private static void messageClients(int msg) 
	{
		for(ClientHandler sclient : clients) 
		{
				sclient.getWriter().println(msg);
		}
	}
	
}
