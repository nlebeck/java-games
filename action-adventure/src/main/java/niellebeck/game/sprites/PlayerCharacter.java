package niellebeck.game.sprites;

import niellebeck.game.MyGameLogic;
import niellebeck.game.sprites.behaviors.Timer;
import niellebeck.game.sprites.movebehaviors.ConstantMoveBehavior;
import niellebeck.game.sprites.movebehaviors.KeyboardMoveBehavior;
import niellebeck.gameengine.Animation;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.GameEngine;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Logger;
import niellebeck.gameengine.MovingAnimatedSprite;
import niellebeck.gameengine.Sprite;
import niellebeck.gameengine.TimedAnimation;
import niellebeck.gameengine.TimedBehavior;

public class PlayerCharacter extends MovingAnimatedSprite {
	
	private static final int PLAYER_WIDTH = 40;
	private static final int PLAYER_HEIGHT = 40;
	
	public static final int SPEED = 4;
	private static final int RECOIL_SPEED = 8;
	
	// INVULNERABLE_TIME should be greater than or equal to RECOIL_TIME
	private static final int INVULNERABLE_TIME = 30;
	private static final int RECOIL_TIME = 5;
	
	private static final int SHOOTING_ANIM_TIME = 10;
	
	private final TimedAnimation shootingLeftAnimation;
	private final TimedAnimation shootingRightAnimation;
	private final TimedAnimation shootingUpAnimation;
	private final TimedAnimation shootingDownAnimation;
	
	private TimedBehavior invulnerableTimer;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		setStaticMoveBehavior(new KeyboardMoveBehavior(SPEED));
		
		invulnerableTimer = null;
		
		Animation leftRightAnimation = new Animation(5,
				"/sprites/stickfigure/standing.png",
				"/sprites/stickfigure/walking0.png",
				"/sprites/stickfigure/walking1.png");
		Animation upDownAnimation = new Animation(10,
				"/sprites/stickfigure/walking-vertical-0.png",
				"/sprites/stickfigure/walking-vertical-1.png");
		Animation standingAnimation = new Animation(1, "/sprites/stickfigure/standing.png");
		shootingLeftAnimation = new TimedAnimation(SHOOTING_ANIM_TIME, 1, "/sprites/stickfigure/shooting-left.png");
		shootingRightAnimation = new TimedAnimation(SHOOTING_ANIM_TIME, 1, "/sprites/stickfigure/shooting-right.png");
		shootingUpAnimation = new TimedAnimation(SHOOTING_ANIM_TIME, 1, "/sprites/stickfigure/shooting-up.png");
		shootingDownAnimation = new TimedAnimation(SHOOTING_ANIM_TIME, 1, "/sprites/stickfigure/shooting-down.png");

		registerMovingLeftAnimation(leftRightAnimation);
		registerMovingRightAnimation(leftRightAnimation);
		registerStandingAnimation(standingAnimation);
		registerMovingUpAnimation(upDownAnimation);
		registerMovingDownAnimation(upDownAnimation);
		
		initializeMovingAnimation();
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		if (invulnerableTimer != null && invulnerableTimer.isDone()) {
			invulnerableTimer = null;
		}
		
		setFlickering(invulnerableTimer != null);
		animate();
	}
	
	public void animateShooting(Direction dir) {
		TimedAnimation animation = null;
		switch (dir) {
		case LEFT:
			animation = shootingLeftAnimation;
			break;
		case RIGHT:
			animation = shootingRightAnimation;
			break;
		case UP:
			animation = shootingUpAnimation;
			break;
		case DOWN:
			animation = shootingDownAnimation;
			break;
		default:
			Logger.warning("Tried to animate shooting in invalid direction " + dir);
		}
		setTimedAnimation(animation);
	}
	
	@Override
	public void onCollideTilemap() { }
	
	public void onEnemyHit(Sprite hitter) {
		if (invulnerableTimer == null) {
			MyGameLogic gameLogic = (MyGameLogic)GameEngine.getGameEngine().getGameLogic();
			gameLogic.damagePlayer();
			invulnerableTimer = new Timer(INVULNERABLE_TIME);
			addTimedBehavior(invulnerableTimer);
			Direction hitDir = DirectionUtils.getDirectionBetween(hitter, this, true);
			this.setTimedMoveBehavior(new ConstantMoveBehavior(hitDir, RECOIL_SPEED, RECOIL_TIME));
		}
	}
}
