package niellebeck.game.scenes;

import java.util.ArrayList;
import java.util.List;

import niellebeck.game.sprites.Enemy;
import niellebeck.game.sprites.NPC;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Tilemap;

public class LevelOneScene extends BaseGameScene {

	private PlayerCharacter playerChar;
	private NPC npc;
	private List<Enemy> enemies;
	
	@Override
	public void init() {
		playerChar = new PlayerCharacter(300, 220);
		npc = new NPC(80, 350);
		enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy(400, 400));
		enemies.add(new Enemy(320, 100));
		
		getGameEngine().addSprite(playerChar);
		getGameEngine().addSprite(npc);
		getGameEngine().addSprites(enemies);
	}
	
	@Override
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
	public int getCameraX() {
		return playerChar.getX() + (playerChar.getWidth() / 2);
	}
	
	@Override
	public int getCameraY() {
		return playerChar.getY() + (playerChar.getHeight() / 2);
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
