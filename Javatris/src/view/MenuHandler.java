package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.Game;

/**
 * MenuHandler handles every menu in the game
 * @author Philip Axenhamn 
 * @since 2020-02-27
 * @version 1.0
 */
public class MenuHandler {

	private final Game game;
	private final JFrame frame;
	private final JPanel fixedPanel;
	private Menu startMenu;
	private Menu creditsMenu;
	private Dialog pauseMenu;
	private Dialog gameOverMenu;
	
	public MenuHandler(Game game,JFrame frame, JPanel fixedPanel) {
		this.game = game;
		this.frame = frame;
		this.fixedPanel = fixedPanel;
		loadMenus();
	}
	
	private void loadMenus() {
		makeStartMenu();
		makeCreditsMenu();
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
			startMenu.close();
			game.startGame();
		});
		
		
		onlineButton.setFont(new Font(font, Font.BOLD, 50));
		onlineButton.setForeground(Color.WHITE);
		onlineButton.setBackground(Color.BLACK);
		onlineButton.setBorderPainted(false);
		onlineButton.setFocusPainted(false); 
		onlineButton.addActionListener((ActionEvent e) -> {
			game.startOnlineGame();
		});
		
		loadButton.setFont(new Font(font, Font.BOLD, 50));
		loadButton.setForeground(Color.WHITE);
		loadButton.setBackground(Color.BLACK);
		loadButton.setBorderPainted(false);
		loadButton.setFocusPainted(false); 
		loadButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.startGame();
			game.loadGame();
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
		fixedPanel.add(startMenu);
	}
	
	/**
	 * Rolls Credits
	 */
	private void rollCredits() {
		startMenu.close();
		creditsMenu.open();
	}
	
	public void openStartMenu() {
		startMenu.open();
		fixedPanel.validate();
		fixedPanel.repaint();
	}
	
	public void closePauseMenu() {
		pauseMenu.close();
	}
	public void openPauseMenu(){
		int menuWidth = 200;
		int menuHeigth = 250;
		int textSize = menuWidth/10;
		
		pauseMenu = new Dialog(fixedPanel,new Dimension(menuWidth,menuHeigth),3,5,textSize, "PAUSE",40);
 		JButton resumeButton = new JButton("Resume");
 		JButton saveButton = new JButton("Save");
 		JButton mainMenuButton = new JButton("Main Menu");
 		
 		
 		resumeButton.addActionListener((ActionEvent e) -> {
 			pauseMenu.close();
 			game.resumeGame();
 		});
 		
 		saveButton.addActionListener((ActionEvent e) -> {
 			game.saveGame();
 		});
 		
 		mainMenuButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 		});
 		
 		pauseMenu.addButton(resumeButton); 
 		pauseMenu.addButton(saveButton); 
 		pauseMenu.addButton(mainMenuButton); 
 		pauseMenu.pack();	
 	
	}
	
	
	
	public void openGameOverMenu() {
		int menuWidth = 300;
		int menuHeigth = 200;
		int textSize = 20;
		
		gameOverMenu = new Dialog(fixedPanel,new Dimension(menuWidth,menuHeigth),2,5,textSize,"GAME OVER",40);
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
		creditsMenu.close();
		fixedPanel.add(creditsMenu);
	}
	
	
	
	public String showOnlineDialog() {
		String code = JOptionPane.showInputDialog
				(
			        frame, 
			        "Enter the server ip and port, (IP:PORT)", 
			        "Connect to server", 
			        JOptionPane.PLAIN_MESSAGE
				 );
		return code;
	}
	
	private void toMainMenu() {
		if(pauseMenu !=null)
			pauseMenu.close();
		if(creditsMenu !=null)
			creditsMenu.close();
		if(gameOverMenu !=null)
			gameOverMenu.close();
		
		game.removeGamePanel();
		game.setOffline();
		startMenu.open();
	}

}

