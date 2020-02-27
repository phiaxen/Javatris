package controller;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import main.Game;
import model.*;

import view.Dialog;


/**
 * THIS CLASS IS A CONTROLLER
 * Controller is a class that handles all user inputs.
 * 
 * 
 * @author Philip
 * @version 1.0
 */

public class Controller implements KeyEventDispatcher{

	private GameEngine gameEngine;
	private MusicPlayer musicPlayer; //just for testing
	private SfxManager sfxManager;
	
	public Controller(GameEngine gameEngine, MusicPlayer musicPlayer, Dialog pauseMenu) {
		this.gameEngine = gameEngine;
		this.musicPlayer = musicPlayer;
		this.sfxManager = new SfxManager();
		gameEngine.addPropertyChangeListener(sfxManager);
	}
	
	@Override
    public boolean dispatchKeyEvent(KeyEvent e) {
		int key = e.getKeyCode();   
		
		if ((e.getID() == KeyEvent.KEY_PRESSED)&&gameEngine.running()) {
			if((key == KeyEvent.VK_RIGHT)) {	
				if(!(gameEngine.getCurrentShape().getX() + gameEngine.getCurrentShape().getCoords()[0].length == 10)) {
					gameEngine.getCurrentShape().setDeltaX(1);
					sfxManager.playSound1();
				}
			}
			
			if((key == KeyEvent.VK_LEFT)) {
				if(!(gameEngine.getCurrentShape().getX() == 0)){
					gameEngine.getCurrentShape().setDeltaX(-1);
					sfxManager.playSound1();
				}
			}
			
			if((key == KeyEvent.VK_UP)) {
				gameEngine.getCurrentShape().rotate();
			}
			
			if((key == KeyEvent.VK_DOWN)) {
				gameEngine.getCurrentShape().fasterSpeedDown();
			}
			if(key == KeyEvent.VK_ESCAPE) {
				
				if(gameEngine.running()) {
					gameEngine.pause();
				}else {
					gameEngine.resume();
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