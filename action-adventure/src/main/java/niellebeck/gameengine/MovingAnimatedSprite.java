package niellebeck.gameengine;

import java.util.UUID;

/**
 * A special type of AnimatedSprite that supports five animation states
 * corresponding to standing still and moving in the four cardinal directions.
 * It automatically changes its animation state when it starts or stops moving
 * unless the animation state is explicitly overridden.
 */
public abstract class MovingAnimatedSprite extends AnimatedSprite {

	private UUID movingLeftAnimationState;
	private UUID movingRightAnimationState;
	private UUID movingUpAnimationState;
	private UUID movingDownAnimationState;
	private UUID standingAnimationState;
	
	private boolean override;
	
	public MovingAnimatedSprite(int x, int y, int width, int height) {
		super(x, y, width, height);
		override = false;
	}
	
	@Override
	public void move(Direction dir, int distance) {
		super.move(dir, distance);
		setMovingAnimation(dir);
	}
	
	public void overrideAnimationState(UUID animationState) {
		setAnimationState(animationState);
		override = true;
	}
	
	public void clearOverride() {
		override = false;
	}
	
	public void registerMovingLeftAnimationState(Animation animation) {
		movingLeftAnimationState = registerAnimationState(animation);
	}
	
	public void registerMovingRightAnimationState(Animation animation) {
		movingRightAnimationState = registerAnimationState(animation);
	}
	
	public void registerMovingUpAnimationState(Animation animation) {
		movingUpAnimationState = registerAnimationState(animation);
	}
	
	public void registerMovingDownAnimationState(Animation animation) {
		movingDownAnimationState = registerAnimationState(animation);
	}
	
	public void registerStandingAnimationState(Animation animation) {
		standingAnimationState = registerAnimationState(animation);
	}
	
	/**
	 * This method is necessary because we would like to have the Sprite's
	 * initial animation state be its "standing" animation state, but because
	 * this class accepts its animation states from separate method calls
	 * instead of in its constructor, we don't know for sure when the standing
	 * animation state will actually be defined.
	 */
	public void initializeMovingAnimation() {
		setAnimationState(standingAnimationState);
	}
	
	private void setMovingAnimation(Direction dir) {
		if (override) {
			return;
		}
		
		Direction componentDir = DirectionUtils.getComponentDirections(dir).get(0);
		UUID targetState = null;
		switch (componentDir) {
		case LEFT:
			targetState = movingLeftAnimationState;
			break;
		case RIGHT:
			targetState = movingRightAnimationState;
			break;
		case UP:
			targetState = movingUpAnimationState;
			break;
		case DOWN:
			targetState = movingDownAnimationState;
			break;
		default:
			targetState = standingAnimationState;
		}
		if (targetState != null) {
			setAnimationState(targetState);
		}
	}

}
