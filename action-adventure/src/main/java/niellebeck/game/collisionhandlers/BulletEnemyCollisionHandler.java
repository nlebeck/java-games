package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.Enemy;
import niellebeck.gameengine.ClassPairCollisionHandler;

public class BulletEnemyCollisionHandler extends ClassPairCollisionHandler<Bullet, Enemy> {
	public BulletEnemyCollisionHandler() {
		super(Bullet.class, Enemy.class);
	}
	
	@Override
	public void handleEvent(Bullet bullet, Enemy enemy) {
		bullet.destroy();
		enemy.destroy();
	}
}
