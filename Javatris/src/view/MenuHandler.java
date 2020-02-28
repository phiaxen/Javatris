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
 * @author Joachim Antfolk
 * @since 2020-02-27
 * @version 1.0
 */
public class MenuHandler {

	private final Game game;
	private final JFrame frame;
	private final JPanel fixedPanel;
	private Menu startMenu;
	private Menu creditsMenu;
	private DialogMenu pauseMenu;
	private DialogMenu gameOverMenu;
	private DialogMenu winMenu;
	
	public MenuHandler(Game game,JFrame frame, JPanel fixedPanel) {
		this.game = game;
		this.frame = frame;
		this.fixedPanel = fixedPanel;
		loadMenus();
	}
	
	/**
	 * Creates menus for initial start of game
	 */
	private void loadMenus() {
		makeStartMenu();
		makeCreditsMenu();
	}
	
	/**
	 * Creates the main menu
	 */
	private void makeStartMenu() {
		
		startMenu = new Menu(new Dimension(700,820),0,5,0.25f,0.75f,Color.BLACK);
		startMenu.addTitle("/images/javatris1.png");

		JButton startButton = new JButton("PLAY");
		JButton onlineButton = new JButton("ONLINE");
		JButton loadButton = new JButton("LOAD");
		JButton exitButton = new JButton("EXIT");
		JButton credits = new JButton("CREDITS");

		
		String font = "Arial";
		formatButton(startButton, Color.WHITE, Font.BOLD, 90);
		startButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.startGame();
		});
		
		formatButton(onlineButton, Color.WHITE, Font.BOLD, 50);
		onlineButton.addActionListener((ActionEvent e) -> {
			game.startOnlineGame();
		});
		
		formatButton(loadButton, Color.WHITE, Font.BOLD, 50);
		loadButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.startGame();
			game.loadGame();
		});
		
		formatButton(exitButton, Color.WHITE, Font.BOLD, 40);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		formatButton(credits, Color.GRAY, Font.BOLD, 20);
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
	
	/**
	 * Closes the main menu
	 */
	public void closeStartMenu() {
		startMenu.close();
	}
	
	/*
	 * Opens the main menu
	 */
	public void openStartMenu() {
		startMenu.open();
		fixedPanel.validate();
		fixedPanel.repaint();
	}
	
	/**
	 * Closes the pause menu
	 */
	public void closePauseMenu() {
		pauseMenu.close();
	}
	
	/*
	 * Opens the pause menu
	 */
	public void openPauseMenu(){
		int menuWidth = 200;
		int menuHeigth = 250;
		int textSize = menuWidth/10;
		
		pauseMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),3,5,textSize, "PAUSE",40);
 		JButton resumeButton = new JButton("Resume");
 		JButton saveButton = new JButton("Save");
 		JButton mainMenuButton = new JButton("Main Menu");
 		
 		formatButton(resumeButton, Color.WHITE, Font.BOLD, 30);
 		resumeButton.addActionListener((ActionEvent e) -> {
 			pauseMenu.close();
 			game.resumeGame();
 		});
 		
 		formatButton(saveButton, Color.WHITE, Font.BOLD, 30);
 		saveButton.addActionListener((ActionEvent e) -> {
 			game.saveGame();
 		});
 		
 		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, 30);
 		mainMenuButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 		});
 		
 		pauseMenu.addButton(resumeButton); 
 		pauseMenu.addButton(saveButton); 
 		pauseMenu.addButton(mainMenuButton); 
 		pauseMenu.pack();	
 	
	}
	
	
	/**
	 * Opens the Game Over menu
	 */
	public void openGameOverMenu() {
		int menuWidth = 300;
		int menuHeigth = 200;
		int textSize = 20;
		
		gameOverMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),2,5,textSize,"GAME OVER",40);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");
		
		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, 30);
		mainMenuButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 		});
		
		formatButton(exitButton, Color.WHITE, Font.BOLD, 30);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
 		gameOverMenu.addButton(mainMenuButton); 
 		gameOverMenu.addButton(exitButton); 
 		gameOverMenu.pack();	
	}
	
	/**
	 * Opens the Game Over menu
	 */
	public void openVictoryMenu() {
		int menuWidth = 300;
		int menuHeigth = 200;
		int textSize = 20;
		
		winMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),2,5,textSize,"YOU WIN!",40);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");
		
		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, 30);
		mainMenuButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 		});
		
		formatButton(exitButton, Color.WHITE, Font.BOLD, 30);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		winMenu.addButton(mainMenuButton); 
		winMenu.addButton(exitButton); 
		winMenu.pack();	
	}
	
	/**
	 * Creates the Credits screen
	 */
	private void makeCreditsMenu() {
		creditsMenu = new Menu(new Dimension(700,820),2,1,0.8f,0.2f,Color.BLACK);
		
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
	
	
	/**
	 * Opens the dialog to get server info
	 * @return : server IP and port
	 */
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
	
	/**
	 * Returns to the main menu
	 */
	private void toMainMenu() {
		if(pauseMenu !=null)
			pauseMenu.close();
		if(creditsMenu !=null)
			creditsMenu.close();
		if(gameOverMenu !=null)
			gameOverMenu.close();
		if(winMenu !=null)
			winMenu.close();
		
		game.removeGamePanel();
		game.setOffline();
		startMenu.open();
	}
	
	/**
	 * Formats button
	 * @param button : button to be formated
	 * @param foreground : foreground color
	 * @param fontType : font type bold, italic, etc.
	 * @param fontSize : size of the font
	 */
	private void formatButton(JButton button, Color foreground,int fontType, int fontSize) {
		String font = "Arial";
		
		button.setFont(new Font(font, fontType, fontSize));
		button.setForeground(foreground);
		button.setBackground(Color.BLACK);
		button.setBorderPainted(false);
		button.setFocusPainted(false); 
	}

}

