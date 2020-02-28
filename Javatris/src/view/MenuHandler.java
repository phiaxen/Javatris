package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.Game;
import model.MusicPlayer;

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
	private DialogMenu optionsMenu;
	private MusicPlayer musicPlayer;
	
	public MenuHandler(Game game,JFrame frame, JPanel fixedPanel,MusicPlayer musicPlayer) {
		this.game = game;
		this.frame = frame;
		this.fixedPanel = fixedPanel;
		this.musicPlayer = musicPlayer;
		loadMenus();
	}
	
	/**
	 * Creates menus for initial start of game
	 */
	private void loadMenus() {
		makeStartMenu();
		makeCreditsMenu();
		makePauseMenu();
		makeOptionsMenu();
		makeGameOverMenu();
		makeVictoryMenu();
	}
	
	/**
	 * Creates the main menu
	 */
	private void makeStartMenu() {
		
		startMenu = new Menu(new Dimension(700,820),0,5,0.25f,0.75f,Color.BLACK);
		startMenu.addTitle("/images/javatris1.png");

		JButton playButton = new JButton("PLAY");
		JButton onlineButton = new JButton("ONLINE");
		JButton loadButton = new JButton("LOAD");
		JButton exitButton = new JButton("EXIT");
		JButton credits = new JButton("CREDITS");

		//play
		formatButton(playButton, Color.WHITE, Font.BOLD, 90);
		playButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.startGame();
		});
		
		//online
		formatButton(onlineButton, Color.WHITE, Font.BOLD, 50);
		onlineButton.addActionListener((ActionEvent e) -> {
			game.startOnlineGame();
		});
		
		//load
		formatButton(loadButton, Color.WHITE, Font.BOLD, 50);
		loadButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.startGame();
			game.loadGame();
		});
		
		//exit
		formatButton(exitButton, Color.WHITE, Font.BOLD, 40);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		//credits
		formatButton(credits, Color.GRAY, Font.BOLD, 20);
		credits.addActionListener((ActionEvent e) -> {
			startMenu.close();
			openCreditsMenu();
		});
		
		
		startMenu.addElementBot(playButton); 
		startMenu.addElementBot(onlineButton); 
		startMenu.addElementBot(loadButton);
		startMenu.addElementBot(exitButton);
		startMenu.addElementBot(credits);
		fixedPanel.add(startMenu);
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
	 * Closes the main menu
	 */
	public void closeStartMenu() {
		startMenu.close();
	}
	
	/*
	 * Makes the pause menu
	 */
	private void makePauseMenu(){
		int menuWidth = 200;
		int menuHeigth = 250;
		int textSize = menuWidth/10;
		
		pauseMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),4,5,"PAUSE",40);
 		JButton resumeButton = new JButton("Resume");
 		JButton optionsButton = new JButton("Options");
 		JButton saveButton = new JButton("Save");
 		JButton mainMenuButton = new JButton("Main Menu");
 		
 		//resume
 		formatButton(resumeButton, Color.WHITE, Font.BOLD, textSize);
 		resumeButton.addActionListener((ActionEvent e) -> {
 			pauseMenu.close();
 			game.resumeGame();
 		});
 		
 		//options
 		formatButton(optionsButton, Color.WHITE, Font.BOLD, textSize);
 		optionsButton.addActionListener((ActionEvent e) -> {
 			pauseMenu.close();
 			optionsMenu.open();
 		});
 		
 		//save
 		formatButton(saveButton, Color.WHITE, Font.BOLD, textSize);
 		saveButton.addActionListener((ActionEvent e) -> {
 			game.saveGame();
 		});
 		
 		//main menu
 		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, textSize);
 		mainMenuButton.addActionListener((ActionEvent e) -> {
 			toMainMenu();
 			pauseMenu.close();
 		});
 		
 		pauseMenu.addElement(resumeButton); 
 		pauseMenu.addElement(optionsButton); 
 		pauseMenu.addElement(saveButton); 
 		pauseMenu.addElement(mainMenuButton); 
	}
	
	/**
	 * Opens the pause menu
	 */
	public void openPauseMenu() {
		pauseMenu.open();
	}
	
	/**
	 * Closes the pause menu
	 */
	public void closePauseMenu() {
		pauseMenu.close();
	}
	
	/**
	 * Makes the options menu
	 */
	public void makeOptionsMenu(){
		int menuWidth = 250;
		int menuHeigth = 250;
		int textSize = menuWidth/10;
		
		optionsMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),3,5,"OPTIONS",40);
 		
		JPanel fullScreenPanel = new JPanel();
		JLabel fullScreenLabel = new JLabel("Full Screen");
		JButton fullScreenButton = new JButton("Off");
		
		JPanel volumePanel = new JPanel(new BorderLayout());
		JLabel sliderLabel = new JLabel("Music Volume",SwingConstants.CENTER);
		JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 8);
		
 		JButton backButton = new JButton("Back");

 		//full screen panel 
 		fullScreenPanel.setBackground(Color.BLACK);
 		
 		//full screen button 
 		fullScreenButton.setFont(new Font("Arial", Font.BOLD, 20));
 		fullScreenButton.setForeground(Color.RED);
 		fullScreenButton.setBackground(Color.BLACK);
 		fullScreenButton.setBorderPainted(false);
 		fullScreenButton.setFocusPainted(false); 
 		
 		
 		//full screen label
 		fullScreenLabel.setFont(new Font("Arial", Font.BOLD, 20));
 		fullScreenLabel.setForeground(Color.WHITE);
 		fullScreenLabel.setBackground(Color.BLACK);
 	
 		
 		//slider label
 		sliderLabel.setFont(new Font("Arial", Font.BOLD, 20));
 		sliderLabel.setForeground(Color.WHITE);
 		sliderLabel.setBackground(Color.BLACK);
 		
 		//volume slider
 	
 		volumeSlider.setPaintLabels(false);
 		volumeSlider.setSnapToTicks(true);
 		volumeSlider.setBackground(Color.BLACK);
 		
 		//volume panel
 		volumePanel.setBackground(Color.BLACK);
 		volumePanel.add(sliderLabel,BorderLayout.NORTH);
 		volumePanel.add(volumeSlider,BorderLayout.SOUTH);
 		
 		volumeSlider.addChangeListener((ChangeEvent e) -> {
 			float sliderValue = volumeSlider.getValue();
 			musicPlayer.setVolume(sliderValue/20);
 		});
 	
 		fullScreenButton.addActionListener((ActionEvent e) -> {
 			
 			if(frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
 				
 				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
 	 			frame.dispose();
 	 			frame.setUndecorated(true);
 	 			fullScreenButton.setText("ON");
 	 			fullScreenButton.setForeground(Color.GREEN);
 			}else {
 				frame.setExtendedState(JFrame.NORMAL);
 				frame.setSize(715,860);
 				frame.setLocationRelativeTo(null);
 	 			frame.dispose();
 	 			frame.setUndecorated(false);
 	 			fullScreenButton.setText("OFF");
 	 			fullScreenButton.setForeground(Color.RED);
 			}
 			frame.setVisible(true);
 			optionsMenu.open();
 		});
 		
 		fullScreenPanel.add(fullScreenLabel,BorderLayout.WEST);
 		fullScreenPanel.add(fullScreenButton,BorderLayout.EAST);
 		
 		formatButton(backButton, Color.WHITE, Font.BOLD, textSize);
 		backButton.addActionListener((ActionEvent e) -> {
 			optionsMenu.close();
 			pauseMenu.open();
 		});
 		
 		optionsMenu.addElement(fullScreenPanel);
 		optionsMenu.addElement(volumePanel); 
 		optionsMenu.addElement(backButton); 
	}
	
	/**
	 * Makes the game over menu
	 */
	private void makeGameOverMenu() {
		int menuWidth = 300;
		int menuHeigth = 200;
		int textSize = 20;
		
		gameOverMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),2,5,"GAME OVER",40);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");
		
		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, textSize);
		mainMenuButton.addActionListener((ActionEvent e) -> {
			gameOverMenu.close();
 			toMainMenu();
 		});
		
		formatButton(exitButton, Color.WHITE, Font.BOLD, textSize);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
 		gameOverMenu.addElement(mainMenuButton); 
 		gameOverMenu.addElement(exitButton); 
	}
	
	
	
	/**
	 * Opens the Game Over menu
	 */
	public void openGameOverMenu() {
		gameOverMenu.open();
	}
	
	/**
	 * Makes the game over victory menu
	 */
	private void makeVictoryMenu() {
		int menuWidth = 300;
		int menuHeigth = 200;
		int textSize = 20;
		
		winMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),2,5,"YOU WIN",40);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");
		
		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, textSize);
		mainMenuButton.addActionListener((ActionEvent e) -> {
			winMenu.close();
 			toMainMenu();
 		});
		
		formatButton(exitButton, Color.WHITE, Font.BOLD, textSize);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		winMenu.addElement(mainMenuButton); 
		winMenu.addElement(exitButton); 
	}
	
	/**
	 * Opens the Game Over victory menu
	 */
	public void openVictoryMenu() {
		winMenu.open();
	}

	
	/**
	 * Creates the credits screen
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
			creditsMenu.close();
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
	 * Opens credits menu
	 */
	private void openCreditsMenu() {
		creditsMenu.open();
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

