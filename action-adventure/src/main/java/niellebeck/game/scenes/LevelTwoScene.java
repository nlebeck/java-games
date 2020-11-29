package niellebeck.game.scenes;

import static niellebeck.gameengine.GameEngine.getGameEngine;

import niellebeck.game.sprites.Door;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.InteractionHandler;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Sprite;
import niellebeck.gameengine.Tilemap;

public class LevelTwoScene extends GameScene {

	private PlayerCharacter playerChar;
	private Door door;
	private Door houseDoor;
	
	private boolean comingFromHouse;
	
	public LevelTwoScene(boolean comingFromHouse) {
		this.comingFromHouse = comingFromHouse;
	}
	
	@Override
	public void init() {
		door = new Door(300, 300);
		houseDoor = new Door(120, 140);
		if (comingFromHouse) {
			playerChar = new PlayerCharacter(120, 180);
		}
		else {
			playerChar = new PlayerCharacter(300, 260);
		}
		
		getGameEngine().addSprite(playerChar);
		getGameEngine().addSprite(door);
		getGameEngine().addSprite(houseDoor);
		
		door.setInteractionHandler(new InteractionHandler() {
			@Override
			public void interact() {
				getGameEngine().changeScene(new LevelOneScene());
			}
			
			@Override
			public String getInteractionMessage() {
				return "enter door";
			}
		});
		
		houseDoor.setInteractionHandler(new InteractionHandler() {
			@Override
			public void interact() {
				getGameEngine().changeScene(new InsideHouseScene());
			}
			
			@Override
			public String getInteractionMessage() {
				return "enter house";
			}
		});
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
	
	@Override
	public int getPlayerCharacterSpeed() {
		return PlayerCharacter.SPEED;
	}

}
