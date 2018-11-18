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
