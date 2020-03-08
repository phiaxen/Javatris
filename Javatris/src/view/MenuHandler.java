package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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

/**
 * MenuHandler handles every menu in the game
 * 
 * @author Philip Axenhamn
 * @author Joachim Antfolk
 * @author Andreas Greppe
 * @author Tobias Mauritzon
 * @since 2020-03-02
 * @version 3.0
 */
public class MenuHandler {

	private final Game game;
	private final JFrame frame;
	private final JPanel fixedPanel;
	private Menu startMenu;
	private Menu creditsMenu;
	private Menu controlsMenu;
	private DialogMenu pauseMenu;
	private DialogMenu[] basicMenus;
	private DialogMenu optionsMenu;
	private final MusicPlayer musicPlayer;
	private final SfxManager sfxManager;
	private JSlider volumeSlider;

	public MenuHandler(Game game, JFrame frame, JPanel fixedPanel, MusicPlayer musicPlayer, SfxManager sfxManager) {
		this.game = game;
		this.frame = frame;
		this.basicMenus = new DialogMenu[4];
		this.fixedPanel = fixedPanel;
		this.musicPlayer = musicPlayer;
		this.sfxManager = sfxManager;
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
		makeControlsMenu();
		makeBasicMenu("GAME OVER", 0);
		makeBasicMenu("YOU LOOSE!", 1);
		makeBasicMenu("YOU WIN!", 2);
		makeBasicMenu("Connection Lost", 3);
	}

	/**
	 * Creates and opens an error message for users with a low resolution screen.
	 */
	public void screenSizeToSmallMenu() {
		int menuWidth = 350;
		int menuHeigth = 120;
		int buttonTextSize = 20;
		String titleText = "Screen size is to small";
		int titleLength = titleText.length();
		int titleTextSize = (menuWidth + 300) / titleLength;

		DialogMenu screenSizeToSmallMenu = new DialogMenu(fixedPanel, new Dimension(menuWidth, menuHeigth), 1,
				titleText, titleTextSize);
		JButton exitButton = new JButton("EXIT");

		formatButton(exitButton, Color.WHITE, Font.BOLD, buttonTextSize);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		screenSizeToSmallMenu.addElement(exitButton);
		screenSizeToSmallMenu.open();
	}

