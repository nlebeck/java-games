package niellebeck.game.sprites;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import niellebeck.game.MyGameLogic;
import niellebeck.gameengine.AnimatedSprite;
import niellebeck.gameengine.Animation;
import niellebeck.gameengine.EventManager;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.GameEngine;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class PlayerCharacter extends AnimatedSprite {
	
	private static final int PLAYER_WIDTH = 40;
	private static final int PLAYER_HEIGHT = 40;
	
	public static final int SPEED = 4;
	private static final int RECOIL_SPEED = 8;
	
	// INVULNERABLE_TIME should be greater than or equal to RECOIL_TIME
	private static final int INVULNERABLE_TIME = 30;
	private static final int RECOIL_TIME = 5;
	
	private static final int ANIMATION_STATE_STANDING = 0;
	private static final int ANIMATION_STATE_MOVING_LEFT = 1;
	private static final int ANIMATION_STATE_MOVING_RIGHT = 2;
	private static final int ANIMATION_STATE_MOVING_UP = 3;
	private static final int ANIMATION_STATE_MOVING_DOWN = 4;
	
	private int invulnerableTimer;
	private boolean invulnerable;
	private Direction hitDir;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, PLAYER_WIDTH, PLAYER_HEIGHT);
		invulnerableTimer = INVULNERABLE_TIME;
		invulnerable = false;
		hitDir = Direction.NONE;
		
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
	public void update(KeyboardInput keyboard) {
		Direction moveDir = keyboard.getArrowKeyDirection();
		int moveSpeed = SPEED;
		
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
		
		if (moveDir != Direction.NONE) {
			move(moveDir, moveSpeed);
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
	
	public void onEnemyHit(Sprite hitter) {
		if (!invulnerable) {
			MyGameLogic gameLogic = (MyGameLogic)GameEngine.getGameEngine().getGameLogic();
			gameLogic.damagePlayer();
			invulnerable = true;
			hitDir = DirectionUtils.getDirectionBetween(hitter, this, true);
		}
	}
}
