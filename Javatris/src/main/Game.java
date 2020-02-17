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
	
	private void Init() {
		board = Board.getInstance();
		boardView = new BoardView(board,BoardHeight,BoardWidth,BlockSize,false);
		sideInfo = new SideInfo();
		gameEngine = new GameEngine(board, boardView,sideInfo);
		controller = new Controller(gameEngine);
		gameEngine.start();
		
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
		startMenu = new Menu(new Dimension(200,300), 2);
		JButton startButton = new JButton("START");
		JButton exitButton = new JButton("EXIT");
		
		startButton.addActionListener((ActionEvent e) -> {startGame();});
		exitButton.addActionListener((ActionEvent e) -> {System.exit(0);});
		
		startMenu.addElement(startButton);
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
	
	
}
