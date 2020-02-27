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

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
	
	private final  int BLOCKSIZE = 40; //resize game <=40 (standard is 40)
	private final  int BOARDWIDTH = 10;
	private final  int BOARDHEIGHT = 20;
	
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
	private Menu startMenu;
	private Dialog pauseMenu;
	private Dialog gameOverMenu;
	private JPanel gamePanel;
	private boolean firstGame = true;
	private Menu creditsMenu;

	
	public Game(JFrame frame) {
		this.frame = frame;
//		ResizeFrame();
		init();
		makeStartMenu();
		
		SetUpFrame();
	}
	
	public static void main(String[] args) {
		new Game(new JFrame("JavaTris"));
	}
	
	
	/*
	 * Initializes the game and creates all the nececcary components that are needed
	 */
	private void init() {
		musicPlayer = new MusicPlayer(2);
		
		board = new Board();
		boardView = new BoardView(BOARDHEIGHT, BOARDWIDTH, BLOCKSIZE, true);
		gameEngine = new GameEngine(board, false);
		controller = new Controller(gameEngine, musicPlayer, pauseMenu);
		sideInfo = new SideInfo();
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(controller);
		
		gameEngine.delegate = new GameEngine.Delegate() 
		{
			@Override
			public Client getClient()
			{
				return client.getClient();
			}
			
			@Override
			public void pause()
			{
				musicPlayer.stop();
				makePauseMenu();
			}

			@Override
			public void resume() {
				//resumeGame();
				closePauseMenu();
			}
			
			@Override
			public void gameOver() {
				musicPlayer.stop();
				makeGameOverMenu();
			}
		};
	}
	
	/*
	 * Initializes the game just like the other Init but also starts a client and connects to the server for an online session.
	 * @Param ip the ip of the server
	 * @Param port the port of the server
	 */
	private void init(String ip, int port) {
		client = new Client(gameEngine, ip, port);			
	}
	
	/**
	 * 
	 */
	public void SetUpFrame() {	
//		frame.setSize(FrameWidth + 300,FrameHeight);	//add 300 on width if sideInfo is included
		frame.setSize(715,860);
		
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridBagLayout());
		gamePanel.setPreferredSize(new Dimension(684,804));
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
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
		return BOARDWIDTH;
	}
	public int getBoardHeight() {
		return BOARDHEIGHT; 
	}
	public int getBlockSize() {
		return BLOCKSIZE;
	}
	
	
	private void makeStartMenu() {
		
		startMenu = new Menu(new Dimension(700,820),0,5,0.25f,0.75f,Color.BLACK);
		startMenu.addTitle("/images/javatris1.png");

		JButton startButton = new JButton("PLAY");
		JButton onlineButton = new JButton("ONLINE");
		JButton loadButton = new JButton("LOAD");
		JButton exitButton = new JButton("EXIT");
		JButton credits = new JButton("CREDITS");

		
		String font = "Arial";
		startButton.setFont(new Font(font, Font.BOLD, 90));
		startButton.setForeground(Color.WHITE);
		startButton.setBackground(Color.BLACK);
		startButton.setBorderPainted(false);
		startButton.setFocusPainted(false); 
		startButton.addActionListener((ActionEvent e) -> {
			startGame();
		});
		
		
		onlineButton.setFont(new Font(font, Font.BOLD, 50));
		onlineButton.setForeground(Color.WHITE);
		onlineButton.setBackground(Color.BLACK);
		onlineButton.setBorderPainted(false);
		onlineButton.setFocusPainted(false); 
		onlineButton.addActionListener((ActionEvent e) -> {
			startOnlineGame();
		});
		
		loadButton.setFont(new Font(font, Font.BOLD, 50));
		loadButton.setForeground(Color.WHITE);
		loadButton.setBackground(Color.BLACK);
		loadButton.setBorderPainted(false);
		loadButton.setFocusPainted(false); 
		loadButton.addActionListener((ActionEvent e) -> {
			startGame();
			loadGame();
			gameEngine.fireGameField();
			gamePanel.validate();
			gamePanel.repaint();
		});
		
		
		exitButton.setFont(new Font(font, Font.BOLD, 40));
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(Color.BLACK);
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false); 
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		
		
		credits.setFont(new Font(font, Font.ITALIC, 20));
		credits.setForeground(Color.GRAY);
		credits.setBackground(Color.BLACK);
		credits.setBorderPainted(false);
		credits.setFocusPainted(false); 
		credits.addActionListener((ActionEvent e) -> {
			rollCredits();
		});
		
		
		startMenu.addElementBot(startButton); 
		startMenu.addElementBot(onlineButton); 
		startMenu.addElementBot(loadButton);
		startMenu.addElementBot(exitButton);
		startMenu.addElementBot(credits);
		startMenu.openMenu();
	}

	/**
	 * Rolls Credits
	 */
	private void rollCredits() {
		startMenu.closeMenu();
		makeCreditsMenu();
		FixedPanel.add(creditsMenu);
	}
	/**
	 * Makes the Credits screen
	 */
	private void makeCreditsMenu() {
		creditsMenu = new Menu(new Dimension(700,820),2,1,0.8f,0.2f,Color.GREEN);
		
		JButton back = new JButton("Back");
		back.setFont(new Font("Arial", Font.BOLD, 20));
		back.setForeground(Color.WHITE);
		back.setBackground(Color.BLACK);
		back.setBorderPainted(false);
		back.setFocusPainted(false); 
		back.addActionListener((ActionEvent e) -> {
			toMainMenu();
		});
		
		JLabel text1 = new JLabel("CREATED BY",SwingConstants.CENTER);
		text1.setForeground(Color.white);
		text1.setFont(new Font("Arial", Font.BOLD, 50));
	
		
		JTextPane textArea = new JTextPane();
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
	
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		textArea.setFont(new Font("Arial", Font.PLAIN, 30));
		textArea.setText("Joachim Antfolk\r\n" + 
				"\r\n" + 
				"Philip Axenhamn\r\n" + 
				"\r\n" + 
				"Andreas Greppe\r\n" + 
				"\r\n" + 
				"Tobias Mauritzon\r\n" + 
				"\r\n");
		textArea.setEditable(false);
		creditsMenu.addElementTop(text1);
		creditsMenu.addElementTop(textArea);
		creditsMenu.addElementBot(back); 
		creditsMenu.openMenu();
	}
	
	/*
	 * Creates a pause Menu
	 */
	private void makePauseMenu()
	{
		int menuWidth = 200;
		int menuHeigth = 250;
		int textSize = menuWidth/10;
		
		pauseMenu = new Dialog(FixedPanel,new Dimension(menuWidth,menuHeigth),3,5,textSize, "PAUSE",40);
 		JButton resumeButton = new JButton("Resume");
 		JButton saveButton = new JButton("Save");
 		JButton exitButton = new JButton("Main Menu");
 		
 		
 		resumeButton.addActionListener((ActionEvent e) -> {
 			resumeGame();
 		});
 		
 		saveButton.addActionListener((ActionEvent e) -> {
 			saveGame();
 		});
 		
 		exitButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 		});
 		
 		pauseMenu.addButton(resumeButton); 
 		pauseMenu.addButton(saveButton); 
 		pauseMenu.addButton(exitButton); 
		
 		pauseMenu.pack();	
 	
	}
	
	private void makeGameOverMenu() {
		int menuWidth = 300;
		int menuHeigth = 200;
		int textSize = 20;
		
		gameOverMenu = new Dialog(FixedPanel,new Dimension(menuWidth,menuHeigth),2,5,textSize,"GAME OVER",40);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");
		
		
		mainMenuButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 		});
		
	
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
 		gameOverMenu.addButton(mainMenuButton); 
 		gameOverMenu.addButton(exitButton); 
 		gameOverMenu.pack();	
	}
	
	/**
	 * Starts game
	 */
	private void startGame() {		
		gameEngine.addPropertyChangeListener(boardView);
		gameEngine.addPropertyChangeListener(sideInfo);
		startMenu.closeMenu();
	
		FixedPanel.add(gamePanel);
		FixedPanel.validate();
		FixedPanel.repaint();
		frame.add(FixedPanel);
		
		if(firstGame) {
			musicPlayer.play();
			gameEngine.start();
		}
		else {
			musicPlayer.restart();
			gameEngine.restart();
		}
		firstGame = false;	
	}
	
	private void loadGame() 
	{
		try {
			Savedata loadData = (Savedata) SaveManager.loadFile("saveFile.Save");
			board.setBoard(loadData.getBoard());
			gameEngine.setCurrentShape(loadData.getCurrentShape());
			gameEngine.setNextShapes(loadData.getNextShapes());
			gameEngine.setScore(loadData.getScore());
			gameEngine.setLevel(loadData.getLevel());
			gameEngine.setTime(loadData.getTime());
			gameEngine.setClearedRows(loadData.getRemovedRows());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveGame() 
	{
		Savedata saveData = new Savedata();
		saveData.setBoard(board.getBoard());
		saveData.setCurrentShape(gameEngine.getCurrentShape());
		saveData.setNextShapes(gameEngine.GetNextShapes());
		saveData.setScore(gameEngine.getPoints());
		saveData.setLevel(gameEngine.getLevel());
		saveData.setRemovedRows(gameEngine.getRemovedRows());
		saveData.setTime(gameEngine.getTime());
		
		try 
		{
			SaveManager.saveFile(saveData, "saveFile.Save");
		}
		catch(IIOException e) {} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Starts a multiplayer Session of the game, opens up a prompt so the user can enter the ip and port to the server
	 * It should be inputed like this: 127.0.0.1:2525
	 * in other words ip:port
	 */
	private void startOnlineGame() {
		String code = JOptionPane.showInputDialog
		(
	        frame, 
	        "Enter the server ip and port, (IP:PORT)", 
	        "Connect to server", 
	        JOptionPane.PLAIN_MESSAGE
		 );
		
		//Only starts if
		if(code != null&&!code.isBlank() && !code.isEmpty()) 
		{
			String[] adress = code.split(":");
			
			init(adress[0], Integer.parseInt(adress[1]));
			
			startGame();
		}

	}
	
	/*
	 * Resumes the paused game 
	 */
	private void resumeGame() 
	{
		gameEngine.resume();	
	}
	private void closePauseMenu() {
		musicPlayer.play();
		if(pauseMenu !=null)
		pauseMenu.close();
	}
	
	/*
	 * Exits the current game and goes to the main menu
	 */
	private void toMainMenu() 
	{	
		if(pauseMenu !=null)
			pauseMenu.close();
		if(creditsMenu !=null)
			creditsMenu.closeMenu();
		if(gameOverMenu !=null)
			gameOverMenu.close();
		
		FixedPanel.remove(gamePanel);
		
		FixedPanel.validate();
		FixedPanel.repaint();
		
		FixedPanel.add(startMenu);
		startMenu.openMenu();
		System.out.println("exit pause menu");
		
	}
	
}
