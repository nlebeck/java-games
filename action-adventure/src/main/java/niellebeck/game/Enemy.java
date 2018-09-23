package niellebeck.game;

import java.util.List;
import java.util.Random;

public class Enemy extends Sprite {
	final int SPEED = 1;
	
	Direction currentDir;
	int movementTime;
	int movementDuration;
	
	public Enemy(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/enemy/enemy.png");
		
		currentDir = Direction.NONE;
		movementTime = 0;
		movementDuration = 60;
	}
	
	@Override
	public void update(KeyboardInput keyboard, CollisionManager collisionManager) {
		movementTime += 1;
		if (movementTime >= movementDuration) {
			Random random = new Random();
			
			movementTime = 0;
			movementDuration = random.nextInt(60) + 60;
			
			if (currentDir == Direction.NONE) {
				currentDir = DirectionUtils.getRandomCardinalDirection();
			}
			else {
				currentDir = Direction.NONE;
			}
		}
		
		this.move(currentDir, SPEED, collisionManager);
	}
}
