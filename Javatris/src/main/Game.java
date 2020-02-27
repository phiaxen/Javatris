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
	private JPanel gamePanel;
	private boolean firstGame = true;

	private MenuHandler menuHandler;
	
	public Game(JFrame frame) {
		this.frame = frame;
		
//		ResizeFrame();
		init();
		SetUpFrame();
		menuHandler = new MenuHandler(this, frame, FixedPanel);
		menuHandler.openStartMenu();
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
		controller = new Controller(gameEngine, musicPlayer);
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
				openPauseMenu();
			}

			@Override
			public void gameOver() {
				musicPlayer.stop();
				openGameOverMenu();
			}
		};
	}
	
	/*
	 * Starts a client and connects to the server for an online session.
	 * @Param ip the ip of the server
	 * @Param port the port of the server
	 */
	private void initClient(String ip, int port) {
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
		
		frame.add(FixedPanel);
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//		frame.setUndecorated(true);
		
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		frame.setLocationRelativeTo(null); //set frame in the middle of the screen
		
		frame.setVisible(true);

	}
	
	private void openPauseMenu() {
		menuHandler.openPauseMenu();
	}
	
	private void openGameOverMenu() {
		menuHandler.openGameOverMenu();
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
	
	
	/**
	 * Starts game
	 */
	public void startGame() {		
		gameEngine.addPropertyChangeListener(boardView);
		gameEngine.addPropertyChangeListener(sideInfo);
	
		FixedPanel.add(gamePanel);
		FixedPanel.validate();
		FixedPanel.repaint();
		//frame.add(FixedPanel);
		
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
	
	public void loadGame() 
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
		gameEngine.fireGameField();
		gamePanel.validate();
		gamePanel.repaint();
	}
	
	public void saveGame() 
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
	public void startOnlineGame() {
		String code = menuHandler.showOnlineDialog();
		
		//Only starts if
		if(code != null&&!code.isBlank() && !code.isEmpty()) 
		{
			String[] adress = code.split(":");
			
			initClient(adress[0], Integer.parseInt(adress[1]));
			gameEngine.setOnline(true);
			startGame();
		}

	}
	
	/*
	 * Resumes the paused game 
	 */
	public void resumeGame() {
		musicPlayer.play();
		gameEngine.resume();	
	}
	
	/*
	 * Exits the current game and goes to the main menu
	 */
	public void removeGamePanel() {	
		FixedPanel.remove(gamePanel);
		FixedPanel.validate();
		FixedPanel.repaint();
	}
	
	/**
	 * Returns the game Engine to offline state
	 */
	public void setOffline(){
		gameEngine.setOnline(false);
	}
	
}
