package niellebeck.game.sprites;

import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;

public class NPC extends Sprite {

	public NPC(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/initial_npc/initial_npc.png");
	}

	@Override
	public void update(KeyboardInput keyboard) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollideTilemap() {
		// TODO Auto-generated method stub

	}
}
