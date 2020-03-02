package model;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 * MusicPlayer is a class that handles the background songs
 * @author Tobias Mauritzon
 * @author Philip Axenhamn
 * @author Joachim Antfolk
 * @version 3.0
 * @since 2020-02-29
 */

// datumet är inte rätt än

public class MusicPlayer {
	
	private Clip audioClip;
	private final float MaxSteps = 20; //Even numbers only
	private final float steps = 1/MaxSteps;
	private boolean muted;
	private float gobalVolume;
	
	
	private boolean fileLoaded = false;
	
	/**
	 * MusicPlayer is the constructor for MusicPlayer.
	 * @param choice 1-3 for prepared songs else you get an empty constructor and 
	 * you need to use playFile
	 * @return Nothing.
	*/
	public MusicPlayer (int choice) {		
		switch(choice) {
		case 1: loadMusic("/songs/Tetris Game Theme1.wav"); break;
		case 2: loadMusic("/songs/Tetris99 Game Theme1.wav"); break;
		case 3: loadMusic("/songs/08 Dave Rodgers - Deja Vu.wav"); break;
		default: break;
		}
		muted = false;
	}
	
	/**
	 * preparedMusic starts one of the prepared tacks
	 * @param choice 1-3 for prepared songs
	 */
	public void preparedMusic(int choice) {
		switch(choice) {
		case 1: loadMusic("/songs/Tetris Game Theme1.wav"); break;
		case 2: loadMusic("/songs/Tetris99 Game Theme1.wav"); break;
		case 3: loadMusic("/songs/08 Dave Rodgers - Deja Vu.wav"); break;
		}
	}
	
	 /**
	 * loadMusic loads and starts the song.
	 * @param fp is the file path to the song as a string
	 * @return Nothing.
	 * @exception if file path is incorrect
	 */
	public void loadMusic(String fp) {
		
		if(fileLoaded){
			audioClip.stop();
			audioClip.flush();
		}		
														
		URL audioFile = SfxManager.class.getResource(fp);	
		
		
		try {
								
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);				
			
			audioClip = AudioSystem.getClip();         
			audioClip.open(audioStream);   
			
			if(fileLoaded) {
				setVolume(gobalVolume);
			}
			
			if(!fileLoaded){
				setVolume(0.4f);	//start volume
			}
			
			fileLoaded = true;
		}
		catch(Exception e){
			
			JOptionPane.showMessageDialog(null, "Error with audio loading");
		}
		 
	}
	
		
	/**
	 * play restarts the currently playing song from the stopped point.
	 * @param Empty
	 * @return Nothing.
	 * @exception if no clip has been created
	 */	
	public void play() {
		System.out.println("PLAY SONG");
		audioClip.loop(Clip.LOOP_CONTINUOUSLY);  
	}


	 /**
	 * playMusicFile loads and starts the song.
	 * @param audioFile is the File you want to load
	 * @return Nothing.
	 */	
	public void playMusicFile(File audioFile) {										 	
	
		if(fileLoaded){
			audioClip.stop();
			audioClip.flush();
		}
		
		try {
			
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			
			
			audioClip = AudioSystem.getClip();         
			audioClip.open(audioStream);    
			
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);  
			
			if(fileLoaded) {
				setVolume(gobalVolume);
			}

			if(!fileLoaded){
				setVolume(0.4f);	//start volume
			}
			
			fileLoaded = true;
		}
		catch(Exception e){
			
			JOptionPane.showMessageDialog(null, "Error with audio loading");
		}
		
	}
	
	/**
	* stop stops the currently playing song.
	* @param Empty
	* @return Nothing.
	* @exception if no clip has been created
	*/
	public void stop() {
		System.out.println("STOP SONG");
		if(fileLoaded) {
			try {
				audioClip.stop();
			}
			catch(Exception e) {System.out.println("No musik running");}
			}
	}
	
	/**
	 * restart starts the current song over from the beginning
	 * @param Empty
	 * @return Nothing
	 */	
	public void restart() {
		audioClip.setFramePosition(0);
		play();
	}
	
	/**
	 * getVolume gets the volume of the current song
	 * @param Empty
	 * @return type float volume between 0 and 1 in floating as point.
	 */
	public float getVolume() {
		if(fileLoaded) {
	    
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
	    
			float volume = (float) Math.pow(10f, gainControl.getValue() / 20f);
			volume = (float) Math.round(volume*100)/100;
			return volume;
	    }
		return 0f;
	}
		
	/**
	 * setVolume sets the volume of the current song
	 * @param Empty
	 * @return volume is a floating point between 0 and 1.
	 */
	public void setVolume(float volume){
		
			if (volume < 0f || volume > 1f) {
				throw new IllegalArgumentException("Volume not valid: " + volume);}
			else {
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
				gainControl.setValue(20f * (float) Math.log10(volume));
				gobalVolume = volume;
			}
		
		
	}
	
	/**
	 * incVolume increases the volume of the current song in 0.05 steps
	 * @param Empty
	 * @return Nothing
	 */
	public void incVolume() {
		if(fileLoaded) {
			float volume = getVolume();
			volume += steps;
			
			if(volume < 1.0f) {						
				volume = (float)Math.round(volume*100)/100;
				System.out.println(volume);
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			    gobalVolume = getVolume();
			}
			else {
				volume = 1f;
				
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			    gobalVolume = getVolume();
			}
		}

	}
	
	/**
	 * decVolume decreases the volume of the current song in 0.05 steps
	 * @param Empty
	 * @return Nothing
	 */
	public void decVolume() {
		if(fileLoaded) {
			float volume = getVolume();
			volume -= steps;
			
			if(volume > 0.0f) {				
				volume = (float) Math.round(volume*100)/100;
				System.out.println(volume);
				
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			    gobalVolume = getVolume();
			}
			else {
				volume = 0f;
				
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			    gobalVolume = getVolume();
			}
		}
	}
	
	/**
	 * mute mutes the current song
	 * @param Empty
	 * @return Nothing
	 */
	public void mute() {
		if(fileLoaded) {
			if(muted) {
				setVolume(this.gobalVolume);
				muted = false;
			}
			else {
				gobalVolume = getVolume();
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
				gainControl.setValue(20f * (float) Math.log10(0.0f));
				muted = true;
			}
		}
		
	}

	/**
	 * playFile Opens a JFileChooser and sends your selected .wav file to playMusicFile
	 * this is a way to lad local sound files in to the game
	 * @param Empty
	 * @return Nothing
	 */	
	public void playFile() {
		
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
		j.setFont(new Font("Arial", Font.BOLD, 20));
		j.setForeground(Color.WHITE);
		j.setBackground(Color.BLACK);
        // invoke the showsSaveDialog function to show the save dialog 
        int r = j.showOpenDialog(null); 

        // if the user selects a file 
        if(r == JFileChooser.APPROVE_OPTION){ 
        	File path = new File(j.getSelectedFile().getAbsolutePath());
 
        	if (path.getName().endsWith((".wav"))){
        		playMusicFile(j.getSelectedFile());
        	}
        	else {
        		JOptionPane.showMessageDialog(null, "Wrong file type "+j.getSelectedFile().getAbsolutePath()); // System.out.println();
        	}
        } 
        // if the user cancelled the operation 
        else
        	JOptionPane.showMessageDialog(null,"The user cancelled the operation"); 
	}
	
}
