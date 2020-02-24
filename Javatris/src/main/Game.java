package main;
import server.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
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
	//hej
	private final  int BlockSize = 40; //resize game <=40 (standard is 40)
	private final  int BoardWidth = 10;
	private final  int BoardHeight = 20;
	
	private final JFrame frame;
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
	private SfxManager sfxManager;
	private StartMenu startMenu;
	private Menu pauseMenu;
	private JPanel gamePanel;
	private boolean firstGame = true;
	
	public Game(JFrame frame) {
		this.frame = frame;
//		ResizeFrame();
		Init();
		makeStartmenu();
		
		SetUpFrame();
	
	}
	
	public static void main(String[] args) {
		new Game(new JFrame("JavaTris"));
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
		musicPlayer = new MusicPlayer(1);
	
		board = new Board();
		boardView = new BoardView(board,BoardHeight,BoardWidth,BlockSize,false);
		sideInfo = new SideInfo();
		gameEngine = new GameEngine(board, boardView,sideInfo,false);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new Controller(gameEngine,musicPlayer));
		
		gameEngine.delegate = new GameEngine.Delegate() 
		{
			@Override
			public Client getClient()
			{
				return client.getClient();
			}
			
			@Override
			public void createPauseMenu()
			{
				System.out.println(this);
				System.out.println("create the pause");
				musicPlayer.stop();
				makePauseMenu();
			}
			
		};
		
		
	}
	
	
	
	/*
	 * Initializes the game just like the other Init but also starts a client and connects to the server for an online session.
	 * @Param ip the ip of the server
	 * @Param port the port of the server
	 */
	private void Init(String ip, int port) {
//		musicPlayer = new MusicPlayer(1);
//		
//		board = new Board();
//		boardView = new BoardView(board,BoardHeight,BoardWidth,BlockSize,false);
//		sideInfo = new SideInfo();
//		gameEngine = new GameEngine(board, boardView,sideInfo,true);
//		client = new Client(gameEngine, ip, port);
//		controller = new Controller(gameEngine,musicPlayer);
//		startMenu = new Menu(new Dimension(800,400),5);

		
		gameEngine.delegate = new GameEngine.Delegate() 
		{
			@Override
			public Client getClient()
			{
				return client.getClient();
			}
			
			@Override
			public void createPauseMenu()
			{
			}
			
		};
		
//		gameEngine.start();
	}
	
	public void SetUpFrame() {
	
		
//		frame.setSize(FrameWidth + 300,FrameHeight);	//add 300 on width if sideInfo is included
		frame.setSize(715,860);
		
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridBagLayout());
		gamePanel.setPreferredSize(new Dimension(700,820));
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,10));
		gamePanel.add(sideInfo);
		gamePanel.add(boardView);
		
		FixedPanel = new JPanel();
		FixedPanel.setLayout(new GridBagLayout());
		FixedPanel.setPreferredSize(frame.getSize());
		FixedPanel.setBackground(Color.BLACK);
		
		FixedPanel.add(startMenu);
		
		frame.add(FixedPanel);
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//		frame.setUndecorated(true);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		frame.setLocationRelativeTo(null); //set frame in the middle of the screen
		
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
		
		startMenu = new StartMenu(new Dimension(500,600),true,4,Color.BLACK);
		startMenu.addTitle("/images/javatris1.png");
//		ImageIcon startButtonImage = null;
//		try {
//			startButtonImage = new ImageIcon(ImageIO.read(new File("src/images/tiles.png")));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		JButton startButton = new JButton("PLAY");
		JButton onlineButton = new JButton("ONLINE");
		JButton loadButton = new JButton("LOAD");
		JButton exitButton = new JButton("EXIT");
		
		startButton.setFont(new Font("Arial", Font.BOLD, 40));
		startButton.addActionListener((ActionEvent e) -> {startGame();});
		
		onlineButton.setFont(new Font("Arial", Font.BOLD, 40));
