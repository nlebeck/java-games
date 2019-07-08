package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.gameengine.ClassPairCollisionHandler;

public class BulletBulletCollisionHandler extends ClassPairCollisionHandler<Bullet, Bullet> {
	public BulletBulletCollisionHandler() {
		super(Bullet.class, Bullet.class);
	}

	@Override
	public void handleEvent(Bullet bulletA, Bullet bulletB) {
		bulletA.destroy();
		bulletB.destroy();
	}
}
