package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.Door;
import niellebeck.gameengine.ClassPairCollisionHandler;

public class BulletDoorCollisionHandler extends ClassPairCollisionHandler<Bullet, Door> {

	public BulletDoorCollisionHandler() {
		super(Bullet.class, Door.class);
	}
	
	@Override
	public void handleEvent(Bullet bullet, Door door) {
		bullet.destroy();
	}

}
