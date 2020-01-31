package niellebeck.gameengine;

/**
 * A special type of AnimatedSprite that supports five animation states
 * corresponding to standing still and moving in the four cardinal directions.
 * It automatically changes its animation state when it starts or stops moving
 * unless the animation state is explicitly overridden.
 * <p>
 * MovingAnimatedSprites have the following animation precedence order,
 * from highest to lowest precedence:
 * <p>
 * 1. Current active TimedAnimation (if any)
 * <br>
 * 2. Current active override Animation (if any)
 * <br>
 * 3. Current moving Animation
 */
public abstract class MovingAnimatedSprite extends AnimatedSprite {

	private Animation movingLeftAnimation;
	private Animation movingRightAnimation;
	private Animation movingUpAnimation;
	private Animation movingDownAnimation;
	private Animation standingAnimation;
	
	private boolean override;
	private Direction moveDir;
	
	public MovingAnimatedSprite(int x, int y, int width, int height) {
		super(x, y, width, height);
		override = false;
		moveDir = Direction.NONE;
	}
	
	@Override
	public void setMove(Direction dir, int distance) {
		super.setMove(dir, distance);
		moveDir = dir;
	}
	
	@Override
	public void animate() {
		setMovingAnimation(moveDir);
		super.animate();
		moveDir = Direction.NONE;
	}
	
	public void overrideAnimation(Animation animation) {
		setAnimation(animation);
		override = true;
	}
	
	public void clearAnimationOverride() {
		override = false;
	}
	
	public void registerMovingLeftAnimation(Animation animation) {
		movingLeftAnimation = animation;
	}
	
	public void registerMovingRightAnimation(Animation animation) {
		movingRightAnimation = animation;
	}
	
	public void registerMovingUpAnimation(Animation animation) {
		movingUpAnimation = animation;
	}
	
	public void registerMovingDownAnimation(Animation animation) {
		movingDownAnimation = animation;
	}
	
	public void registerStandingAnimation(Animation animation) {
		standingAnimation = animation;
	}
	
	/**
	 * This method is necessary because we would like to have the Sprite's
	 * initial animation state be its "standing" animation state, but because
	 * this class accepts its animation states from separate method calls
	 * instead of in its constructor, we don't know for sure when the standing
	 * animation state will actually be defined.
	 */
	public void initializeMovingAnimation() {
		if (standingAnimation == null) {
			Logger.warning("Missing standing animation");
		}
		else {
			setAnimation(standingAnimation);
		}
	}
	
	private void setMovingAnimation(Direction dir) {
		if (override) {
			return;
		}
		
		Direction componentDir = DirectionUtils.getComponentDirections(dir).get(0);
		Animation targetAnimation = null;
		switch (componentDir) {
		case LEFT:
			targetAnimation = movingLeftAnimation;
			break;
		case RIGHT:
			targetAnimation = movingRightAnimation;
			break;
		case UP:
			targetAnimation = movingUpAnimation;
			break;
		case DOWN:
			targetAnimation = movingDownAnimation;
			break;
		default:
			targetAnimation = standingAnimation;
		}
		if (targetAnimation == null) {
			Logger.warning("Missing animation for Direction " + componentDir);
		}
		else {
			setAnimation(targetAnimation);
		}
	}

}
