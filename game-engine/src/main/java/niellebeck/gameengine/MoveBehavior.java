package niellebeck.gameengine;

public interface MoveBehavior {
	Direction getMoveDirection();
	int getMoveDistance();
	void update(KeyboardInput keyboard);
	boolean shouldAnimate();
}
