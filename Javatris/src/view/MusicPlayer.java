package view;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 * MusicPlayer is a class that handles the background music
 * 
 * @author Tobias Mauritzon
 * @author Philip Axenhamn
 * @author Joachim Antfolk
 * @version 3.0
 * @since 2020-02-29
 */
public class MusicPlayer {

	private Clip audioClip;
	private final float MaxSteps = 20; // Even numbers only
	private final float steps = 1 / MaxSteps;
	private float globalVolume;
	private boolean fileLoaded = false;
	private boolean muted;

	/**
	 * MusicPlayer is the constructor for MusicPlayer.
	 * 
	 * @param choice 1-3 for prepared songs else you get an empty constructor and
	 *               you need to use playFile
	 */
	public MusicPlayer(int choice) {
		switch (choice) {
		case 1:
			loadMusic("/songs/Tetris Game Theme1.wav");
			break;
		case 2:
			loadMusic("/songs/Tetris99 Game Theme1.wav");
			break;
		case 3:
			loadMusic("/songs/08 Dave Rodgers - Deja Vu.wav");
			break;
		default:
			break;
		}
		muted = false;
	}

	/**
	 * Starts one of the prepared tacks.
	 * 
	 * @param choice 1-3 for prepared songs
	 */
	public void preparedMusic(int choice) {
		switch (choice) {
		case 1:
			loadMusic("/songs/Tetris Game Theme1.wav");
			break;
		case 2:
			loadMusic("/songs/Tetris99 Game Theme1.wav");
			break;
		case 3:
			loadMusic("/songs/08 Dave Rodgers - Deja Vu.wav");
			break;
		}
	}

	/**
	 * Loads a choosen song.
	 * 
	 * @param fp is the file path to the song as a string
	 */
	public void loadMusic(String fp) {

		if (fileLoaded) {
			audioClip.stop();
			audioClip.flush();
		}

		URL audioFile = MusicPlayer.class.getResource(fp);

		try {

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			audioClip = AudioSystem.getClip();
			audioClip.open(audioStream);

			if (fileLoaded) {
				setVolume(globalVolume);
			}

			if (!fileLoaded) {
				setVolume(0.4f); // start volume
			}

			fileLoaded = true;
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Starts the choosen song.
	 */
	public void play() {
		audioClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Loads and starts the song.
	 * 
	 * @param audioFile is the File you want to load
	 */
	public void playMusicFile(File audioFile) {

		if (fileLoaded) {
			audioClip.stop();
			audioClip.flush();
		}

		try {

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			audioClip = AudioSystem.getClip();
			audioClip.open(audioStream);

			audioClip.loop(Clip.LOOP_CONTINUOUSLY);

			if (fileLoaded) {
				setVolume(globalVolume);
			}

			if (!fileLoaded) {
				setVolume(0.4f); // start volume
			}

			fileLoaded = true;
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Error with audio loading");
		}

	}

	/**
	 * Stops the currently playing song.
	 */
	public void stop() {
		if (fileLoaded) {
			try {
				audioClip.stop();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "No music running");
			}
		}
	}

	/**
	 * Starts the current song over from the beginning
	 */
	public void restart() {
		audioClip.setFramePosition(0);
		play();
	}

	/**
	 * Gets the volume of the current song
	 * 
	 * @return type float volume between 0 and 1 in floating as point.
	 */
	public float getVolume() {
		if (fileLoaded) {

			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);

			float volume = (float) Math.pow(10f, gainControl.getValue() / 20f);
			volume = (float) Math.round(volume * 100) / 100;
			return volume;
		}
		return 0f;
	}

	/**
	 * Sets the volume of the current song
	 * 
	 * @param volume is a floating point between 0 and 1.
	 */
	public void setVolume(float volume) {
		muted = false;
		if (volume < 0f || volume > 1f) {
			throw new IllegalArgumentException("Volume not valid: " + volume);
		} else {
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f * (float) Math.log10(volume));
			globalVolume = volume;
		}

	}

	/**
	 * Increases the volume of the current song in 0.05 steps
	 */
	public void incVolume() {
		if (fileLoaded) {
			muted = false;
			// float volume = getVolume();
			float volume = globalVolume;
			volume += steps;

			if (volume < 1.0f) {
				volume = (float) Math.round(volume * 100) / 100;
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(volume));
				globalVolume = getVolume();
			} else {
				volume = 1f;

				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(volume));
				globalVolume = getVolume();
			}
		}

	}

	/**
	 * Decreases the volume of the current song in 0.05 steps
	 */
	public void decVolume() {
		if (fileLoaded) {
			muted = false;
			float volume = globalVolume;
			volume -= steps;

			if (volume > 0.0f) {
				volume = (float) Math.round(volume * 100) / 100;
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(volume));
				globalVolume = getVolume();
			} else {
				volume = 0f;

				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(volume));
				globalVolume = getVolume();
			}
		}
	}

	/**
	 * Mutes the current song
	 */
	public void mute() {
		if (fileLoaded) {
			if (muted) {
				setVolume(this.globalVolume);
				muted = false;
			} else {
				globalVolume = getVolume();
				FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(20f * (float) Math.log10(0.0f));
				muted = true;
			}
		}

	}

	/**
	 * Opens a JFileChooser and sends your selected .wav file to playMusicFile this
	 * is a way to lad local sound files in to the game
	 */
	public void playFile() {

		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		j.setFont(new Font("Arial", Font.BOLD, 20));
		j.setForeground(Color.WHITE);
		j.setBackground(Color.BLACK);
		// invoke the showsSaveDialog function to show the save dialog
		int r = j.showOpenDialog(null);

		// if the user selects a file
		if (r == JFileChooser.APPROVE_OPTION) {
			File path = new File(j.getSelectedFile().getAbsolutePath());

			if (path.getName().endsWith((".wav"))) {
				playMusicFile(j.getSelectedFile());
			} else {
				JOptionPane.showMessageDialog(null, "Wrong file type " + j.getSelectedFile().getAbsolutePath());
			}
		}
		// if the user cancelled the operation
		else
			JOptionPane.showMessageDialog(null, "The user cancelled the operation");
	}

}
