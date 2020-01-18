package niellebeck.gameengine;

/**
 * A Sprite whose appearance is determined by an Animation. Concrete
 * instances of this class should call {@link #animate()} in their
 * {@link #update(KeyboardInput)} method.
 * <p>
 * An AnimatedSprite supports both normal Animations, which continue to
 * play indefinitely, and TimedAnimations, which play for a fixed
 * number of frames and then stop. Each AnimatedSprite has one active
 * Animation and at most one active TimedAnimation. If both types are
 * active, the TimedAnimation takes precedence.
 */
public abstract class AnimatedSprite extends Sprite {

	private Animation curAnimation = null;
	private TimedAnimation timedAnimation = null;
	private boolean isFlickering = false;
	private int flickerCounter = 0;
	
	public AnimatedSprite(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void animate() {
		Animation animation = curAnimation;
		if (timedAnimation != null) {
			if (timedAnimation.done()) {
				timedAnimation = null;
			}
			else {
				animation = timedAnimation;
			}
		}
		
		img = animation.animate();
		
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
	
	public void setTimedAnimation(TimedAnimation animation) {
		timedAnimation = animation;
		timedAnimation.reset();
	}
	
	public void setFlickering(boolean isFlickering) {
		this.isFlickering = isFlickering;
		
		if (!this.isFlickering) {
			flickerCounter = 0;
		}
	}
}
