package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * SfxManager is a class that handles all sound effects.
 * 
 * @author Philip Axenhamn
 * @version 1.0
 * @since 2020-02-29
 */
public class SfxManager implements PropertyChangeListener {

	private Clip[] sfx;
	private boolean muted;

	/**
	 * Constructor of this class calls the function init.
	 */
	public SfxManager() {
		sfx = new Clip[4];
		init();
	}

	/**
	 * Loads each soundeffect from source file.
	 * Sets mute to false.
	 */
	private void init() {
		loadSound(0, "/soundEffects/sfx1.wav", 0.1f); // soundeffect 1
		loadSound(1, "/soundEffects/sfx2.1.wav", 1f); // soundeffect 2
		loadSound(2, "/soundEffects/sfx3.wav", 1f); // soundeffect 3
		loadSound(3, "/soundEffects/sfx4.wav", 0.3f); // soundeffect 4
		muted = false;
	}

	/**
	 * Loads a sound effect to the Clip array.
	 * 
	 * @param index the index of the array where this sound effect should be placed.
	 * @param filePath the file path ocf the sound effect
	 * @param volume the volume of the sound effect(0-1)
	 * @exception IOException
	 * @exception LineUnavailableException
	 * @exception UnsupportedAudioFileException
	 */
	private void loadSound(int index, String filePath, float volume) {
		try {			
			URL file1 = SfxManager.class.getResource(filePath);
			
			sfx[index] = AudioSystem.getClip();
			sfx[index].open(AudioSystem.getAudioInputStream(file1));
			FloatControl gainControl = (FloatControl) sfx[index].getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f * (float) Math.log10(volume));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		catch (LineUnavailableException e2) {
			e2.printStackTrace();
		}
		catch (UnsupportedAudioFileException e3) {
			e3.printStackTrace();
		}
	}

	/**
	 * Plays sound effect 1 if not muted and loads it again.
	 * Sound effect for movement.
	 */
	public void playSound1() {
		if (!muted) {
			
			sfx[0].start();
			loadSound(0, "/soundEffects/sfx1.wav", 0.1f);

		}
	}

	/**
	 * Plays sound effect 2 if not muted and loads it again.
	 * Sound effect for hit.
	 */
	public void playSound2() {
		if (!muted) {
			
			sfx[1].start();
			loadSound(1, "/soundEffects/sfx2.1.wav", 1f);
		}
	}

	/**
	 * Plays sound effect 3 if not muted and loads it again.
	 * Sound effect for lines cleared.
	 */
	public void playSound3() {
		if (!muted) {
			
			sfx[2].start();
			loadSound(2, "/soundEffects/sfx3.wav", 1f);
		}
	}

	/**
	 * Plays sound effect 4 if not muted and loads it again.
	 * Sound effect for level up.
	 */
	public void playSound4() {
		if (!muted) {
			
			sfx[3].start();
			loadSound(3, "/soundEffects/sfx4.wav", 0.3f);
		}
	}

	/**
	 * Sets if the sounds should play or not
	 * 
	 * @param mute true for muted, false not muted
	 */
	public void setMuted(boolean mute) {
		this.muted = mute;

	}

	/**
	 * Returns if sound effects are muted or not
	 * 
	 * @return true if muted, else false
	 */
	public boolean getMuted() {
		return this.muted;

	}

	/**
	 * Applies property-changes from observed objects
	 * 
	 * @param evt the fired event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (evt.getOldValue() != null) {
			if (property.equals("level")) {
				playSound4();
			}
			if (property.equals("lines cleared")) {
				playSound3();
			}
			if (property.equals("collisionY")) {
				playSound2();
			}
			if (property.equals("movedX")) {
				playSound1();
			}
		}
	}
}
