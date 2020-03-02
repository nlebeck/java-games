package niellebeck.game.sprites.movebehaviors;

import niellebeck.gameengine.Direction;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.TimedMoveBehavior;

/**
 * A MoveBehavior that moves in a single direction at constant speed.
 *
 */
public class ConstantMoveBehavior implements TimedMoveBehavior {
	
	private Direction dir;
	private int speed;
	private boolean isTimed;
	private int time;
	
	public ConstantMoveBehavior(Direction dir, int speed) { 
		this.dir = dir;
		this.speed = speed;
		isTimed = false;
		time = -1;
	}
	
	public ConstantMoveBehavior(Direction dir, int speed, int time) {
		this.dir = dir;
		this.speed = speed;
		isTimed = true;
		this.time = time;
	}
	
	public Direction getMoveDirection() {
		return dir;
	}
	
	public int getMoveDistance() {
		return speed;
	}

	@Override
	public void update(KeyboardInput keyboard) {
		time--;
	}

	@Override
	public boolean isDone() {
		if (isTimed) {
			return time < 0;
		}
		return false;
	}

	@Override
	public boolean shouldAnimate() {
		return false;
	}
}
