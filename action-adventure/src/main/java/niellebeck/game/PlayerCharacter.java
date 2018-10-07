package niellebeck.game;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerCharacter extends AnimatedSprite {
	
	private static final int PLAYER_WIDTH = 40;
	private static final int PLAYER_HEIGHT = 40;
	
	private static final int SPEED = 4;
	private static final int MAX_HP = 20;
	private static final int INVULNERABLE_TIME = 30;
	
	private static final int ANIMATION_STATE_STANDING = 0;
	private static final int ANIMATION_STATE_MOVING_LEFT = 1;
	private static final int ANIMATION_STATE_MOVING_RIGHT = 2;
	private static final int ANIMATION_STATE_MOVING_UP = 3;
	private static final int ANIMATION_STATE_MOVING_DOWN = 4;
	
	private int hp;
	private int invulnerableTimer;
	private boolean invulnerable;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, PLAYER_WIDTH, PLAYER_HEIGHT);
		hp = MAX_HP;
		invulnerableTimer = INVULNERABLE_TIME;
		invulnerable = false;
		
		Animation leftRightAnimation = new Animation(5,
				"/sprites/stickfigure/standing.png",
				"/sprites/stickfigure/walking0.png",
				"/sprites/stickfigure/walking1.png");
		Animation standingAnimation = new Animation(1, "/sprites/stickfigure/standing.png");
		registerAnimationState(ANIMATION_STATE_MOVING_LEFT, leftRightAnimation);
		registerAnimationState(ANIMATION_STATE_MOVING_RIGHT, leftRightAnimation);
		registerAnimationState(ANIMATION_STATE_STANDING, standingAnimation);
		registerAnimationState(ANIMATION_STATE_MOVING_UP, standingAnimation);
		registerAnimationState(ANIMATION_STATE_MOVING_DOWN, standingAnimation);
		
		setAnimationState(ANIMATION_STATE_STANDING);
	}
	
	@Override
	public void update(KeyboardInput keyboard, CollisionManager collisionManager) {
		Direction moveDir = keyboard.getArrowKeyDirection();
		if (moveDir != Direction.NONE) {
			move(moveDir, SPEED, collisionManager);
		}
		
		if (hp <= 0) {
			this.destroy();
		}
		
		if (invulnerable) {
			invulnerableTimer--;
			if (invulnerableTimer <= 0) {
				invulnerable = false;
				invulnerableTimer = INVULNERABLE_TIME;
			}
		}
		
		if (moveDir == Direction.LEFT || moveDir == Direction.UP_LEFT || moveDir == Direction.DOWN_LEFT) {
			setAnimationState(ANIMATION_STATE_MOVING_LEFT);
		}
		else if (moveDir == Direction.RIGHT || moveDir == Direction.UP_RIGHT || moveDir == Direction.DOWN_RIGHT) {
			setAnimationState(ANIMATION_STATE_MOVING_RIGHT);
		}
		else {
			setAnimationState(ANIMATION_STATE_STANDING);
		}
		setFlickering(invulnerable);
		animate();
	}
	
	@Override
	public void onCollideTilemap() { }
	
	public int getHp() {
		return hp;
	}
	
	public void onEnemyHit() {
		if (!invulnerable) {
			hp = (hp - 1 >= 0) ? hp - 1 : 0;
			invulnerable = true;
		}
	}
}
