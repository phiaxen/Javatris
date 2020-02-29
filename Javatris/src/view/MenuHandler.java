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
	private DialogMenu[] basicMenus = new DialogMenu[4];
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
	 * Creates menus for initial start of game a
	 */
	private void loadMenus() {
		makeStartMenu();
		makeCreditsMenu();
		makePauseMenu();
		makeOptionsMenu();
		makeBasicMenu("GAME OVER",0);
		makeBasicMenu("YOU LOOSE!",1);
		makeBasicMenu("YOU WIN!",2);
		makeBasicMenu("Connection Lost",3);
		
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
	
	private void musiceButtonsCSS(JButton b, Color c) {
		b.setFont(new Font("Arial", Font.BOLD, 15));
 		b.setForeground(c);
 		b.setBackground(Color.BLACK);
 		b.setBorderPainted(false);
 		b.setFocusPainted(false);
	}
	
	/**
	 * Makes the options menu
	 */
	public void makeOptionsMenu(){
		int menuWidth = 350;  	//250
		int menuHeigth = 400;	//250
		int textSize = menuWidth/10;
		
		optionsMenu = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),5,5,"OPTIONS",40);
 		
		JPanel fullScreenPanel = new JPanel();
		JLabel fullScreenLabel = new JLabel("Full Screen");
		JButton fullScreenButton = new JButton("Off");
		
		JPanel volumePanel = new JPanel(new BorderLayout());
		JLabel sliderLabel = new JLabel("Music Volume",SwingConstants.CENTER);
		JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 8);
		
		JPanel musicChangePanel = new JPanel(new BorderLayout());
		JLabel musicChangeLabel = new JLabel("Change Music");
		JButton musicTrack1 = new JButton("Tetris99");
		JButton musicTrack2 = new JButton("Tetris");
		JButton musicTrack3 = new JButton("Deja Vu");
		
		JPanel musicLoadPanel = new JPanel();
		JButton loadMusicButton = new JButton("Load Local Music");
		
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
 		
 		//music label	
 		musicChangeLabel.setFont(new Font("Arial", Font.BOLD, 20));
 		musicChangeLabel.setForeground(Color.WHITE);
 		musicChangeLabel.setBackground(Color.BLACK);
 		musicChangeLabel.setHorizontalAlignment(JLabel.CENTER);
 		
 		//music buttons
 		
 		musiceButtonsCSS(musicTrack1, Color.WHITE);
 		musiceButtonsCSS(musicTrack2, Color.WHITE);
 		musiceButtonsCSS(musicTrack3, Color.WHITE);
 		
 		
 		//music panel
 		musicChangePanel.setBackground(Color.BLACK);
 		musicChangePanel.add(musicChangeLabel,BorderLayout.NORTH);
 		musicChangePanel.add(musicTrack1,BorderLayout.WEST);
 		musicChangePanel.add(musicTrack2,BorderLayout.CENTER);
 		musicChangePanel.add(musicTrack3,BorderLayout.EAST);
 		
 		//music loading button
 		
 		loadMusicButton.setFont(new Font("Arial", Font.BOLD, 20));
 		loadMusicButton.setForeground(Color.WHITE);
 		loadMusicButton.setBackground(Color.BLACK);
 		loadMusicButton.setBorderPainted(false);
 		loadMusicButton.setFocusPainted(false);
 		
 		//music loading panel
 		musicLoadPanel.setBackground(Color.BLACK);
 		musicLoadPanel.add(loadMusicButton);
 		
 		//music loading button listener
 		
 		loadMusicButton.addActionListener(e->{
 			musicPlayer.playFile();
 			musicPlayer.stop();
 		});
 		
 		//music track button listener
 		musicTrack1.addActionListener(e->{
 			musicPlayer.preparedMusic(1);
 			musicPlayer.stop();
 		});
 		musicTrack2.addActionListener(e->{
 			musicPlayer.preparedMusic(2);
 			musicPlayer.stop();
 		});
 		musicTrack3.addActionListener(e->{
 			musicPlayer.preparedMusic(3);
 			musicPlayer.stop();
 		});
 		
 		
 		volumeSlider.addChangeListener((ChangeEvent e) -> {
 			float sliderValue = volumeSlider.getValue();
 			musicPlayer.setVolume(sliderValue/20);
 		});
 	
 		fullScreenButton.addActionListener((ActionEvent e) -> {
 			frame.setResizable(true);
 			
 			if(frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
 				
 				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
 	 			frame.dispose();
 	 			frame.setUndecorated(true);
 	 			fullScreenButton.setText("ON");
 	 			fullScreenButton.setForeground(Color.GREEN);
 			}else {
 				frame.setExtendedState(JFrame.NORMAL);
 				frame.setSize(740,885);
 				frame.setLocationRelativeTo(null);
 	 			frame.dispose();
 	 			frame.setUndecorated(false);
 	 			fullScreenButton.setText("OFF");
 	 			fullScreenButton.setForeground(Color.RED);
 			}
 			frame.setResizable(false);
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
 		optionsMenu.addElement(musicChangePanel);
 		optionsMenu.addElement(musicLoadPanel);
 		optionsMenu.addElement(volumePanel);	
 		optionsMenu.addElement(backButton); 
	}
	
	/**
	 * Makes the game over menu
	 */
	private void makeBasicMenu(String title,int menuId) {
		int menuWidth = 300;
		int menuHeigth = 200;
		int buttonTextSize = 20;
		String titleText = title;
		int titleLength = titleText.length();
		int titleTextSize = (menuWidth + 100)/titleLength;
		
		basicMenus[menuId] = new DialogMenu(fixedPanel,new Dimension(menuWidth,menuHeigth),2,5,title,titleTextSize);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");
		
		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, buttonTextSize);
		mainMenuButton.addActionListener((ActionEvent e) -> {
			basicMenus[menuId].close();
 			toMainMenu();
 		});
		
		formatButton(exitButton, Color.WHITE, Font.BOLD, buttonTextSize);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		
		basicMenus[menuId].addElement(mainMenuButton); 
		basicMenus[menuId].addElement(exitButton); 
	}
	
	/**
	 * Opens the Game Over menu
	 */
	public void openBasicMenu(int menuId) {
		System.out.println("menuID: " + menuId);
		basicMenus[menuId].open();
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