	/**
	 * Creates the main menu
	 */
	private void makeStartMenu() {

		startMenu = new Menu(new Dimension(700, 820), 0, 6, 0.20f, Color.BLACK);
		startMenu.addTitle("/images/javatris1.png");

		JButton playButton = new JButton("PLAY");
		JButton onlineButton = new JButton("ONLINE");
		JButton loadButton = new JButton("LOAD");
		JButton exitButton = new JButton("EXIT");
		JButton credits = new JButton("CREDITS");
		JButton controls = new JButton("CONTROLS");

		// play
		formatButton(playButton, Color.WHITE, Font.BOLD, 95);
		playButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.startGame();
		});

		// controls
		formatButton(controls, Color.WHITE, Font.BOLD, 40);
		controls.addActionListener((ActionEvent e) -> {
			startMenu.close();
			openControlsMenu();
		});

		// online
		formatButton(onlineButton, Color.WHITE, Font.BOLD, 50);
		onlineButton.addActionListener((ActionEvent e) -> {
			game.startOnlineGame();
		});

		// load
		formatButton(loadButton, Color.WHITE, Font.BOLD, 50);
		loadButton.addActionListener((ActionEvent e) -> {
			startMenu.close();
			game.loadGame();
		});

		// exit
		formatButton(exitButton, Color.WHITE, Font.BOLD, 40);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});

		// credits
		formatButton(credits, Color.GRAY, Font.BOLD, 20);
		credits.addActionListener((ActionEvent e) -> {
			startMenu.close();
			openCreditsMenu();
		});

		startMenu.addElementBottom(playButton);
		startMenu.addElementBottom(controls);
		startMenu.addElementBottom(onlineButton);
		startMenu.addElementBottom(loadButton);
		startMenu.addElementBottom(exitButton);

		startMenu.addElementBottom(credits);
		fixedPanel.add(startMenu);
	}

	/**
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

	/**
	 * Makes the pause menu
	 */
	private void makePauseMenu() {
		int menuWidth = 200;
		int menuHeigth = 250;
		int textSize = menuWidth / 10;

		pauseMenu = new DialogMenu(fixedPanel, new Dimension(menuWidth, menuHeigth), 4, "PAUSE", 40);
		JButton resumeButton = new JButton("Resume");
		JButton optionsButton = new JButton("Options");
		JButton saveButton = new JButton("Save");
		JButton mainMenuButton = new JButton("Main Menu");

		// resume
		formatButton(resumeButton, Color.WHITE, Font.BOLD, textSize);
		resumeButton.addActionListener((ActionEvent e) -> {
			pauseMenu.close();
			game.resumeGame();
		});

		// options
		formatButton(optionsButton, Color.WHITE, Font.BOLD, textSize);
		optionsButton.addActionListener((ActionEvent e) -> {
			pauseMenu.close();
			optionsMenu.open();
		});

		// save
		formatButton(saveButton, Color.WHITE, Font.BOLD, textSize);
		saveButton.addActionListener((ActionEvent e) -> {
			game.saveGame();
		});

		// main menu
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
		volumeSlider.setValue(Math.round(musicPlayer.getVolume() * 20));
		pauseMenu.open();
	}

	/**
	 * Closes the pause menu
	 */
	public void closePauseMenu() {
		pauseMenu.close();
	}

	private void musiceButtonsCSS(JButton b, Color fore, Color back) {
		b.setFont(new Font("Arial", Font.BOLD, 15));
		b.setForeground(fore);
		b.setBackground(back);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
	}

	/**
	 * Makes the options menu
	 */
	public void makeOptionsMenu() {
		int menuWidth = 350; // 250
		int menuHeigth = 500; // 250
		int textSize = menuWidth / 10;
		Color color = new Color(30, 30, 30);

		optionsMenu = new DialogMenu(fixedPanel, new Dimension(menuWidth, menuHeigth), 6, "OPTIONS", 40);

		JPanel fullScreenPanel = new JPanel();
		JLabel fullScreenLabel = new JLabel("Full Screen");
		JButton fullScreenButton = new JButton("Off");

		JPanel sfxPanel = new JPanel();
		JLabel sfxLabel = new JLabel("SFX");
		JButton sfxButton = new JButton("ON");

		JPanel volumePanel = new JPanel(new BorderLayout());
		JLabel sliderLabel = new JLabel("Music Volume", SwingConstants.CENTER);
		volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, Math.round(musicPlayer.getVolume() * 20));

		JPanel musicChangePanel = new JPanel(new BorderLayout());
		JLabel musicChangeLabel = new JLabel("Change Music");
		JButton musicTrack1 = new JButton("Tetris");
		JButton musicTrack2 = new JButton("Tetris99");
		JButton musicTrack3 = new JButton("Deja Vu");

		JPanel musicLoadPanel = new JPanel();
		JButton loadMusicButton = new JButton("Load Local Music");

		JButton backButton = new JButton("Back");

		// full screen panel
		fullScreenPanel.setBackground(color);

		// full screen button
		fullScreenButton.setFont(new Font("Arial", Font.BOLD, 20));
		fullScreenButton.setForeground(Color.RED);
		fullScreenButton.setBackground(color);
		fullScreenButton.setBorderPainted(false);
		fullScreenButton.setFocusPainted(false);

		// full screen label
		fullScreenLabel.setFont(new Font("Arial", Font.BOLD, 20));
		fullScreenLabel.setForeground(Color.WHITE);
		fullScreenLabel.setBackground(color);

		// sfx panel
		sfxPanel.setBackground(color);

		// sfx button
		sfxButton.setFont(new Font("Arial", Font.BOLD, 20));
		sfxButton.setForeground(Color.GREEN);
		sfxButton.setBackground(color);
		sfxButton.setBorderPainted(false);
		sfxButton.setFocusPainted(false);

		// sfx label
		sfxLabel.setFont(new Font("Arial", Font.BOLD, 20));
		sfxLabel.setForeground(Color.WHITE);
		sfxLabel.setBackground(color);

		// slider label
		sliderLabel.setFont(new Font("Arial", Font.BOLD, 20));
		sliderLabel.setForeground(Color.WHITE);
		sliderLabel.setBackground(color);

		// volume slider
		volumeSlider.setPaintLabels(false);
		volumeSlider.setSnapToTicks(true);
		volumeSlider.setBackground(color);

		// volume panel
		volumePanel.setBackground(color);
		volumePanel.add(sliderLabel, BorderLayout.NORTH);
		volumePanel.add(volumeSlider, BorderLayout.SOUTH);

		// music label
		musicChangeLabel.setFont(new Font("Arial", Font.BOLD, 20));
		musicChangeLabel.setForeground(Color.WHITE);
		musicChangeLabel.setBackground(color);
		musicChangeLabel.setHorizontalAlignment(JLabel.CENTER);

		// music buttons
		musiceButtonsCSS(musicTrack1, Color.WHITE, color);
		musiceButtonsCSS(musicTrack2, Color.WHITE, color);
		musiceButtonsCSS(musicTrack3, Color.WHITE, color);

		// music panel
		musicChangePanel.setBackground(color);
		musicChangePanel.add(musicChangeLabel, BorderLayout.NORTH);
		musicChangePanel.add(musicTrack1, BorderLayout.WEST);
		musicChangePanel.add(musicTrack2, BorderLayout.CENTER);
		musicChangePanel.add(musicTrack3, BorderLayout.EAST);

		// music loading button
		loadMusicButton.setFont(new Font("Arial", Font.BOLD, 20));
		loadMusicButton.setForeground(Color.WHITE);
		loadMusicButton.setBackground(color);
		loadMusicButton.setBorderPainted(false);
		loadMusicButton.setFocusPainted(false);

		// music loading panel
		musicLoadPanel.setBackground(color);
		musicLoadPanel.add(loadMusicButton);

		// music loading button listener
		loadMusicButton.addActionListener(e -> {
			musicPlayer.playFile();
			musicPlayer.stop();
		});

		// music track button listener
		musicTrack1.addActionListener(e -> {
			musicPlayer.preparedMusic(1);
			musicPlayer.stop();
		});
		musicTrack2.addActionListener(e -> {
			musicPlayer.preparedMusic(2);
			musicPlayer.stop();
		});
		musicTrack3.addActionListener(e -> {
			musicPlayer.preparedMusic(3);
			musicPlayer.stop();
		});

		volumeSlider.addChangeListener((ChangeEvent e) -> {
			float sliderValue = volumeSlider.getValue();
			musicPlayer.setVolume(sliderValue / 20);
		});

		sfxButton.addActionListener((ActionEvent e) -> {

			if (sfxManager.getMuted() == true) {
				sfxManager.setMuted(false);
				sfxButton.setText("ON");
				sfxButton.setForeground(Color.GREEN);
			} else {
				sfxManager.setMuted(true);
				sfxButton.setText("OFF");
				sfxButton.setForeground(Color.RED);
			}
		});

		fullScreenButton.addActionListener((ActionEvent e) -> {
			frame.setResizable(true);

			if (frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {

				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.dispose();
				frame.setUndecorated(true);
				fullScreenButton.setText("ON");
				fullScreenButton.setForeground(Color.GREEN);
			} else {
				frame.setExtendedState(JFrame.NORMAL);
				frame.setSize(740, 885);
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

		fullScreenPanel.add(fullScreenLabel, BorderLayout.WEST);
		fullScreenPanel.add(fullScreenButton, BorderLayout.EAST);

		sfxPanel.add(sfxLabel, BorderLayout.WEST);
		sfxPanel.add(sfxButton, BorderLayout.EAST);

		formatButton(backButton, Color.WHITE, Font.BOLD, textSize);
		backButton.addActionListener((ActionEvent e) -> {
			optionsMenu.close();
			pauseMenu.open();
		});

		optionsMenu.addElement(fullScreenPanel);
		optionsMenu.addElement(musicChangePanel);
		optionsMenu.addElement(musicLoadPanel);
		optionsMenu.addElement(volumePanel);
		optionsMenu.addElement(sfxPanel);
		optionsMenu.addElement(backButton);
	}

	/**
	 * Makes basic menu
	 * 
	 * @param title  the title of the menu
	 * @param index the id of the menu
	 */
	private void makeBasicMenu(String title, int index) {
		int menuWidth = 300;
		int menuHeigth = 200;
		int buttonTextSize = 20;
		String titleText = title;
		int titleLength = titleText.length();
		int titleTextSize = (menuWidth + 100) / titleLength;

		basicMenus[index] = new DialogMenu(fixedPanel, new Dimension(menuWidth, menuHeigth), 2, title, titleTextSize);
		JButton mainMenuButton = new JButton("Main Menu");
		JButton exitButton = new JButton("Exit");

		formatButton(mainMenuButton, Color.WHITE, Font.BOLD, buttonTextSize);
		mainMenuButton.addActionListener((ActionEvent e) -> {
			basicMenus[index].close();
			toMainMenu();
		});

		formatButton(exitButton, Color.WHITE, Font.BOLD, buttonTextSize);
		exitButton.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});

		basicMenus[index].addElement(mainMenuButton);
		basicMenus[index].addElement(exitButton);
	}

	/**
	 * Opens the Game Over menu
	 * @param index the menu that should be opened.
	 */
	public void openBasicMenu(int index) {
		basicMenus[index].open();
	}

	/**
	 * Creates the credits screen
	 */
	private void makeCreditsMenu() {
		creditsMenu = new Menu(new Dimension(700, 820), 2, 1, 0.75f, Color.BLACK);

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

		JLabel text1 = new JLabel("CREATED BY", SwingConstants.CENTER);
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
		textArea.setText("Joachim Antfolk\r\n" + "\r\n" + "Philip Axenhamn\r\n" + "\r\n" + "Andreas Greppe\r\n" + "\r\n"
				+ "Tobias Mauritzon\r\n" + "\r\n");
		textArea.setEditable(false);
		creditsMenu.addElementTop(text1);
		creditsMenu.addElementTop(textArea);
		creditsMenu.addElementBottom(back);
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
	 * Creates the controls screen
	 */
	private void makeControlsMenu() {
		controlsMenu = new Menu(new Dimension(700, 820), 2, 1, 0.75f, Color.BLACK);
		JButton back = new JButton("Back");
		JLabel text = new JLabel("CONTROLS", SwingConstants.CENTER);

		back.setFont(new Font("Arial", Font.BOLD, 20));
		back.setForeground(Color.WHITE);
		back.setBackground(Color.BLACK);
		back.setBorderPainted(false);
		back.setFocusPainted(false);

		back.addActionListener((ActionEvent e) -> {
			controlsMenu.close();
			toMainMenu();
		});

		text.setForeground(Color.white);
		text.setFont(new Font("Arial", Font.BOLD, 50));

		controlsMenu.addElementTop(text);
		
		try {
			BufferedImage image = ImageIO.read(Menu.class.getResource("/images/how-to-play.png"));
			JLabel title = new JLabel(new ImageIcon(image));
			controlsMenu.addElementTop(title);
		} catch (IOException e) {
			e.printStackTrace();
		}

		controlsMenu.addElementBottom(back);
		controlsMenu.close();
		fixedPanel.add(controlsMenu);
	}

	/**
	 * Opens controls menu
	 */
	private void openControlsMenu() {
		controlsMenu.open();
	}

	/**
	 * Opens the dialog to get server info
	 * 
	 * @return server IP and port
	 */
	public String showOnlineDialog() {
		String code = JOptionPane.showInputDialog(frame, "Enter the server ip and port, (IP:PORT)", "Connect to server",
				JOptionPane.PLAIN_MESSAGE);
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
	 * 
	 * @param button     button to be formated
	 * @param foreground foreground color
	 * @param fontType   font type bold, italic, etc.
	 * @param fontSize   size of the font
	 */
	private void formatButton(JButton button, Color foreground, int fontType, int fontSize) {
		String font = "Arial";

		button.setFont(new Font(font, fontType, fontSize));
		button.setForeground(foreground);
		button.setBackground(Color.BLACK);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
	}

}
