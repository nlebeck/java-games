package niellebeck.game;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimatedSprite extends Sprite {

	private int animationState = -1;
	private Map<Integer, Animation> animationMap = new HashMap<Integer, Animation>();
	
	private boolean isFlickering = false;
	private int flickerCounter = 0;
	
	public AnimatedSprite(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void animate() {
		if (animationState < 0) {
			return;
		}
		
		int prevAnimationState = animationState;
		
		if (prevAnimationState != animationState) {
			animationMap.get(animationState).reset();
		}
		
		img = animationMap.get(animationState).animate();
		
		if (isFlickering) {
			if (flickerCounter % 2 == 0) {
				img = Animation.getBlankImage(width, height);
			}
			flickerCounter++;
		}
	}
	
	public void setAnimationState(int animationState) {
		this.animationState = animationState;
	}
	
	public void registerAnimationState(int animationState, Animation animation) {
		animationMap.put(animationState, animation);
	}
	
	public void setFlickering(boolean isFlickering) {
		this.isFlickering = isFlickering;
		
		if (!this.isFlickering) {
			flickerCounter = 0;
		}
	}
}
