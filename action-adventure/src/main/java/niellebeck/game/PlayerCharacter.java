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
	
	private static final int SPEED = 4;
	private static final int MAX_HP = 20;
	
	protected AnimationState animationState;
	protected Map<AnimationState, Animation> animations;
	private int hp;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/stickfigure/standing.png");
		hp = MAX_HP;
		animationState = AnimationState.STANDING;
		
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
	public void update(KeyboardInput keyboard, List<Sprite> sprites, Tilemap tilemap) {
		Direction moveDir = keyboard.getArrowKeyDirection();
		img = animate(moveDir);
		if (moveDir != Direction.NONE) {
			move(moveDir, SPEED, sprites, tilemap);
		}
		
		if (hp <= 0) {
			this.destroy();
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
		return animations.get(animationState).animate();
	}
	
	public int getHp() {
		return hp;
	}
	
	@Override
	public void onCollide(Sprite sprite) {
		if (sprite.getClass() == Enemy.class) {
			hp = (hp - 1 >= 0) ? hp - 1 : 0;
		}
	}
}
