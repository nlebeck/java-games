package niellebeck.game.collisionhandlers;

import niellebeck.game.Enemy;
import niellebeck.game.PlayerCharacter;

public class EnemyPlayerCharacterCollisionHandler extends CollisionHandler<Enemy, PlayerCharacter> {
	public EnemyPlayerCharacterCollisionHandler() {
		super(Enemy.class, PlayerCharacter.class);
	}

	public void handleCollision(Enemy enemy, PlayerCharacter playerCharacter) {
		playerCharacter.onEnemyHit();
	}
}
