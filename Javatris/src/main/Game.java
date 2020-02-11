package main;
import server.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
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
 */

public class Game {
	
	private final  int BlockSize = 40;
	private final  int BoardWidth = 10;
	private final  int BoardHeight = 20;
	
	private int FrameWidth = 0;
	private int FrameHeight = 0;
	

	private BoardView boardView;
	private Board board;
	private GameEngine gameEngine; 
	private SideInfo sideInfo;
	
	//Temporary solution
	public void ResizeFrame() {
		FrameWidth = BlockSize * BoardWidth + 14;
		FrameHeight = BlockSize * BoardHeight + 37;
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
	public Game() {
	
		
		board = new Board(BoardHeight,BoardWidth);
		boardView = new BoardView(board,BoardHeight,BoardWidth,BlockSize,false);
		sideInfo = new SideInfo();
		gameEngine = new GameEngine(board, boardView,sideInfo);
		gameEngine.start();
		
		
		Controller controller = new Controller();
		ResizeFrame();
		
		JFrame frame = new JFrame("Tetris");
		frame.setSize(FrameWidth,FrameHeight);	
				
		boardView.setLayout(null);
       

//		JPanel border = new JPanel(new GridBagLayout());
		JPanel FixedPanel = new JPanel(new GridBagLayout());
		FixedPanel.setPreferredSize(frame.getSize());
		FixedPanel.setBackground(Color.WHITE);
//		border.setPreferredSize(new Dimension(440,840));
//		border.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,20));
		boardView.setPreferredSize(new Dimension(400,800));
		boardView.setBorder(new EmptyBorder(10,10,10,10));
//		border.add(boardView);
		FixedPanel.add(sideInfo);
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
	
	
}
