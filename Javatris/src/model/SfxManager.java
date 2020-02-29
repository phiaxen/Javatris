package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SfxManager implements PropertyChangeListener{

	private Clip sfx1;
	private Clip sfx2;
	private Clip sfx3;
	private Clip sfx4;
	private boolean muted;
	
	public SfxManager() {
		init();
	}
	
	private void init() {
		getSound1();
		getSound2();
		getSound3();
		getSound4();
		muted = false;
	}
	
	private void getSound1() {
		try {
			Path path = FileSystems.getDefault().getPath("").toAbsolutePath(); 
			File file1 = new File(path + "\\src\\soundEffects\\sfx1.wav");	
			sfx1 = AudioSystem.getClip();  
			sfx1.open(AudioSystem.getAudioInputStream(file1)); 
			float volume = 0.1f;			
			FloatControl gainControl = (FloatControl) sfx1.getControl(FloatControl.Type.MASTER_GAIN);        
			gainControl.setValue(20f * (float) Math.log10(volume));
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	private void getSound2() {
		try {
			Path path = FileSystems.getDefault().getPath("").toAbsolutePath(); 
			File file2 = new File(path + "\\src\\soundEffects\\sfx2.1.wav");	
			sfx2 = AudioSystem.getClip();         
			sfx2.open(AudioSystem.getAudioInputStream(file2)); 
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	private void getSound3() {
		try {
			Path path = FileSystems.getDefault().getPath("").toAbsolutePath(); 
			File file3 = new File(path + "\\src\\soundEffects\\sfx3.wav");	
			sfx3 = AudioSystem.getClip();         
			sfx3.open(AudioSystem.getAudioInputStream(file3)); 
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	private void getSound4() {
		try {
			Path path = FileSystems.getDefault().getPath("").toAbsolutePath(); 
			File file4 = new File(path + "\\src\\soundEffects\\sfx4.wav");	
			sfx4 = AudioSystem.getClip();         
			sfx4.open(AudioSystem.getAudioInputStream(file4)); 
			float volume = 0.3f;			
			FloatControl gainControl = (FloatControl) sfx4.getControl(FloatControl.Type.MASTER_GAIN);        
			gainControl.setValue(20f * (float) Math.log10(volume));
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	public void playSound1() {
		if(!muted) {
			sfx1.start();
			getSound1();
		}
	}
	//hit
	public void playSound2() {
		if(!muted) {
			sfx2.start();
			getSound2();
		}
	}
	//line cleared
	public void playSound3() {
		if(!muted) {
			sfx3.start();
			getSound3();
		};
	}
	//level up
	public void playSound4() {
		if(!muted) {
			sfx4.start();
			getSound4();
		}
		
	}
	
	/**
	 * setMute sets the if the sounds should play or not
	 * @param mute : boolean  
	 */
	public void setMuted(boolean mute){
		this.muted = mute;
		
	}
	
	/**
	 * getMute returns if sfx are muted or not
	 * @return : value of muted  
	 */
	public boolean getMuted(){
		return this.muted;
		
	}

	/**
	 * Applies property-changes from observed objects
	 * @param evt : the fired event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if(evt.getOldValue() != null) {
			if(property.equals("level")) {
				playSound4();
			}
			if(property.equals("lines cleared")) {
				playSound3();
			}
			if(property.equals("collisionY")) {
				playSound2();
			}
			if(property.equals("movedX")) {
				playSound1();
			}
		}
	}
}
