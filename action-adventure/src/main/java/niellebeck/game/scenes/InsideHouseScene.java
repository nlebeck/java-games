package niellebeck.game.scenes;

import static niellebeck.gameengine.GameEngine.getGameEngine;

import niellebeck.game.sprites.InvisibleDoor;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.InteractionHandler;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;
import niellebeck.gameengine.Tilemap;

public class InsideHouseScene extends GameScene {

	private PlayerCharacter playerChar;
	private InvisibleDoor door;
	
	@Override
	public void init() {
		playerChar = new PlayerCharacter(200, 340);
		door = new InvisibleDoor(200, 380);
		
		getGameEngine().addSprite(playerChar);
		getGameEngine().addSprite(door);
		
		door.setInteractionHandler(new InteractionHandler() {

			@Override
			public void interact() {
				getGameEngine().changeScene(new LevelTwoScene(true));
			}

			@Override
			public String getInteractionMessage() {
				return "go outside";
			}
			
		});
	}

	@Override
	public Tilemap getTilemap() {
		return new Tilemap("/tilemap_inside_house.txt");
	}

	@Override
	public void update(KeyboardInput keyboard) {
		
	}

	@Override
	public Sprite getPlayerCharacter() {
		return playerChar;
	}

	@Override
	public int getPlayerCharacterSpeed() {
		return PlayerCharacter.SPEED;
	}

}
