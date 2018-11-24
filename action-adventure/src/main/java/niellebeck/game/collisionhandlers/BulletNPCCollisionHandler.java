package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.NPC;
import niellebeck.gameengine.CollisionHandler;

public class BulletNPCCollisionHandler extends CollisionHandler<Bullet, NPC> {

	public BulletNPCCollisionHandler() {
		super(Bullet.class, NPC.class);
	}

	@Override
	public void handleCollision(Bullet bullet, NPC npc) {
		bullet.destroy();
	}

}
