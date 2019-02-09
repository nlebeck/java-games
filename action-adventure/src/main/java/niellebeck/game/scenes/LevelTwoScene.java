package niellebeck.game.scenes;

import niellebeck.game.sprites.Door;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;
import niellebeck.gameengine.Tilemap;

public class LevelTwoScene extends GameScene {

	private PlayerCharacter playerChar;
	private Door door;
	
	@Override
	public void init() {
		playerChar = new PlayerCharacter(300, 260);
		door = new Door(300, 300);
		
		getGameEngine().addSprite(playerChar);
		getGameEngine().addSprite(door);
	}

	@Override
	public Tilemap getTilemap() {
		return new Tilemap("/tilemap_level_two.txt");
	}

	@Override
	public void update(KeyboardInput keyboard) {
		
	}

	@Override
	public Sprite getPlayerCharacter() {
		return playerChar;
	}

}
