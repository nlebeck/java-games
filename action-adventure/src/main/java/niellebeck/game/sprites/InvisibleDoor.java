package niellebeck.game.sprites;

import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class InvisibleDoor extends Sprite {
	
	private static final int INVISIBLE_DOOR_WIDTH = 40;
	private static final int INVISIBLE_DOOR_HEIGHT = 40;

	public InvisibleDoor(int initX, int initY) {
		super(initX, initY, INVISIBLE_DOOR_WIDTH, INVISIBLE_DOOR_HEIGHT);
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		
	}

	@Override
	public void onCollideTilemap() {
		
	}

}
