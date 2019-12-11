package niellebeck.gameengine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AnimatedSprite extends Sprite {

	private UUID animationState = null;
	private Map<UUID, Animation> animationMap = new HashMap<UUID, Animation>();
	
	private boolean isFlickering = false;
	private int flickerCounter = 0;
	
	public AnimatedSprite(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void animate() {
		if (animationState == null) {
			return;
		}
		
		UUID prevAnimationState = animationState;
		
		if (!prevAnimationState.equals(animationState)) {
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
	
	public void setAnimationState(UUID animationState) {
		this.animationState = animationState;
	}
	
	public UUID registerAnimationState(Animation animation) {
		UUID animationState = UUID.randomUUID();
		animationMap.put(animationState, animation);
		return animationState;
	}
	
	public void setFlickering(boolean isFlickering) {
		this.isFlickering = isFlickering;
		
		if (!this.isFlickering) {
			flickerCounter = 0;
		}
	}
}
