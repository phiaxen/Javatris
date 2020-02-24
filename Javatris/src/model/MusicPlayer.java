package model;

import java.io.File;
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



public class MusicPlayer {
	
	private Clip audioClip;
	private boolean restart;
	private final float MaxSteps = 20; //Even numbers only
	private final float steps = 1/MaxSteps;
	
	
	private boolean fileLoaded = false;
	
	public MusicPlayer (int choice) {		
		switch(choice) {
		case 1: playMusic("/songs/Tetris Game Theme1.wav"); break;
		case 2: playMusic("/songs/Tetris99 Game Theme1.wav"); break;
		case 3: playMusic("/songs/08 Dave Rodgers - Deja Vu.wav"); break;
		}	
	}
	
	// funkar
	public void playMusic(String fp) {
		
		if(fileLoaded){
			audioClip.stop();
			audioClip.flush();
		}
		
														
		URL audioFile = SfxManager.class.getResource(fp);	
		
		try {
								
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);				
			
			audioClip = AudioSystem.getClip();         
			audioClip.open(audioStream);    
			
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);  
			fileLoaded = true;
		}
		catch(Exception e){
			
			JOptionPane.showMessageDialog(null, "Error with audio loading");
		}
		setVolume(0.2f);
	}
	
	/*	borde kanske sl� ihop denna med playMusic men det verkade jobbigare
	 *  �n vad det var v�rt.
	 * 
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
			fileLoaded = true;
		}
		catch(Exception e){
			
			JOptionPane.showMessageDialog(null, "Error with audio loading");
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
	
	/*	Om du kallar p� denna och har en pause eller breakpoint 
	 * 	kommer rutan kanske �ppnas i bakgrunden och man m�ste
	 * 	minimera sina f�nster
	 */
	public void playFile() {
		
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
		  
        // invoke the showsSaveDialog function to show the save dialog 
        int r = j.showSaveDialog(null); 

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
	
	public void reset() {
		if(fileLoaded) {
			this.restart = true;
			try {
				audioClip.setFramePosition(0);
				audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			catch(Exception e) {System.out.println("No musik running");}
		}
	}
	
}
