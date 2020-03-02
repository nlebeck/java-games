package niellebeck.game.sprites.movebehaviors;

import java.util.Random;

import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.MoveBehavior;

/**
 * A MoveBehavior that moves about randomly.
 */
public class RandomMoveBehavior implements MoveBehavior {

	private Random random;
	private int speed;
	private Direction currentDir;
	private int movementTime;
	private int movementDuration;
	
	public RandomMoveBehavior(int speed) {
		random = new Random();
		this.speed = speed;
		currentDir = Direction.NONE;
		movementTime = 0;
		movementDuration = 60;
	}
	
	@Override
	public Direction getMoveDirection() {
		return currentDir;
	}

	@Override
	public int getMoveDistance() {
		return speed;
	}

	@Override
	public void update(KeyboardInput keyboard) {
		movementTime += 1;
		if (movementTime >= movementDuration) {
			movementTime = 0;
			movementDuration = random.nextInt(60) + 60;
			
			if (currentDir == Direction.NONE) {
				currentDir = DirectionUtils.getRandomCardinalDirection();
			}
			else {
				currentDir = Direction.NONE;
			}
		}
	}
	
	@Override
	public boolean shouldAnimate() {
		return true;
	}
}
