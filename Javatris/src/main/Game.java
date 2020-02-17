package main;
import server.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import model.*;
import view.*;
import controller.*;

/**
 * MAIN CLASS
 * @author Philip
 * @version 1.0
 * 
 * Added a simple start-menu
 * @author Jocke
 * @version 1.1
 */

public class Game {
	
	private final  int BlockSize = 40; //resize game <=40 (standard is 40)
	private final  int BoardWidth = 10;
	private final  int BoardHeight = 20;
	
	private JFrame frame;
	private int FrameWidth = 0;
	private int FrameHeight = 0;
	
	private JPanel FixedPanel;
	
	private Board board;
	private BoardView boardView;
	private GameEngine gameEngine; 
	private Controller controller;
	private SideInfo sideInfo;
	private Client client;
	
	private Menu startMenu;
	
	
	public Game() {
		ResizeFrame();
		makeStartmenu();
		//Init();
		//SetUpFrame();	
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
	//Temporary solution
	public void ResizeFrame() {
		FrameWidth = BlockSize * BoardWidth + 14;
		FrameHeight = BlockSize * BoardHeight + 37;  
	}
	
	/*
	 * Initializes the game and creates all the nececcary components that are needed
	 */
	private void Init() {
		board = Board.getInstance();
		boardView = new BoardView(board,BoardHeight,BoardWidth,BlockSize,false);
		sideInfo = new SideInfo();
		gameEngine = new GameEngine(board, boardView,sideInfo,false);
		controller = new Controller(gameEngine);
		gameEngine.start();
	}
	
	/*
	 * Initializes the game just like the other Init but also starts a client and connects to the server for an online session.
	 * @Param ip the ip of the server
	 * @Param port the port of the server
	 */
	private void Init(String ip, int port) {
		board = Board.getInstance();
		boardView = new BoardView(board,BoardHeight,BoardWidth,BlockSize,false);
		sideInfo = new SideInfo();
		gameEngine = new GameEngine(board, boardView,sideInfo,true);
		client = new Client(gameEngine, ip, port);
		gameEngine.delegate = new GameEngine.Delegate() 
		{
			public Client getClient()
			{
				return client.getClient();
			}
		}
		;
		controller = new Controller(gameEngine);
	}
	
	public void SetUpFrame() {
	
		frame = new JFrame("JavaTris");
		frame.setSize(FrameWidth,FrameHeight);	//add 300 on width if sideInfo is included
		
		FixedPanel = new JPanel(new GridBagLayout());
		
		FixedPanel.setPreferredSize(frame.getSize());
		FixedPanel.setBackground(Color.WHITE);

//		FixedPanel.add(sideInfo); 
		FixedPanel.add(boardView);
		frame.add(FixedPanel);
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.addKeyListener(controller);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
	}
	
	//Temporary solution with getters
	public int getBoardWidth() {
		return BoardWidth;
	}
	public int getBoardHeight() {
		return BoardHeight;
	}
	public int getBlockSize() {
		return BlockSize;
	}
	
	/**
	 * Creates a basic start-menu
	 */
	private void makeStartmenu() {
		startMenu = new Menu(new Dimension(200,300), 3);
		JButton startButton = new JButton("START");
		JButton onlineButton = new JButton("ONLINE");
		JButton exitButton = new JButton("EXIT");
		
		startButton.addActionListener((ActionEvent e) -> {startGame();});
		onlineButton.addActionListener((ActionEvent e) -> {startOnlineGame();});
		exitButton.addActionListener((ActionEvent e) -> {System.exit(0);});
		
		startMenu.addElement(startButton); 
		startMenu.addElement(onlineButton); 
		startMenu.addElement(exitButton);
		
		startMenu.openMenu();
	}
	
	/**
	 * Starts game
	 */
	private void startGame() {
		Init();
		SetUpFrame();
		startMenu.closeMenu();
	}
	
	/*
	 * Starts a multiplayer Session of the game, opens up a prompt so the user can enter the ip and port to the server
	 * It should be inputed like this: 127.0.0.1:2525
	 * in other words ip:port
	 */
	private void startOnlineGame() 
	{
		String code = JOptionPane.showInputDialog
		(
	        startMenu.getFrame(), 
	        "Enter the sever ip and port", 
	        "Connect to server", 
	        JOptionPane.PLAIN_MESSAGE
		 );
		
		//Only starts if
		if(!code.isBlank() && !code.isEmpty()) 
		{
			String[] adress = code.split(":");
			Init(adress[0], Integer.parseInt(adress[1]));
			SetUpFrame();
			startMenu.closeMenu();
		}
	}
	
	
}
