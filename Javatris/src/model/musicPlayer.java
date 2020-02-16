package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JOptionPane;


import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class musicPlayer {
/*	man skulle kunna g�ra om urlen till ett obejct om ber 
 *  anv�ndaren om hela filv�gen i ett f�nster
 *  filens namn i ett annat(man vill kunna visa namnet i en lista)
 *  och l�tens l�ngd i sekunder f�r jag kommer inte p� n�got praktiskt s�tt att loppa detta
 * 
 */
	private AudioStream song;
	private boolean run = true;
	
	musicPlayer(){}
	
	public void main() {
		// TODO Auto-generated method stub
		int l = 1;
		
		
		//while(true) {
		playMusic("Javatris\\\\src\\\\music\\\\Tetris99 Game Theme.wav"); // Vet inte varf�r det har blivit fler \\ det var inte fyra fr�n b�rjan
	//	playMusic("Javatris/src/music/Tetris99 Game Theme.wav"); // Denna funkar ocks�
//		try{
//			Thread.sleep(298000);
//			}catch(InterruptedException e) {
//				System.out.println("hej");
//			}
//		System.out.println("loop"+l);
//		l++;
//		}
		
		
		//Javatris\\src\\music\\Tetris99 Game Theme.wav
		
		//playMusic("G:\\firefox\\eclipse object ori\\Javatris2\\Javatris\\src\\music\\Tetris99 Game Theme.wav"); Denna funkar p� min dator
		// 
		// Tetris99 Game Theme.mp3
	}
	
	// Denna funkar men bara med .wav filer 
	public void playMusic(String fp) {
		InputStream music;
		int l = 1;
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fp));
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			double durationInSeconds = (frames+0.0) / format.getFrameRate();  
			int dInS = (int) Math.round(durationInSeconds);
			
			
			
			
				
			while(run) {
				music = new FileInputStream(new File(fp));
				song = new AudioStream(music);	
				AudioPlayer.player.start(song);
				System.out.println("loop"+l);
				l++;	
				//Thread.sleep(5000);  //anv�ndsw f�r test
				Thread.sleep((dInS+3)*1000);
				AudioPlayer.player.stop(song);
			}			
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	// F�rs�k att loopa l�ten utan att anv�nda en sleep funktion i kallningen
	public void playMusic2(String fp) {
		InputStream music;
		ContinuousAudioDataStream loop;
		try {
			music = new FileInputStream(new File(fp));
			AudioStream song = new AudioStream(music);	
			AudioData audiodata = song.getData();
			loop = new ContinuousAudioDataStream(audiodata);
			AudioPlayer.player.start(loop);
			
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	// Vet inte om den funkar
	public void playMusic3(String fp) {
		Media music;
		try {
			music = new Media(new File(fp).toPath().toString());
			System.out.println("hej");
			MediaPlayer mediaPlayer = new MediaPlayer(music);
			mediaPlayer.play();		
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	// Vet inte om den funkar
	public void playMusic4(String fp) {
	
		try {
			AudioInputStream audioInputStream =AudioSystem.getAudioInputStream(new File(fp));
		     Clip clip = AudioSystem.getClip();
		     clip.open(audioInputStream);
		     clip.start( );		
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	
	public void stopSong() {
		this.run = false;
		try {
		AudioPlayer.player.stop(this.song);
		}
		catch(Exception e) {System.out.println("No musik running");}
	}
	
	

}
