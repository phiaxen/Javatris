package controller;

import java.awt.KeyEventDispatcher;

import java.awt.event.KeyEvent;

import model.*;
import view.MusicPlayer;

/**
 * Controller is a class that handles all user inputs.
 * 
 * @author Philip Axenhamn
 * @author Tobias Mauritzon
 * @author Joachim Antfolk
 * @since 2020-03-01
 * @version 2.0
 */
public class Controller implements KeyEventDispatcher {

	private GameEngine gameEngine;
	private MusicPlayer musicPlayer;

	public Controller(GameEngine gameEngine, MusicPlayer musicPlayer) {
		this.gameEngine = gameEngine;
		this.musicPlayer = musicPlayer;
	}

	/**
	 * When a key is pressed on the keyboard a KeyEvent is sent here
	 * 
	 * @param e is the key event thats handled
	 * @return not used right now
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		int key = e.getKeyCode();

		if ((e.getID() == KeyEvent.KEY_PRESSED)) {

			if ((key == KeyEvent.VK_RIGHT)) {
				gameEngine.setDeltaXCurrentShape(1);
			}

			if ((key == KeyEvent.VK_LEFT)) {
				gameEngine.setDeltaXCurrentShape(-1);
			}

			if ((key == KeyEvent.VK_UP)) {
				gameEngine.rotateCurrentShape();
			}

			if ((key == KeyEvent.VK_DOWN)) {
				gameEngine.getCurrentShape().fasterSpeedDown();
			}
			if (key == KeyEvent.VK_ESCAPE) {
				gameEngine.pause();
			}

			if ((key == KeyEvent.VK_W)) {
				musicPlayer.incVolume();
			}
			if ((key == KeyEvent.VK_S)) {
				musicPlayer.decVolume();
			}
			if ((key == KeyEvent.VK_M)) {
				musicPlayer.mute();
			}

		} else if (e.getID() == KeyEvent.KEY_RELEASED) {

			if (key == KeyEvent.VK_DOWN) {
				gameEngine.getCurrentShape().normalSpeedDown();
			}

		} else if (e.getID() == KeyEvent.KEY_TYPED) {

		}

		return false;
	}
}