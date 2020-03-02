package niellebeck.game.sprites;

import java.util.Random;

import niellebeck.gameengine.GameEngine;
import niellebeck.gameengine.AnimatedSprite;
import niellebeck.gameengine.Animation;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class Enemy extends AnimatedSprite {
	private static final int SPEED = 1;
	private static final int ATTACK_INTERVAL = 100;
	private static final int MAX_HP = 3;
	
	// TODO: Cut down on duplicated code with PlayerCharacter
	private static final int INVULNERABLE_TIME = 30;
	private static final int RECOIL_TIME = 5;
	private static final int RECOIL_SPEED = 8;
	
	private Random random;
	private Direction currentDir;
	private int movementTime;
	private int movementDuration;
	private int attackCounter;
	private int hp;
	
	private static final String ENEMY_IMAGE = "/sprites/enemy/enemy.png";
	
	// TODO: Cut down on duplicated code with PlayerCharacter
	private int invulnerableTimer;
	private boolean invulnerable;
	private Direction hitDir;
	
	public Enemy(int initX, int initY) {
		super(initX, initY, 40, 40);
		
		random = new Random();
		currentDir = Direction.NONE;
		movementTime = 0;
		movementDuration = 60;
		attackCounter = random.nextInt(ATTACK_INTERVAL);
		hp = MAX_HP;
		
		invulnerableTimer = INVULNERABLE_TIME;
		invulnerable = false;
		hitDir = Direction.NONE;
		
		setAnimation(new Animation(1, ENEMY_IMAGE));
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
		
		Direction moveDir = currentDir;
		int moveSpeed = SPEED;
		
		// TODO: Cut down on duplicated code with PlayerCharacter
		if (invulnerable) {
			invulnerableTimer--;
			if (invulnerableTimer <= 0) {
				invulnerable = false;
				invulnerableTimer = INVULNERABLE_TIME;
			}
			else if (invulnerableTimer >= INVULNERABLE_TIME - RECOIL_TIME) {
				moveDir = hitDir;
				moveSpeed = RECOIL_SPEED;
			}
		}
		
		this.setMove(moveDir, moveSpeed);
		
		attackCounter++;
		if (attackCounter >= ATTACK_INTERVAL) {
			attackCounter = 0;
			attack();
		}
		
		setFlickering(invulnerable);
		animate();
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
	
	public void damage(Sprite hitter) {
		if (!invulnerable) {
			hp -= 1;
			if (hp <= 0) {
				this.destroy();
			}
			
			// TODO: Cut down on duplicated code with PlayerCharacter
			invulnerable = true;
			hitDir = DirectionUtils.getDirectionBetween(hitter, this, true);
		}
	}
	
	@Override
	public void onCollideTilemap() { }
}
