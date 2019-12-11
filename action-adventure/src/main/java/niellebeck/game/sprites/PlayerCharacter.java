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
		registerMovingLeftAnimationState(leftRightAnimation);
		registerMovingRightAnimationState(leftRightAnimation);
		registerStandingAnimationState(standingAnimation);
		registerMovingUpAnimationState(standingAnimation);
		registerMovingDownAnimationState(standingAnimation);
		
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
		
		if (moveDir != Direction.NONE) {
			move(moveDir, moveSpeed);
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
