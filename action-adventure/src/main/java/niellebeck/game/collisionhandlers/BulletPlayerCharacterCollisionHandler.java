package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.ClassPairCollisionHandler;

public class BulletPlayerCharacterCollisionHandler extends ClassPairCollisionHandler<Bullet, PlayerCharacter> {
	public BulletPlayerCharacterCollisionHandler() {
		super(Bullet.class, PlayerCharacter.class);
	}

	@Override
	public void handleEvent(Bullet bullet, PlayerCharacter playerCharacter) {
		playerCharacter.onEnemyHit();
		bullet.destroy();
	}
}
