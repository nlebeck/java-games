package niellebeck.game.collisionhandlers;

import niellebeck.game.Bullet;
import niellebeck.game.NPC;
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
