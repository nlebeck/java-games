package niellebeck.gameengine;

public abstract class AnimatedSprite extends Sprite {

	private Animation curAnimation = null;
	private boolean isFlickering = false;
	private int flickerCounter = 0;
	
	public AnimatedSprite(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void animate() {
		img = curAnimation.animate();
		
		if (isFlickering) {
			if (flickerCounter % 2 == 0) {
				img = Animation.getBlankImage(width, height);
			}
			flickerCounter++;
		}
	}
	
	public void setAnimation(Animation animation) {
		curAnimation = animation;
	}
	
	public void setFlickering(boolean isFlickering) {
		this.isFlickering = isFlickering;
		
		if (!this.isFlickering) {
			flickerCounter = 0;
		}
	}
}
