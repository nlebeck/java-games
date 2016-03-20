package niellebeck.game;

import java.util.ArrayList;
import java.util.List;

enum Direction {
	LEFT, RIGHT, UP, DOWN,
	UP_LEFT, UP_RIGHT,
	DOWN_LEFT, DOWN_RIGHT,
	NONE
}

public class DirectionUtils {
	
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
}
