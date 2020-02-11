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

	public Controller() {
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_RIGHT) {
			GameEngine.getCurrentShape().setDeltaX(1);
		}
		
		if(key == KeyEvent.VK_LEFT) {
			GameEngine.getCurrentShape().setDeltaX(-1);
		}
		
		if(key == KeyEvent.VK_UP) {
			GameEngine.getCurrentShape().rotate();
		}
		
		if(key == KeyEvent.VK_DOWN) {
			GameEngine.getCurrentShape().fasterSpeedDown();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_DOWN) {
			GameEngine.getCurrentShape().normalSpeedDown();
		}
		
	}

}
