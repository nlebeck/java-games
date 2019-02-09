package niellebeck.game.sprites;

import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class Door extends Sprite {
	private static final int DOOR_WIDTH = 40;
	private static final int DOOR_HEIGHT = 40;
	
	public Door(int initX, int initY) {
		super(initX, initY, DOOR_WIDTH, DOOR_HEIGHT, "/sprites/door/door.png");
	}

	@Override
	public void update(KeyboardInput keyboard) {
		
	}

	@Override
	public void onCollideTilemap() {
		
	}
}
