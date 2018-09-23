package niellebeck.game;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import niellebeck.game.collisionhandlers.ClassPair;
import niellebeck.game.collisionhandlers.CollisionHandler;

public class CollisionManager {
	
	/**
	 * An unordered pair of Sprites that have collided during this update step.
	 * <p>
	 * The equals() and hashCode() methods are defined so that two
	 * CollisionPair objects are considered equal if they contain the same
	 * Sprites in the opposite order.
	 * </p>
	 */
	private class CollisionPair {
		private Sprite spriteA;
		private Sprite spriteB;
		
		public CollisionPair(Sprite spriteA, Sprite spriteB) {
			this.spriteA = spriteA;
			this.spriteB = spriteB;
		}
		
		public Sprite getSpriteA() {
			return spriteA;
		}
		
		public Sprite getSpriteB() {
			return spriteB;
		}
		
		@Override
		public boolean equals(Object obj) {
			CollisionPair other = (CollisionPair)obj;
			return (   (spriteA.equals(other.spriteA) && spriteB.equals(other.spriteB))
					|| (spriteB.equals(other.spriteA) && spriteA.equals(other.spriteB)));
		}
		
		@Override
		public int hashCode() {
			return spriteA.hashCode() + spriteB.hashCode();
		}
	}
	
	private Game game;
	private Set<CollisionPair> collisionPairs;
	private Set<Sprite> tilemapCollisions;
	private Map<ClassPair, CollisionHandler<?,?>> collisionHandlers;
	
	public CollisionManager (Game game) {
		this.game = game;
		collisionPairs = new HashSet<CollisionPair>();
		tilemapCollisions = new HashSet<Sprite>();
		collisionHandlers = new HashMap<ClassPair, CollisionHandler<?,?>>();
	}
	
	public boolean testAndAddCollisions(Sprite sprite) {
		Set<Sprite> collisionSet = getCollisionSet(sprite, game.getSpriteList());
		boolean tilemapCollision = game.getTilemap().collidesWithSprite(sprite);
		
		for (Sprite otherSprite : collisionSet) {
			collisionPairs.add(new CollisionPair(sprite, otherSprite));
		}
		if (tilemapCollision) {
			tilemapCollisions.add(sprite);
		}
		
		return (collisionSet.size() > 0 || tilemapCollision);
	}
	
	private Set<Sprite> getCollisionSet(Sprite sprite, List<Sprite> spriteList) {
		Set<Sprite> collisionSet = new HashSet<Sprite>();
		Rectangle spriteBoundingBox = sprite.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox()) && otherSprite != sprite) {
				collisionSet.add(otherSprite);
			}
		}
		return collisionSet;
	}
	
	public void processCollisions() {
		for (CollisionPair pair : collisionPairs) {
			invokeCollisionHandler(pair);
		}
		for (Sprite sprite : tilemapCollisions) {
			sprite.onCollideTilemap();
		}
		collisionPairs.clear();
		tilemapCollisions.clear();
	}
	
	public void registerCollisionHandler(CollisionHandler<? extends Sprite, ? extends Sprite> collisionHandler) {
		if (collisionHandlers.containsKey(collisionHandler.getClassPair())) {
			throw new IllegalStateException("This CollisionManager already has a CollisionHandler registered for the given pair of Sprite subclasses.");
		}
		collisionHandlers.put(collisionHandler.getClassPair(), collisionHandler);
	}
	
	private CollisionHandler<? extends Sprite, ? extends Sprite> getCollisionHandler(Class<? extends Sprite> classA, Class<? extends Sprite> classB) {
		ClassPair classPair = new ClassPair(classA, classB);
		return collisionHandlers.get(classPair);
	}
	
	private void invokeCollisionHandler(CollisionPair pair) {
		Sprite spriteA = pair.getSpriteA();
		Sprite spriteB = pair.getSpriteB();
		Class<? extends Sprite> classA = spriteA.getClass();
		Class<? extends Sprite> classB = spriteB.getClass();
		CollisionHandler<? extends Sprite, ? extends Sprite> collisionHandler = getCollisionHandler(classA, classB);
		if (collisionHandler != null) {
			collisionHandler.castObjectsAndHandleCollision(spriteA, spriteB);
		}
	}
}
