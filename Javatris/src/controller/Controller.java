package controller;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.*;

/**
 * THIS CLASS IS A CONTROLLER
 * Controller is a class that handles all user inputs.
 * 
 * 
 * @author Philip
 * @version 1.0
 */

public class Controller implements KeyListener{

	private GameEngine gameEngine;
	private MusicPlayer musicPlayer; //just for testing
	
	public Controller(GameEngine gameEngine, MusicPlayer musicPlayer) {
		this.gameEngine = gameEngine;
		this.musicPlayer = musicPlayer;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		if((key == KeyEvent.VK_RIGHT)&&!gameEngine.paused()) {
			if(!gameEngine.getCurrentShape().rightBound()) {
				gameEngine.getCurrentShape().setDeltaX(1);
			}
		}
		
		if((key == KeyEvent.VK_LEFT)&&!gameEngine.paused()) {
			if(!gameEngine.getCurrentShape().leftBound()) {
				gameEngine.getCurrentShape().setDeltaX(-1);
			}
		}
		
		if((key == KeyEvent.VK_UP)&&!gameEngine.paused()) {
			gameEngine.getCurrentShape().rotate();
		}
		
		if((key == KeyEvent.VK_DOWN)&&!gameEngine.paused()) {
			gameEngine.getCurrentShape().fasterSpeedDown();
		}
		if(key == KeyEvent.VK_ESCAPE) {
			if(!gameEngine.paused()) {
				gameEngine.pause();
				musicPlayer.stopSong();
			}else {
				gameEngine.resume();
				musicPlayer.restartSong();
			}
			
		}
		
		//Just for testing
		if((key == KeyEvent.VK_0)&&!gameEngine.paused()) {
			gameEngine.addRow(0,1);
		}
		if((key == KeyEvent.VK_W)&&!gameEngine.paused()) {
			musicPlayer.incVolume();
		}
		if((key == KeyEvent.VK_S)&&!gameEngine.paused()) {
			musicPlayer.decVolume();
		}
		if((key == KeyEvent.VK_M)&&!gameEngine.paused()) {
			musicPlayer.mute();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_DOWN) {
			gameEngine.getCurrentShape().normalSpeedDown();
		}
		
	}

}
