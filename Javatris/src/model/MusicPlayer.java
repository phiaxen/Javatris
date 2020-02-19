package model;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;



public class MusicPlayer {
	
	private Clip audioClip;
	private boolean restart;
	private final float MaxSteps = 20; //Even numbers only
	private final float steps = 1/MaxSteps;
	
	
	private boolean fileLoaded = false;
	
	//exempel på url:er
	public MusicPlayer() {		
		
		//playMusic4("G:\\firefox\\eclipse object ori\\Javatris2\\Javatris\\src\\songs\\Tetris99 Game Theme.wav");	Lokal dator
		//playMusic4("Javatris\\\\src\\\\songs\\\\Tetris99 Game Theme.wav"); // Vet inte varför det har blivit fler \\ det var inte fyra från början
		
		
		//playMusic("Javatris/src/songs/Tetris99 Game Theme.wav");  // variant 1	
		playMusic("\\src\\songs\\Tetris Game Theme1.wav");			// variant 2	
	}
	
	// funkar
	public void playMusic(String fp) {
		
//		File audioFile = new File(fp);	// variant 1	
		
		
		 Path path = FileSystems.getDefault().getPath("").toAbsolutePath(); // variant 2	
		 File audioFile = new File(path + fp);								// variant 2	
	
		try {
								
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			
			audioClip = AudioSystem.getClip();         
			audioClip.open(audioStream);    
			
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);  
			fileLoaded = true;
		}
		catch(Exception e){
			
			JOptionPane.showMessageDialog(null, "Error");
		}
		
	}
	
	public void stopSong() {
		if(fileLoaded) {
			this.restart = false;
			try {
				audioClip.stop();
			}
			catch(Exception e) {System.out.println("No musik running");}
			}
	}
	
	public void restartSong() {
		if((restart == false) && fileLoaded)
		{
			this.restart = true;
			try {
				audioClip.loop(Clip.LOOP_CONTINUOUSLY);

			}
			catch(Exception e) {System.out.println("No musik running");}
		}
	}
	
	private float getVolume() {
		if(fileLoaded) {
	    
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
	    
			float volume = (float) Math.pow(10f, gainControl.getValue() / 20f);
			volume = (float) Math.round(volume*100)/100;
			return volume;
	    }
		return 0f;
	}
	
	public void setVolume(float volume){
		if(fileLoaded) {
			if (volume < 0f || volume > 1f) {
				throw new IllegalArgumentException("Volume not valid: " + volume);}
			else {
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
				gainControl.setValue(20f * (float) Math.log10(volume));
			}
		}
		
	}
	
	public void incVolume() {
		if(fileLoaded) {
			float volume = getVolume();
			volume += steps;
			
			if(volume < 1.0f) {						
				volume = (float)Math.round(volume*100)/100;
				System.out.println(volume);
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			}
			else {
				volume = 1f;
				
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			}
		}

	}
	
	public void decVolume() {
		if(fileLoaded) {
			float volume = getVolume();
			volume -= steps;
			
			if(volume > 0.0f) {				
				volume = (float) Math.round(volume*100)/100;
				System.out.println(volume);
				
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			}
			else {
				volume = 0f;
				
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			}
		}
	}
	
	public void mute() {
		if(fileLoaded) {
			float volume = 0.0f;			
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			gainControl.setValue(20f * (float) Math.log10(volume));
		}

	}
}
