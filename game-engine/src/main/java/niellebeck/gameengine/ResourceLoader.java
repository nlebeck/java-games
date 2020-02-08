package niellebeck.gameengine;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ResourceLoader {
	public static Image loadImage(String path) {
		Image result = null;
		try {
			result = ImageIO.read(ResourceLoader.class.getResourceAsStream(path));
		} catch (IOException e) {
			Logger.panic("Error loading image " + path + ": "+ e);
		}
		return result;
	}
	
	public static InputStream openMusicStream(String path) {
		/*
		 * This StackOverflow answer showed me how to use a
		 * BufferedInputStream to avoid a "mark/reset not supported"
		 * IOException: https://stackoverflow.com/a/29777139.
		 */
		InputStream stream = ResourceLoader.class.getResourceAsStream(path);
		BufferedInputStream bufferedStream = new BufferedInputStream(stream);
		return bufferedStream;
	}
}
