package niellebeck.game.sprites;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import niellebeck.game.MyGameLogic;
import niellebeck.gameengine.Animation;
import niellebeck.gameengine.EventManager;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.GameEngine;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Logger;
import niellebeck.gameengine.MovingAnimatedSprite;
import niellebeck.gameengine.Sprite;

public class PlayerCharacter extends MovingAnimatedSprite {
	
	private static final int PLAYER_WIDTH = 40;
	private static final int PLAYER_HEIGHT = 40;
	
	public static final int SPEED = 4;
	private static final int RECOIL_SPEED = 8;
	
	// INVULNERABLE_TIME should be greater than or equal to RECOIL_TIME
	private static final int INVULNERABLE_TIME = 30;
	private static final int RECOIL_TIME = 5;
	
	private static final int SHOOTING_ANIMATION_TIME = 10;
	
	private final UUID shootingLeftAnimationState;
	private final UUID shootingRightAnimationState;
	private final UUID shootingUpAnimationState;
	private final UUID shootingDownAnimationState;
	
	private int invulnerableTimer;
	private boolean invulnerable;
	private Direction hitDir;
	
	private int shootingTimer;
	private boolean shooting;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, PLAYER_WIDTH, PLAYER_HEIGHT);
		invulnerableTimer = INVULNERABLE_TIME;
		invulnerable = false;
		hitDir = Direction.NONE;
		
		shootingTimer = SHOOTING_ANIMATION_TIME;
		shooting = false;
		
		Animation leftRightAnimation = new Animation(5,
				"/sprites/stickfigure/standing.png",
				"/sprites/stickfigure/walking0.png",
				"/sprites/stickfigure/walking1.png");
		Animation standingAnimation = new Animation(1, "/sprites/stickfigure/standing.png");
		Animation shootingLeftAnimation = new Animation(1, "/sprites/stickfigure/shooting-left.png");
		Animation shootingRightAnimation = new Animation(1, "/sprites/stickfigure/shooting-right.png");
		Animation shootingUpAnimation = new Animation(1, "/sprites/stickfigure/shooting-up.png");
		Animation shootingDownAnimation = new Animation(1, "/sprites/stickfigure/shooting-down.png");

		registerMovingLeftAnimationState(leftRightAnimation);
		registerMovingRightAnimationState(leftRightAnimation);
		registerStandingAnimationState(standingAnimation);
		registerMovingUpAnimationState(standingAnimation);
		registerMovingDownAnimationState(standingAnimation);
		
		shootingLeftAnimationState = registerAnimationState(shootingLeftAnimation);
		shootingRightAnimationState = registerAnimationState(shootingRightAnimation);
		shootingUpAnimationState = registerAnimationState(shootingUpAnimation);
		shootingDownAnimationState = registerAnimationState(shootingDownAnimation);
		
		initializeMovingAnimation();
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
		
		if (shooting) {
			shootingTimer--;
			if (shootingTimer <= 0) {
				shooting = false;
				shootingTimer = SHOOTING_ANIMATION_TIME;
				clearAnimationStateOverride();
			}
		}
		
		if (moveDir != Direction.NONE) {
			move(moveDir, moveSpeed);
		}
		
		setFlickering(invulnerable);
		animate();
	}
	
	public void animateShooting(Direction dir) {
		UUID animationState = null;
		switch (dir) {
		case LEFT:
			animationState = shootingLeftAnimationState;
			break;
		case RIGHT:
			animationState = shootingRightAnimationState;
			break;
		case UP:
			animationState = shootingUpAnimationState;
			break;
		case DOWN:
			animationState = shootingDownAnimationState;
			break;
		default:
			Logger.warning("Tried to animate shooting in invalid direction " + dir);
		}
		overrideAnimationState(animationState);
		shooting = true;
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
