package niellebeck.game.collisionhandlers;

import niellebeck.game.Bullet;
import niellebeck.game.Enemy;
import niellebeck.gameengine.CollisionHandler;

public class BulletEnemyCollisionHandler extends CollisionHandler<Bullet, Enemy> {
	public BulletEnemyCollisionHandler() {
		super(Bullet.class, Enemy.class);
	}
	
	@Override
	public void handleCollision(Bullet bullet, Enemy enemy) {
		bullet.destroy();
		enemy.destroy();
	}
}
