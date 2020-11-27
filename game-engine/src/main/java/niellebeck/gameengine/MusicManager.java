package niellebeck.gameengine;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicManager {
	
	private String currentTrack = "";
	
	public void changeMusicTrack(String track) {
		if (track.equals(currentTrack)) {
			return;
		}
		
		currentTrack = track;
		
		InputStream stream = ResourceLoader.openMusicStream(track);
		if (stream == null) {
			Logger.info("Music file not found: " + track);
			return;
		}
		
		/*
		 * I used this StackOverflow link to learn how to play WAV
		 * files in Java: https://stackoverflow.com/q/6045384.
		 */
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.setLoopPoints(0, -1);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (IOException e) {
			Logger.info("I/O exception when opening music file: " + e);
		} catch (UnsupportedAudioFileException e) {
			Logger.info("Unsupported music file: " + e);
		} catch (LineUnavailableException e) {
			Logger.info("LineUnavailableException when opening music file: " + e);
		}
	}
}
