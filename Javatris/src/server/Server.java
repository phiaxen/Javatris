package server;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * The Server class for the Javatriss game, it uses the clienthandler to handle each connection to a client.
 * Therefore a ClientHandler is created for each Client connected. 
 * The Server waits until two Clients are connected and then starts a game,
 * afterwards the clients list is reset so another pair of clients can start a game aswell.
 * The server also has a Gui displaying some information and also contains a exit button to shutdown the server
 * @author Andreas Greppe
 * @since 2020-02-29
 *
 */
public class Server 
{
 	private static final int port = 6969;
	private static ServerSocket sSocket;
	//Listan med de olika ClientHandlers så att server kan komma åt dem enkelt
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	//Begränsar antalet uppkopplade Clienter till 2
	//private static ExecutorService pool = Executors.newFixedThreadPool(2);
	//A varaible that is false before the game has started, when both clients are connected it's set to true
	private static Boolean running = false;
	
	
	private static JFrame frame; 
	private static JPanel buttonPanel; 
	private static JButton exitButton;
	private static JLabel matchesStarted;
	private static JLabel clientWaiting;
	private static int matches = 0;
	
	private static Boolean redo = false;
	
	public static void main(String[] args) throws IOException 
	{
		setupGUI();
		
		
		sSocket = new ServerSocket(port);
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		//O�ndlig Loop som k�r servern
		while(true) 
		{
			
			if(redo) {
				clients = new ArrayList<>();
				clientConnected();
			}
			//Sever looks for input if both the clients are connected
			if(clients.size() == 2 && !running) 
			{

				try{ Thread.sleep(1000); }
				catch(InterruptedException e) { e.printStackTrace(); }

				System.out.println("Starting game");
				//messages the clients to start running
				messageClients(11);
				running = true;
				redo = true;
				//Uppdateing GUI
				newMatch();
			}
			else if (clients.size() < 2) 
			{
				redo = false;
				running = false;
				System.out.println("Waiting for Client");
				//Server get a new client connection
				Socket client = sSocket.accept();
				System.out.println("Client connected");
				//Uppdateing GUI
				clientConnected();
				// Createds a new instance of Clienthandler with it's own thread to handle the connected client
				ClientHandler clientThread = new ClientHandler(client, clients);
				//Adds the client to the list of all connected clients
				clients.add(clientThread);
				
				//Starts the thread with the new client
				new Thread(clientThread).start();
			}
			System.out.println("Clients" + clients.size());
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
	
	/**
	 * This Method is used to setup the basic GUI for the server.
	 * It created the exit button and two Jlabels, one to display the wating clients 
	 * and the other one to display the amount of matches.
	 */
	private static void setupGUI() {
		frame = new JFrame();
		
		clientWaiting = new JLabel("No clients waiting");
		
		matchesStarted = new JLabel(matches+" Matches started");
		
		buttonPanel = new JPanel();
		exitButton = new JButton("Exit server");
		//exitButton.setPreferredSize(new Dimension(150,50));
		exitButton.addActionListener((ActionEvent e) -> {System.exit(0);});
		
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		
		buttonPanel.add(clientWaiting);
		buttonPanel.add(matchesStarted);
		buttonPanel.add(exitButton);
		frame.add(buttonPanel);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Is called when a match i started, increments matches and sets the jlabels to the new value
	 */
	private static void newMatch(){
		matches++;
		matchesStarted.setText(matches+" Matches started");
	}
	
	/**
	 * Sets the text of the ClientWating Jlabel depending on if a client is waiting or not.
	 */
	private static void clientConnected() {
		if(redo) {
			clientWaiting.setText("No clients waiting");
		}
		else {
			clientWaiting.setText("1 client waiting");			
		}
	}
}
