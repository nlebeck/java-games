package niellebeck.gameengine;

import java.awt.Image;
import java.io.IOException;

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
}