//		onlineButton.addActionListener((ActionEvent e) -> {startOnlineGame();});
		
		exitButton.setFont(new Font("Arial", Font.BOLD, 40));
		exitButton.addActionListener((ActionEvent e) -> {System.exit(0);});
		
		loadButton.setFont(new Font("Arial", Font.BOLD, 40));
		loadButton.addActionListener((ActionEvent e) -> {});
		
		startMenu.addElement(startButton); 
		startMenu.addElement(onlineButton); 
		startMenu.addElement(loadButton);
		startMenu.addElement(exitButton);
		startMenu.openMenu();
	}
	
	

	/*
	 * Creates a pause Menu
	 */
	private void makePauseMenu()
	{
		
		int menuWidth = 300;
		int menuHeigth = 400;
		
		pauseMenu = new Menu(FixedPanel,new Dimension(menuWidth,menuHeigth),5);
 		JButton resumeButton = new JButton("Resume");
 		JButton saveButton = new JButton("Save");
 		JButton exitButton = new JButton("Main Menu");
 		
 		int tesxtSize = menuWidth/10;
 		resumeButton.setFont(new Font("Arial", Font.BOLD, tesxtSize));
 		resumeButton.addActionListener((ActionEvent e) -> {resumeGame();});
 		
 		saveButton.setFont(new Font("Arial", Font.BOLD, tesxtSize));
 		saveButton.addActionListener((ActionEvent e) -> {saveGame();});
 		
 		exitButton.setFont(new Font("Arial", Font.BOLD, tesxtSize));
 		exitButton.addActionListener((ActionEvent e) -> {toMainMenu();});
 		
 		pauseMenu.addElement(resumeButton); 
 		pauseMenu.addElement(saveButton); 
 		pauseMenu.addElement(exitButton); 
		
 		pauseMenu.pack();	
 	
         
        
		
		
	}
	
	
	
	/**
	 * Starts game
	 */
	
	
	private void startGame() {
		startMenu.closeMenu();
		
//		FixedPanel.remove(startMenu);
		FixedPanel.add(gamePanel);
		FixedPanel.validate();
		FixedPanel.repaint();
		frame.add(FixedPanel);
		
		if(firstGame) {
			musicPlayer.play();
			gameEngine.start();
		}else {
		
			musicPlayer.restart();
			gameEngine.restart();
		}
		
		
		
		
	}
	

	/*
	 * Resumes the paused game 
	 */
	private void resumeGame() 
	{
		musicPlayer.play();
		gameEngine.resume();
		pauseMenu.close();
		
	}
	
	/*
	 * Exits the current game and goes to the main menu
	 */
	private void toMainMenu() 
	{	
		pauseMenu.close();
		FixedPanel.remove(gamePanel);
		firstGame = false;
		FixedPanel.validate();
		FixedPanel.repaint();
		
		FixedPanel.add(startMenu);
		startMenu.openMenu();
		System.out.println("exit pause menu");
		
	}
	
	/*
	 * Saves the current game to a file
	 */
	private void saveGame() 
	{
		Init();
		SetUpFrame();
		//load the game here
		startMenu.closeMenu();
	}
	
	/*
	 * Loads a saved game state and starts the game
	 */
	private void loadsGame() 
	{
		
	}
	
	
	/*
	 * Starts a multiplayer Session of the game, opens up a prompt so the user can enter the ip and port to the server
	 * It should be inputed like this: 127.0.0.1:2525
	 * in other words ip:port
	 */
//	private void startOnlineGame() 
//	{
////		String code = JOptionPane.showInputDialog
////		(
////	        startMenu.getFrame(), 
////	        "Enter the sever ip and port", 
////	        "Connect to server", 
////	        JOptionPane.PLAIN_MESSAGE
////		 );
//		
//		//Only starts if
//		if(code != null&&!code.isBlank() && !code.isEmpty()) 
//		{
//			String[] adress = code.split(":");
//			Init(adress[0], Integer.parseInt(adress[1]));
//			SetUpFrame();
//			startMenu.closeMenu();
//		}
//	}
//	
	
}
