package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.Door;
import niellebeck.gameengine.CollisionHandler;

public class BulletDoorCollisionHandler extends CollisionHandler<Bullet, Door> {

	public BulletDoorCollisionHandler() {
		super(Bullet.class, Door.class);
	}
	
	@Override
	public void handleCollision(Bullet bullet, Door door) {
		bullet.destroy();
	}

}
