package model;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;



public class musicPlayer {
	
	private Clip audioClip;
	private boolean restart;
	musicPlayer(){}
	
	//exempel på url:er
	public void cheat() {		
		
		//playMusic4("G:\\firefox\\eclipse object ori\\Javatris2\\Javatris\\src\\songs\\Tetris99 Game Theme.wav");	Lokal dator
		//playMusic4("Javatris\\\\src\\\\songs\\\\Tetris99 Game Theme.wav"); // Vet inte varför det har blivit fler \\ det var inte fyra från början
		playMusic("Javatris/src/songs/Tetris99 Game Theme.wav"); // Denna funkar också		

	}
	
	// funkar
	public void playMusic(String fp) {

		try {
			File audioFile = new File(fp);						
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			
			audioClip = AudioSystem.getClip();         
			audioClip.open(audioStream);    
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);           
		}
		catch(Exception e){
			
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	public void stopSong() {
		this.restart = false;
		try {
		audioClip.stop();
		}
		catch(Exception e) {System.out.println("No musik running");}
	}
	
	public void restartSong() {
		if(restart == false)
		this.restart = true;
		try {
				audioClip.loop(Clip.LOOP_CONTINUOUSLY);

		}
		catch(Exception e) {System.out.println("No musik running");}
	}	

}
