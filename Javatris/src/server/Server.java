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
	
	
	
	public static void main(String[] args) throws IOException 
	{
		sSocket = new ServerSocket(port);
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		//O�ndlig Loop som k�r servern
		while(true) 
		{
			System.out.println("Waiting for Client");
			Socket client = sSocket.accept();
			System.out.println("Client connected");
			// Skapar en ny tr�d av ClientHandler
			ClientHandler clientThread = new ClientHandler(client, clients);
			//L�gger till ClientThreden i listan av alla uppkopplade clienter.
			clients.add(clientThread);
			
			//Startar tr�den f�r ClientThread
			pool.execute(clientThread);
		}

	}
	
	
}
