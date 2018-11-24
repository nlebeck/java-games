package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Enemy;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.CollisionHandler;

public class EnemyPlayerCharacterCollisionHandler extends CollisionHandler<Enemy, PlayerCharacter> {
	public EnemyPlayerCharacterCollisionHandler() {
		super(Enemy.class, PlayerCharacter.class);
	}

	public void handleCollision(Enemy enemy, PlayerCharacter playerCharacter) {
		playerCharacter.onEnemyHit();
	}
}
