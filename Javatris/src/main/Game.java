package main;
import server.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
 * @author Joachim Antfolk
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
	private MusicPlayer musicPlayer;

	private Menu startMenu;
	
	
	public Game() {
		makeStartmenu();
		//Init();
		//SetUpFrame();	
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
	
	/*
	 * Initializes the game and creates all the nececcary components that are needed
	 */
	private void Init() {
		musicPlayer = new MusicPlayer(1);
		
		board = new Board();
		gameEngine = new GameEngine(board,false);
		controller = new Controller(gameEngine,musicPlayer);//musicplayer ska inte vara i kontroller egentligen, men har den där för att testa
	}
	
	/*
	 * Initializes the game just like the other Init but also starts a client and connects to the server for an online session.
	 * @Param ip the ip of the server
	 * @Param port the port of the server
	 */
	private void Init(String ip, int port) {
		musicPlayer = new MusicPlayer(1);
		
		board = new Board();
		sideInfo = new SideInfo();
		gameEngine = new GameEngine(board,true);
		client = new Client(gameEngine, ip, port);
		controller = new Controller(gameEngine,musicPlayer);
		
		gameEngine.delegate = new GameEngine.Delegate() 
		{
			@Override
			public Client getClient()
			{
				return client.getClient();
			}
		};		
	}
	
	public void SetUpFrame() {
		sideInfo = new SideInfo();
		boardView = new BoardView(BoardHeight,BoardWidth,BlockSize,true);
		
		frame = new JFrame("JavaTris");
		
		FixedPanel = new JPanel(new GridBagLayout());
		FixedPanel.add(sideInfo); 
		FixedPanel.add(boardView);
		
		frame.add(FixedPanel);
		frame.addKeyListener(controller);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null); //set frame in the middle of the screen
		frame.setResizable(false);
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
		startMenu = new Menu(new Dimension(400,800), 4);
		
//		ImageIcon startButtonImage = null;
//		try {
//			startButtonImage = new ImageIcon(ImageIO.read(new File("src/images/tiles.png")));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		JButton startButton = new JButton("START");
		
		JButton onlineButton = new JButton("ONLINE");
		JButton exitButton = new JButton("EXIT");
		
		startButton.setFont(new Font("Arial", Font.BOLD, 40));
//		startButton.setBackground(Color.BLACK);
		
		
		startButton.addActionListener((ActionEvent e) -> {startGame();});
		
		onlineButton.setFont(new Font("Arial", Font.BOLD, 40));
		onlineButton.addActionListener((ActionEvent e) -> {startOnlineGame();});
		
		exitButton.setFont(new Font("Arial", Font.BOLD, 40));
		exitButton.addActionListener((ActionEvent e) -> {System.exit(0);});
		
		
		startMenu.addElement(startButton); 
		startMenu.addElement(onlineButton); 
		startMenu.addElement(exitButton);
		
		
		startMenu.addTitle("src/images/javatris2.png");
		startMenu.openMenu();
	}
	
	/**
	 * Starts game
	 */
	private void startGame() {
		Init();
		SetUpFrame();
		
		gameEngine.addPropertyChangeListener(boardView);
		gameEngine.addPropertyChangeListener(sideInfo);
		startMenu.closeMenu();
		gameEngine.start();
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
		if(code != null&&!code.isBlank() && !code.isEmpty()) 
		{
			String[] adress = code.split(":");
			Init(adress[0], Integer.parseInt(adress[1]));
			SetUpFrame();
		
			gameEngine.addPropertyChangeListener(boardView);
			gameEngine.addPropertyChangeListener(sideInfo);
			startMenu.closeMenu();
			gameEngine.start();
		}
	}
	
	
}
