package game;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionHandler {
	//TODO: figure out a more elegant way to handle symmetric collisions
	public static void handleCollision(Sprite collidingObj, Sprite collidedObj) {
		if (collidingObj.getType() == SpriteType.PLAYER_CHARACTER) {
			System.out.println("Player char collided with object of type " + collidedObj.getType());
		}
		if (collidingObj.getType() == SpriteType.BULLET) {
			collidingObj.destroy();
			collidedObj.destroy();
		}
		if (collidedObj.getType() == SpriteType.BULLET) {
			collidingObj.destroy();
			collidedObj.destroy();
		}
	}
	
	
	public static Set<Sprite> getCollisionSet(Sprite sprite, List<Sprite> spriteList) {
		Set<Sprite> collisionSet = new HashSet<Sprite>();
		Rectangle spriteBoundingBox = sprite.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox()) && otherSprite != sprite) {
				collisionSet.add(otherSprite);
			}
		}
		return collisionSet;
	}
}
