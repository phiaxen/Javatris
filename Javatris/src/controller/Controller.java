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
	public Controller(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		if((key == KeyEvent.VK_RIGHT)&&!gameEngine.paused) {
			gameEngine.getCurrentShape().setDeltaX(1);
		}
		
		if((key == KeyEvent.VK_LEFT)&&!gameEngine.paused) {
			gameEngine.getCurrentShape().setDeltaX(-1);
		}
		
		if((key == KeyEvent.VK_UP)&&!gameEngine.paused) {
			gameEngine.getCurrentShape().rotate();
		}
		
		if((key == KeyEvent.VK_DOWN)&&!gameEngine.paused) {
			gameEngine.getCurrentShape().fasterSpeedDown();
		}
		if(key == KeyEvent.VK_ESCAPE) {
			if(!gameEngine.paused) {
				gameEngine.pause();
			}else {
				gameEngine.resume();
			}
			
		}
		if((key == KeyEvent.VK_0)&&!gameEngine.paused) {
			gameEngine.addRow(0,1);
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
