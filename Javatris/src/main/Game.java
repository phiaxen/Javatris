package main;

import server.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.imageio.IIOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.*;
import view.*;
import controller.*;

/**
 * The main framework for the game, Creates most classes and handles
 * communication between several of them.
 * 
 * @author Philip
 * @version 1.0
 * 
 *          Added a simple start-menu
 * @author Joachim Antfolk
 * @version 1.1
 */

public class Game {

	private final int BLOCKSIZE = 40;
	private final int BOARDWIDTH = 10;
	private final int BOARDHEIGHT = 20;
	private final JFrame frame;
	private JPanel fixedPanel;
	private Board board;
	private BoardView boardView;
	private GameEngine gameEngine;
	private Controller controller;
	private SideInfo sideInfo;
	private Client client;
	private MusicPlayer musicPlayer;
	private final SfxManager sfxManager;
	private JPanel gamePanel;
	private boolean firstGame = true;
	private final MenuHandler menuHandler;

	public Game(JFrame frame) {
		this.frame = frame;
		init();
		SetUpFrame();

		sfxManager = new SfxManager();
		menuHandler = new MenuHandler(this, frame, fixedPanel, musicPlayer, sfxManager);
		menuHandler.openStartMenu();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		if (width <= 1366 && height <= 768) {
			menuHandler.screenSizeToSmallMenu();
		}
	}

	public static void main(String[] args) {
		new Game(new JFrame("JavaTris"));
	}

	/**
	 * Initializes the game and creates all the neccecary components that are needed
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

		gameEngine.delegate = new GameEngine.Delegate() {

			@Override
			public void pause() {
				musicPlayer.stop();
				menuHandler.openPauseMenu();
			}

			@Override
			public void gameOver(int type) {
				musicPlayer.stop();

				switch (type) {
				case 0: // game over
					menuHandler.openBasicMenu(0);
					break;
				case 1: // loss
					menuHandler.openBasicMenu(1);
					break;
				case 2: // win
					menuHandler.openBasicMenu(2);
					break;
				case 3: // connection error
					menuHandler.openBasicMenu(3);
					break;
				default:
					break;
				}
			}

			@Override
			public void connectionLost() {
				menuHandler.openBasicMenu(4);
			}

			@Override
			public void sendInt(int number) {
				client.sendInt(number);
			}
		};
	}

	/**
	 * Initializes the frame and makes a game panel and a panel to center everything
	 * to the frame.
	 */
	private void SetUpFrame() {
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridBagLayout());
		gamePanel.setPreferredSize(new Dimension(684, 804));
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		gamePanel.add(sideInfo);
		gamePanel.add(boardView);

		fixedPanel = new JPanel();
		fixedPanel.setLayout(new GridBagLayout());
		fixedPanel.setPreferredSize(frame.getSize());
		fixedPanel.setBackground(Color.BLACK);

		frame.setSize(740, 885);
		frame.add(fixedPanel);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Starts the game
	 */
	public void startGame() {
		menuHandler.closeStartMenu();
		fixedPanel.add(gamePanel);
		fixedPanel.validate();
		fixedPanel.repaint();

		if (firstGame) {
			gameEngine.addPropertyChangeListener(boardView);
			gameEngine.addPropertyChangeListener(sideInfo);
			gameEngine.getShapeHandler().addPropertyChangeListener(sideInfo);
			gameEngine.addPropertyChangeListener(sfxManager);
			musicPlayer.play();
			gameEngine.start();
		} else {
			musicPlayer.restart();
			gameEngine.restart();
		}
		firstGame = false;
	}

	/**
	 * Loads a saved game
	 */
	public void loadGame() {
		boolean loaded = false;
		try {
			Savedata loadData = (Savedata) SaveManager.loadFile("saveFile.Save");
			board.setBoard(loadData.getBoard());
			gameEngine.setCurrentShape(loadData.getCurrentShape());
			gameEngine.setNextShapes(loadData.getNextShapes());
			gameEngine.setScore(loadData.getScore());
			gameEngine.setLevel(loadData.getLevel());
			gameEngine.setTime(loadData.getTime());
			gameEngine.setClearedRows(loadData.getRemovedRows());
			gameEngine.setIsLoading();
			loaded = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not load save-file");
			loaded = false;
		}
		if (loaded) {
			gameEngine.fireGameField();
			gamePanel.validate();
			gamePanel.repaint();
			startGame();
		} else {
			menuHandler.openStartMenu();
		}
	}

	/**
	 * Save a game
	 */
	public void saveGame() {
		Savedata saveData = new Savedata();
		saveData.setBoard(board.getBoard());
		saveData.setCurrentShape(gameEngine.getCurrentShape());
		saveData.setNextShapes(gameEngine.GetNextShapes());
		saveData.setScore(gameEngine.getPoints());
		saveData.setLevel(gameEngine.getLevel());
		saveData.setRemovedRows(gameEngine.getRemovedRows());
		saveData.setTime(gameEngine.getTime());

		try {
			SaveManager.saveFile(saveData, "saveFile.Save");
		} catch (IIOException e) {
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not save game");
		}
	}

	/**
	 * Starts a multiplayer Session of the game, opens up a prompt so the user can
	 * enter the ip and port to the server It should be inputed like this
	 * 127.0.0.1:2525 in other words ip:port
	 */
	public void startOnlineGame() {
		String code = menuHandler.showOnlineDialog();
		boolean connected = false;

		// Only starts if
		if (code != null && !code.isBlank() && !code.isEmpty()) {
			try {
				String[] adress = code.split(":");

				client = new Client(gameEngine, adress[0], Integer.parseInt(adress[1]));
				connected = true;
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				connected = false;
				JOptionPane.showMessageDialog(null, "Wrong format");
			} catch (UnknownHostException e) {
				connected = false;
				JOptionPane.showMessageDialog(null, "Not a valid host");
			} catch (IOException e) {
				connected = false;
				JOptionPane.showMessageDialog(null, "Wrong type Client");
			}
			if (connected) {
				gameEngine.setOnline(true);
				startGame();
			}
		}
	}

	/**
	 * Resumes the paused game
	 */
	public void resumeGame() {
		musicPlayer.play();
		gameEngine.resume();
	}

	/**
	 * Exits the current game and goes to the main menu
	 */
	public void removeGamePanel() {
		fixedPanel.remove(gamePanel);
		fixedPanel.validate();
		fixedPanel.repaint();
	}

	/**
	 * Returns the game Engine to offline state
	 */
	public void setOffline() {
		gameEngine.setOnline(false);
	}

}
