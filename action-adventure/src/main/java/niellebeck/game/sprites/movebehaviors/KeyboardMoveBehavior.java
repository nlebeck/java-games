package niellebeck.game.sprites.movebehaviors;

import niellebeck.gameengine.Direction;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.MoveBehavior;

/**
 * A MoveBehavior based on keyboard input.
 */
public class KeyboardMoveBehavior implements MoveBehavior {

	private Direction moveDir;
	private int moveDist;
	
	public KeyboardMoveBehavior(int speed) {
		moveDir = Direction.NONE;
		moveDist = speed;
	}
	
	@Override
	public Direction getMoveDirection() {
		return moveDir;
	}

	@Override
	public int getMoveDistance() {
		return moveDist;
	}

	@Override
	public void update(KeyboardInput keyboard) {
		moveDir = keyboard.getArrowKeyDirection();
	}
	
	@Override
	public boolean shouldAnimate() {
		return true;
	}
}
