package niellebeck.game.sprites;

import java.util.Random;

import niellebeck.gameengine.GameEngine;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class Enemy extends Sprite {
	private final int SPEED = 1;
	private final int ATTACK_INTERVAL = 100;
	
	private Random random;
	private Direction currentDir;
	private int movementTime;
	private int movementDuration;
	private int attackCounter;
	
	public Enemy(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/enemy/enemy.png");
		
		random = new Random();
		currentDir = Direction.NONE;
		movementTime = 0;
		movementDuration = 60;
		attackCounter = random.nextInt(ATTACK_INTERVAL);
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
		
		this.move(currentDir, SPEED);
		
		attackCounter++;
		if (attackCounter >= ATTACK_INTERVAL) {
			attackCounter = 0;
			attack();
		}
	}
	
	public void attack() {
		int verticalOffset = this.getHeight() + Bullet.BULLET_HEIGHT;
		int horizontalOffset = this.getWidth() + Bullet.BULLET_WIDTH;
		
		Bullet leftBullet = new Bullet(this.getX() - horizontalOffset, this.getY(), Direction.LEFT);
		Bullet rightBullet = new Bullet(this.getX() + horizontalOffset, this.getY(), Direction.RIGHT);
		Bullet topBullet = new Bullet(this.getX(), this.getY() - verticalOffset, Direction.UP);
		Bullet bottomBullet = new Bullet(this.getX(), this.getY() + verticalOffset, Direction.DOWN);
		GameEngine.getGameEngine().addSprite(leftBullet);
		GameEngine.getGameEngine().addSprite(rightBullet);
		GameEngine.getGameEngine().addSprite(topBullet);
		GameEngine.getGameEngine().addSprite(bottomBullet);
	}
	
	@Override
	public void onCollideTilemap() { }
}
