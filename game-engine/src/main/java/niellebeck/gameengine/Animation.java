package niellebeck.gameengine;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {
	private List<Image> images;
	private int imageIndex;
	private int timer;
	private int ticksPerImage;
	
	public Animation(int ticksPerImage, List<Image> images) {
		init(ticksPerImage, images);
	}
	
	public Animation(int ticksPerImage, String...strings) {
		List<Image> images = new ArrayList<Image>();
		for (String imagePath : strings) {
			images.add(ResourceLoader.loadImage(imagePath));
		}
		init(ticksPerImage, images);
	}
	
	private void init(int ticksPerImage, List<Image> images) {
		this.images = images;
		this.imageIndex = 0;
		this.timer = 0;
		this.ticksPerImage = ticksPerImage;
	}
	
	public Image animate() {
		if (images.size() == 1) {
			return images.get(0);
		}
		else {
		timer++;
			if (timer >= ticksPerImage) {
				timer = 0;
				imageIndex++;
			}
			if (imageIndex >= images.size()) {
				imageIndex = 0;
			}
			return images.get(imageIndex);
		}
	}
	
	public void reset() {
		imageIndex = 0;
	}
	
	public static Image getBlankImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	}
}
