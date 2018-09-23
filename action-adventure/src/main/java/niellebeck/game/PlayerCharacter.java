package niellebeck.game;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerCharacter extends Sprite {

	enum AnimationState {
		STANDING,
		MOVING_LEFT,
		MOVING_RIGHT,
		MOVING_UP,
		MOVING_DOWN
	}
	
	private static final int PLAYER_WIDTH = 40;
	private static final int PLAYER_HEIGHT = 40;
	
	private static final int SPEED = 4;
	private static final int MAX_HP = 20;
	private static final int INVULNERABLE_TIME = 30;
	
	protected AnimationState animationState;
	protected Map<AnimationState, Animation> animations;
	private int hp;
	private int invulnerableTimer;
	private boolean invulnerable;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, PLAYER_WIDTH, PLAYER_HEIGHT, "/sprites/stickfigure/standing.png");
		hp = MAX_HP;
		animationState = AnimationState.STANDING;
		invulnerableTimer = INVULNERABLE_TIME;
		invulnerable = false;
		
		animations = new HashMap<AnimationState, Animation>();
		Animation leftRightAnimation = new Animation(5,
				"/sprites/stickfigure/standing.png",
				"/sprites/stickfigure/walking0.png",
				"/sprites/stickfigure/walking1.png");
		Animation standingAnimation = new Animation(1, "/sprites/stickfigure/standing.png");
		animations.put(AnimationState.MOVING_LEFT, leftRightAnimation);
		animations.put(AnimationState.MOVING_RIGHT, leftRightAnimation);
		animations.put(AnimationState.STANDING, standingAnimation);
		animations.put(AnimationState.MOVING_UP, standingAnimation);
		animations.put(AnimationState.MOVING_DOWN, standingAnimation);
	}
	
	@Override
	public void update(KeyboardInput keyboard, CollisionManager collisionManager) {
		Direction moveDir = keyboard.getArrowKeyDirection();
		img = animate(moveDir);
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
	}
	
	protected Image animate(Direction dir) {
		AnimationState prevAnimationState = animationState;
		
		if (dir == Direction.LEFT || dir == Direction.UP_LEFT || dir == Direction.DOWN_LEFT) {
			animationState = AnimationState.MOVING_LEFT;
		}
		else if (dir == Direction.RIGHT || dir == Direction.UP_RIGHT || dir == Direction.DOWN_RIGHT) {
			animationState = AnimationState.MOVING_RIGHT;
		}
		else {
			animationState = AnimationState.STANDING;
		}
		
		if (prevAnimationState != animationState) {
			animations.get(animationState).reset();
		}
		Image animationImage = animations.get(animationState).animate();
		
		if (invulnerable) {
			if (invulnerableTimer % 2 == 0) {
				animationImage = Animation.getBlankImage(PLAYER_WIDTH, PLAYER_HEIGHT);
			}
		}
		
		return animationImage;
	}
	
	public int getHp() {
		return hp;
	}
	
	@Override
	public void onCollide(Sprite sprite) {
		if (sprite.getClass() == Enemy.class) {
			if (!invulnerable) {
				hp = (hp - 1 >= 0) ? hp - 1 : 0;
				invulnerable = true;
			}
		}
	}
}
