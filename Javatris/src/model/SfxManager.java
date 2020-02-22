package model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import view.Menu;

public class SfxManager {

	private Clip sfx1;
	private Clip sfx2;
	private Clip sfx3;
	private Clip sfx4;
	
	public SfxManager() {
		init();
	}
	
	private void init() {
		getSound1();
		getSound2();
		getSound3();
		getSound4();
	}
	
	private void getSound1() {
		try {
			URL file1 = SfxManager.class.getResource("/soundEffects/sfx1.wav");	
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
			URL file2 = SfxManager.class.getResource("/soundEffects/sfx2.1.wav");	
			sfx2 = AudioSystem.getClip();         
			sfx2.open(AudioSystem.getAudioInputStream(file2)); 
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	private void getSound3() {
		try {
			URL file3 = SfxManager.class.getResource("/soundEffects/sfx3.wav");
			sfx3 = AudioSystem.getClip();         
			sfx3.open(AudioSystem.getAudioInputStream(file3)); 
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	private void getSound4() {
		try {
			
			URL file4 = SfxManager.class.getResource("/soundEffects/sfx4.wav");
			sfx4 = AudioSystem.getClip();         
			sfx4.open(AudioSystem.getAudioInputStream(file4)); 
			float volume = 0.3f;			
			FloatControl gainControl = (FloatControl) sfx4.getControl(FloatControl.Type.MASTER_GAIN);        
			gainControl.setValue(20f * (float) Math.log10(volume));
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	//move
	public void playSound1() {
		sfx1.start();
		getSound1();
	}
	//hit
	public void playSound2() {
		sfx2.start();
		getSound2();
	}
	//line cleared
	public void playSound3() {
		sfx3.start();
		getSound3();
	}
	//level up
	public void playSound4() {
		sfx4.start();
		getSound4();
	}
}
