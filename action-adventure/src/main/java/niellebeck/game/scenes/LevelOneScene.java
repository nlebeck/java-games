package niellebeck.game.scenes;

import java.util.ArrayList;
import java.util.List;

import niellebeck.game.MyGameLogic;
import niellebeck.game.dialogues.EnemiesDefeatedDialogue;
import niellebeck.game.dialogues.InitialDialogue;
import niellebeck.game.sprites.Enemy;
import niellebeck.game.sprites.NPC;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.Dialogue;
import niellebeck.gameengine.DialogueManager;
import niellebeck.gameengine.GameLogic;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.InteractionHandler;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Tilemap;

public class LevelOneScene extends GameScene {

	private PlayerCharacter playerChar;
	private NPC npc;
	private NPC anotherNpc;
	private List<Enemy> enemies;
	
	@Override
	public void init() {
		playerChar = new PlayerCharacter(300, 220);
		npc = new NPC(80, 350);
		anotherNpc = new NPC(500, 250);
		enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy(400, 400));
		enemies.add(new Enemy(320, 100));
		
		getGameEngine().addSprite(playerChar);
		getGameEngine().addSprite(npc);
		getGameEngine().addSprite(anotherNpc);
		getGameEngine().addSprites(enemies);
		
		npc.setInteractionHandler(new InteractionHandler() {
			
			@Override
			public void interact(GameLogic gameLogic, GameScene gameScene) {
				LevelOneScene levelOneScene = (LevelOneScene)gameScene;
				Dialogue dialogue = null;
				if (levelOneScene.allEnemiesDestroyed()) {
					dialogue = new EnemiesDefeatedDialogue();
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
			
			@Override
			public double getInteractionDistance() {
				return 56.6;
			}
		});
		
		anotherNpc.setInteractionHandler(new InteractionHandler() {

			@Override
			public void interact(GameLogic gameLogic, GameScene gameScene) {
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
			
			@Override
			public double getInteractionDistance() {
				return 56.6;
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

}
