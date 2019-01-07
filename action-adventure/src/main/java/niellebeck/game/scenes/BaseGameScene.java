package niellebeck.game.scenes;

import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.GameScene;

public abstract class BaseGameScene extends GameScene {
	public abstract PlayerCharacter getPlayerCharacter();
	public abstract boolean allEnemiesDestroyed();
}
