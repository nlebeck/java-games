package niellebeck.game.sprites;

import java.util.Random;

import niellebeck.gameengine.GameEngine;
import niellebeck.game.sprites.movebehaviors.ConstantMoveBehavior;
import niellebeck.game.sprites.movebehaviors.RandomMoveBehavior;
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
	
	private static final int INVULNERABLE_TIME = 30;
	private static final int RECOIL_TIME = 5;
	private static final int RECOIL_SPEED = 8;
	
	private Random random;
	private int attackCounter;
	private int hp;
	
	private static final String ENEMY_IMAGE = "/sprites/enemy/enemy.png";
	
	// TODO: Cut down on duplicated code with PlayerCharacter?
	private int invulnerableTimer;
	private boolean invulnerable;
	
	public Enemy(int initX, int initY) {
		super(initX, initY, 40, 40);
		
		random = new Random();
		attackCounter = random.nextInt(ATTACK_INTERVAL);
		hp = MAX_HP;
		
		invulnerableTimer = INVULNERABLE_TIME;
		invulnerable = false;
		
		setStaticMoveBehavior(new RandomMoveBehavior(SPEED));
		
		setAnimation(new Animation(1, ENEMY_IMAGE));
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		// TODO: Cut down on duplicated code with PlayerCharacter?
		if (invulnerable) {
			invulnerableTimer--;
			if (invulnerableTimer <= 0) {
				invulnerable = false;
				invulnerableTimer = INVULNERABLE_TIME;
			}
		}
		
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
			
			invulnerable = true;
			Direction hitDir = DirectionUtils.getDirectionBetween(hitter, this, true);
			this.setTimedMoveBehavior(new ConstantMoveBehavior(hitDir, RECOIL_SPEED, RECOIL_TIME));
		}
	}
	
	@Override
	public void onCollideTilemap() { }
}
