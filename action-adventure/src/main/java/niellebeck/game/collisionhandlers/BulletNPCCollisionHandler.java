package niellebeck.game.collisionhandlers;

import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.NPC;
import niellebeck.gameengine.ClassPairCollisionHandler;

public class BulletNPCCollisionHandler extends ClassPairCollisionHandler<Bullet, NPC> {

	public BulletNPCCollisionHandler() {
		super(Bullet.class, NPC.class);
	}

	@Override
	public void handleEvent(Bullet bullet, NPC npc) {
		bullet.destroy();
	}

}
