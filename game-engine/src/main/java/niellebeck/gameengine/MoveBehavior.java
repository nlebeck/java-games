package niellebeck.gameengine;

/**
 * A reusable package of private state and update logic that specifies
 * the distance and direction of a Sprite's movement each frame.
 */
public interface MoveBehavior {
	Direction getMoveDirection();
	int getMoveDistance();
	void update(KeyboardInput keyboard);
	boolean shouldAnimate();
}
