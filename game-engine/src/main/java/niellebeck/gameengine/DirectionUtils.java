package niellebeck.gameengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DirectionUtils {
	
	private static Random random;
	
	public static List<Direction> getComponentDirections(Direction dir) {
		List<Direction> result = new ArrayList<Direction>(); 
		if (dir == Direction.UP_LEFT) {
			result.add(Direction.UP);
			result.add(Direction.LEFT);
		}
		else if (dir == Direction.UP_RIGHT) {
			result.add(Direction.UP);
			result.add(Direction.RIGHT);
		}
		else if (dir == Direction.DOWN_LEFT) {
			result.add(Direction.DOWN);
			result.add(Direction.LEFT);
		}
		else if (dir == Direction.DOWN_RIGHT) {
			result.add(Direction.DOWN);
			result.add(Direction.RIGHT);
		}
		else {
			result.add(dir);
		}
		return result;
	}
	
	public static Direction getOppositeDirection(Direction dir) {
		switch(dir) {
		case UP:
			return Direction.DOWN;
		case DOWN:
			return Direction.UP;
		case LEFT:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.LEFT;
		case UP_RIGHT:
			return Direction.DOWN_LEFT;
		case DOWN_RIGHT:
			return Direction.UP_LEFT;
		case UP_LEFT:
			return Direction.DOWN_RIGHT;
		case DOWN_LEFT:
			return Direction.UP_RIGHT;
		case NONE:
			return Direction.NONE;
		default:
			return Direction.NONE;
		}
	}
	
	public static Direction getDirectionBetween(Sprite fromSprite, Sprite toSprite, boolean cardinalOnly) {
		int xDiff = toSprite.getX() - fromSprite.getX();
		int yDiff = toSprite.getY() - fromSprite.getY();
		
		if (cardinalOnly) {
			if (Math.abs(xDiff) > Math.abs(yDiff)) {
				yDiff = 0;
			}
			else {
				xDiff = 0;
			}
		}
		
		if (xDiff > 0 && yDiff > 0) {
			return Direction.DOWN_RIGHT;
		}
		else if (xDiff > 0 && yDiff < 0) {
			return Direction.UP_RIGHT;
		}
		else if (xDiff < 0 && yDiff > 0) {
			return Direction.DOWN_LEFT;
		}
		else if (xDiff < 0 && yDiff < 0) {
			return Direction.UP_LEFT;
		}
		else if (xDiff > 0 && yDiff == 0) {
			return Direction.RIGHT;
		}
		else if (xDiff < 0 && yDiff == 0) {
			return Direction.LEFT;
		}
		else if (xDiff == 0 && yDiff > 0) {
			return Direction.DOWN;
		}
		else if (xDiff == 0 && yDiff < 0) {
			return Direction.UP;
		}
		else { // xDiff == 0 && yDiff == 0
			return Direction.NONE;
		}
	}
	
	public static Direction getRandomCardinalDirection() {
		if (random == null) {
			random = new Random();
		}
		
		Direction dir = Direction.NONE;
		
		int randInt = random.nextInt(4);
		
		switch(randInt) {
		case 0:
			dir = Direction.LEFT;
			break;
		case 1:
			dir = Direction.RIGHT;
			break;
		case 2:
			dir = Direction.UP;
			break;
		case 3:
			dir = Direction.DOWN;
			break;
		}
		
		assert dir != Direction.NONE;
		
		return dir;
	}
}
