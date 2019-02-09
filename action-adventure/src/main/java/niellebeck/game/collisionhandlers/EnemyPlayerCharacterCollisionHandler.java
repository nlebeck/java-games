package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Enemy;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.ClassPairCollisionHandler;

public class EnemyPlayerCharacterCollisionHandler extends ClassPairCollisionHandler<Enemy, PlayerCharacter> {
	public EnemyPlayerCharacterCollisionHandler() {
		super(Enemy.class, PlayerCharacter.class);
	}

	@Override
	public void handleEvent(Enemy enemy, PlayerCharacter playerCharacter) {
		playerCharacter.onEnemyHit();
	}
}
