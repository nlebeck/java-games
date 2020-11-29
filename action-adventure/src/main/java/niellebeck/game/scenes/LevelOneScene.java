package niellebeck.game.scenes;

import static niellebeck.gameengine.GameEngine.getGameEngine;

import java.util.ArrayList;
import java.util.List;

import niellebeck.game.MyGameLogic;
import niellebeck.game.dialogues.EnemiesDefeatedDialogue;
import niellebeck.game.dialogues.InitialDialogue;
import niellebeck.game.sprites.Door;
import niellebeck.game.sprites.Enemy;
import niellebeck.game.sprites.NPC;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.Dialogue;
import niellebeck.gameengine.DialogueManager;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.InteractionHandler;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Tilemap;

public class LevelOneScene extends GameScene {

	private PlayerCharacter playerChar;
	private Door door;
	private List<Enemy> enemies;
	
	@Override
	public void init() {
		MyGameLogic myGameLogic = (MyGameLogic)getGameEngine().getGameLogic();
		
		getGameEngine().getMusicManager().changeMusicTrack("/music/riff_A.wav");
		
		NPC npc = new NPC(80, 350);
		NPC anotherNpc = new NPC(500, 250);
		door = new Door(320, 60);
		enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy(400, 400));
		enemies.add(new Enemy(160, 60));
		if (myGameLogic.levelOneDoorIsOpen()) {
			playerChar = new PlayerCharacter(320, 100);
		}
		else {
			playerChar = new PlayerCharacter(300, 220);
		}
		
		getGameEngine().addSprite(playerChar);
		getGameEngine().addSprite(npc);
		getGameEngine().addSprite(anotherNpc);
		getGameEngine().addSprites(enemies);
		if (myGameLogic.levelOneDoorIsOpen()) {
			getGameEngine().addSprite(door);
		}
		
		npc.setInteractionHandler(new InteractionHandler() {
			
			@Override
			public void interact() {
				Dialogue dialogue = null;
				if (myGameLogic.levelOneDoorIsOpen()) {
					dialogue = new Dialogue(new String[] {
							"A door has opened in the north."
					});
				}
				else if (allEnemiesDestroyed()) {
					dialogue = new EnemiesDefeatedDialogue();
					getGameEngine().addSprite(door);
					myGameLogic.openLevelOneDoor();
				}
				else {
					dialogue = new InitialDialogue();
				}
				DialogueManager.getInstance().startDialogue(dialogue);
			}
			
			@Override
			public String getInteractionMessage() {
				return "talk with NPC";
			}
		});
		
		anotherNpc.setInteractionHandler(new InteractionHandler() {

			@Override
			public void interact() {
				Dialogue dialogue = new Dialogue(new String[] {
						"Hello!",
						"I am another NPC."
				});
				DialogueManager.getInstance().startDialogue(dialogue);
			}

			@Override
			public String getInteractionMessage() {
				return "talk with NPC";
			}
		});
		
		door.setInteractionHandler(new InteractionHandler() {

			@Override
			public void interact() {
				getGameEngine().changeScene(new LevelTwoScene(false));
			}

			@Override
			public String getInteractionMessage() {
				return "enter door";
			}
		});
	}
	
	public boolean allEnemiesDestroyed() {
		boolean allDestroyed = true;
		for (Enemy enemy : enemies) {
			if (!enemy.isDestroyed()) {
				allDestroyed = false;
			}
		}
		return allDestroyed;
	}

	@Override
	public Tilemap getTilemap() {
		return new Tilemap("/tilemap.txt");
	}

	@Override
	public void update(KeyboardInput keyboard) {
		
	}

	@Override
	public PlayerCharacter getPlayerCharacter() {
		return playerChar;
	}
	
	@Override
	public int getPlayerCharacterSpeed() {
		return PlayerCharacter.SPEED;
	}

}
