package controller;

import java.awt.KeyEventDispatcher;

import java.awt.event.KeyEvent;

import model.*;


/**
 * THIS CLASS IS A CONTROLLER
 * Controller is a class that handles all user inputs.
 * @author Philip Axenhamn
 * @author Tobias Mauritzon
 * @author Tobias Mauritzon
 * @author Joachim Antfolk
 * @since 2020-03-01
 * @version 2.0
 */
public class Controller implements KeyEventDispatcher{

	private GameEngine gameEngine;
	private MusicPlayer musicPlayer; //just for testing
	
	public Controller(GameEngine gameEngine, MusicPlayer musicPlayer) {
		this.gameEngine = gameEngine;
		this.musicPlayer = musicPlayer;
	}
	/**When a key is pressed on the keyboard a KeyEvent is sent here
	 * @param e is the key event thats handled
	 * @return not used right now
	 */
	@Override
    public boolean dispatchKeyEvent(KeyEvent e) {
		int key = e.getKeyCode();   
		
		if ((e.getID() == KeyEvent.KEY_PRESSED)) {
			
			if(gameEngine.running()) {
				if((key == KeyEvent.VK_RIGHT)) {	
					if(!(gameEngine.getCurrentShape().getX() + gameEngine.getCurrentShape().getCoords()[0].length == 10)) {
						gameEngine.getCurrentShape().setDeltaX(1);
						gameEngine.setDelayBeforeStatic();
					}
				}
				
				if((key == KeyEvent.VK_LEFT)) {
					if(!(gameEngine.getCurrentShape().getX() == 0)){
						gameEngine.getCurrentShape().setDeltaX(-1);
						gameEngine.setDelayBeforeStatic();
					}
				}
				
				if((key == KeyEvent.VK_UP)) {
					gameEngine.getCurrentShape().rotate();
					gameEngine.setDelayBeforeStatic();
				}
				
				if((key == KeyEvent.VK_DOWN)) {
					gameEngine.getCurrentShape().fasterSpeedDown();
				}
				if(key == KeyEvent.VK_ESCAPE) {
					gameEngine.pause();
				}
			}
			
			
			//Just for testing
			if((key == KeyEvent.VK_0)) {
				gameEngine.addRow(0,1);
			}
			if((key == KeyEvent.VK_W)) {
				musicPlayer.incVolume();
			}
			if((key == KeyEvent.VK_S)) {
				musicPlayer.decVolume();
			}
			if((key == KeyEvent.VK_M)) {
				musicPlayer.mute();
			}
			
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
       
    		if(key == KeyEvent.VK_DOWN) {
    			gameEngine.getCurrentShape().normalSpeedDown();
    		}
    		
        } else if (e.getID() == KeyEvent.KEY_TYPED) {
            
        }
		
        return false;
    }
}