package niellebeck.gameengine;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for handling collision events and proximity events
 * as well as registering interactable Sprites.
 * <p>
 * A collision event occurs when one Sprite attempts to move into the space
 * occupied by another Sprite. A proximity event occurs when two Sprites are
 * within a certain distance of each other. An interactable Sprite is a Sprite
 * next to the player character with an InteractionHandler attached.
 * <p>
 * When either a collision event or a proximity event is detected for a pair
 * of Sprites, the EventManager invokes the handleEvent() method in the
 * appropriate event handler corresponding to the specific subclasses of the
 * two Sprites. When an interactable Sprite is detected, the EventManager
 * registers the interactable Sprite with the game engine, so that the player
 * can interact with it by pressing the appropriate button.
 */
public class EventManager {
	
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
	
	private GameEngine game;
	private Set<CollisionPair> collisionPairs;
	private Set<Sprite> tilemapCollisions;
	private Map<ClassPair, ClassPairCollisionHandler<?,?>> classPairCollisionHandlers;
	private Map<ClassPair, ClassPairProximityEventHandler<?,?>> classPairProximityEventHandlers;
	
	public EventManager (GameEngine game) {
		this.game = game;
		collisionPairs = new HashSet<CollisionPair>();
		tilemapCollisions = new HashSet<Sprite>();
		classPairCollisionHandlers = new HashMap<ClassPair, ClassPairCollisionHandler<?,?>>();
		classPairProximityEventHandlers = new HashMap<ClassPair, ClassPairProximityEventHandler<?,?>>();
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
	
	public void processProximityEvents() {
		for (int i = 0; i < game.getSpriteList().size(); i++) {
			Sprite spriteA = game.getSpriteList().get(i);
			for (int j = i + 1; j < game.getSpriteList().size(); j++) {
				Sprite spriteB = game.getSpriteList().get(j);
				double distance = getDistance(spriteA, spriteB);
				Class<? extends Sprite> classA = spriteA.getClass();
				Class<? extends Sprite> classB = spriteB.getClass();
				ClassPairProximityEventHandler<? extends Sprite, ? extends Sprite> eventHandler = getClassPairProximityEventHandler(classA, classB);
				if (eventHandler != null && eventHandler.getProximityDistance() >= distance) {
					eventHandler.castObjectsAndHandleEvent(spriteA, spriteB);
				}
			}
		}
	}
	
	/**
	 * Identifies all Sprites that are interactable and next to the player
	 * character.
	 * <p>
	 * This method currently considers a Sprite "next to" the player character
	 * if the distance between the two is at most the player character's speed
	 * (as determined by querying the GameScene). The idea is that the player
	 * character is next to a Sprite if moving in the Sprite's direction would
	 * cause them to collide that frame.
	 * <p>
	 * Both this definition and its implementation are brittle. If I can think
	 * of a more elegant way of defining and implementing the same high-level
	 * design, I will.
	 */
	public void registerInteractableSprites(GameScene gameScene) {
		for (Sprite sprite : game.getSpriteList()) {
			if (sprite.isInteractable()) {
				if (RectangleUtils.areAdjacent(
						gameScene.getPlayerCharacter().getBoundingBox(),
						sprite.getBoundingBox(),
						gameScene.getPlayerCharacterSpeed())) {
					game.registerInteractable(sprite);
				}
			}
		}
	}
	
	private double getDistance(Sprite spriteA, Sprite spriteB) {
		return Math.sqrt(Math.pow(spriteB.posX - spriteA.posX, 2) + Math.pow(spriteB.posY - spriteA.posY, 2));
	}
	
	public void registerClassPairCollisionHandler(ClassPairCollisionHandler<? extends Sprite, ? extends Sprite> collisionHandler) {
		if (classPairCollisionHandlers.containsKey(collisionHandler.getClassPair())) {
			throw new IllegalStateException("This CollisionManager already has a ClassPairCollisionHandler registered for the given pair of Sprite subclasses.");
		}
		classPairCollisionHandlers.put(collisionHandler.getClassPair(), collisionHandler);
	}
	
	public void registerClassPairProximityEventHandler(ClassPairProximityEventHandler<? extends Sprite, ? extends Sprite> eventHandler) {
		if (classPairProximityEventHandlers.containsKey(eventHandler.getClassPair())) {
			throw new IllegalStateException("This CollisionManager already has a ClassPairProximityEventHandler registered for the given pair of Sprite subclasses.");
		}
		classPairProximityEventHandlers.put(eventHandler.getClassPair(), eventHandler);
	}
	
	private ClassPairCollisionHandler<? extends Sprite, ? extends Sprite> getClassPairCollisionHandler(Class<? extends Sprite> classA, Class<? extends Sprite> classB) {
		ClassPair classPair = new ClassPair(classA, classB);
		return classPairCollisionHandlers.get(classPair);
	}
	
	private ClassPairProximityEventHandler<? extends Sprite, ? extends Sprite> getClassPairProximityEventHandler(Class<? extends Sprite> classA, Class<? extends Sprite> classB) {
		ClassPair classPair = new ClassPair(classA, classB);
		return classPairProximityEventHandlers.get(classPair);
	}
	
	private void invokeCollisionHandler(CollisionPair pair) {
		Sprite spriteA = pair.getSpriteA();
		Sprite spriteB = pair.getSpriteB();
		Class<? extends Sprite> classA = spriteA.getClass();
		Class<? extends Sprite> classB = spriteB.getClass();
		ClassPairCollisionHandler<? extends Sprite, ? extends Sprite> collisionHandler = getClassPairCollisionHandler(classA, classB);
		if (collisionHandler != null) {
			collisionHandler.castObjectsAndHandleEvent(spriteA, spriteB);
		}
	}
}
