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
	
	protected AnimationState animationState;
	protected Map<AnimationState, Animation> animations;
	
	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/stickfigure/standing.png");
		speed = 2;
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
	public void update(KeyboardInput keyboard, List<Sprite> objects) {
		Direction moveDir = keyboard.getArrowKeyDirection();
		img = animate(moveDir);
		if (moveDir != Direction.NONE) {
			move(moveDir, objects);
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
}
