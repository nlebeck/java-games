package niellebeck.gameengine;

/**
 * The base class for collision handlers for specific pairs of Sprite
 * subclasses. Subclasses of this class currently define the methods to handle
 * both collision events and proximity events for their Sprite subclass pairs.
 * <p>
 * The point of this class is to make it easy to both define the collision
 * logic for a pair of Sprite subclasses in one place and execute that
 * collision logic for a pair of collided Sprites without having to type-check
 * or cast those Sprites. To accomplish that goal, this class does some ugly
 * things with reflection. My hope is that the experience of writing
 * CollisionHandler subclasses themselves is intuitive enough to make the
 * ugliness worth it.
 * </p>
 * <p>
 * I wanted to be able to get Class objects for the type parameters, but Java
 * doesn't allow that. As a result, I used the workaround suggested in this
 * StackOverflow answer: https://stackoverflow.com/a/3403987.
 * </p>
 */
public abstract class CollisionHandler<T1 extends Sprite, T2 extends Sprite> {
	private Class<T1> classA;
	private Class<T2> classB;
	
	public CollisionHandler(Class<T1> classA, Class<T2> classB) {
		this.classA = classA;
		this.classB = classB;
	}
	
	public ClassPair getClassPair() {
		return new ClassPair(classA, classB);
	}
	
	/**
	 * Figure out which argument is of which Sprite subclass, then invoke
	 * handleCollision() with the arguments in the right order.
	 */
	public void castObjectsAndHandleCollision(Sprite spriteA, Sprite spriteB) {
		if (spriteA.getClass().equals(classA)) {
			handleCollision((T1)spriteA, (T2)spriteB);
		}
		else if (spriteB.getClass().equals(classA)) {
			handleCollision((T1)spriteB, (T2)spriteA);
		}
		else {
			throw new IllegalArgumentException("The passed-in sprites do not have the required types.");
		}
	}
	
	/**
	 * Figure out which argument is of which Sprite subclass, then invoke
	 * handleProximityEvent() with the Sprite arguments in the right order.
	 */
	public void castObjectsAndHandleProximityEvent(GameEngine game, Sprite spriteA, Sprite spriteB) {
		if (spriteA.getClass().equals(classA)) {
			handleProximityEvent(game, (T1)spriteA, (T2)spriteB);
		}
		else if (spriteB.getClass().equals(classA)) {
			handleProximityEvent(game, (T1)spriteB, (T2)spriteA);
		}
		else {
			throw new IllegalArgumentException("The passed-in sprites do not have the required types.");
		}
	}

	/**
	 * Called when a pair of these Sprite subclasses collided this frame.
	 * Override this method if you want to handle collisions for this Sprite
	 * subclass pair.
	 * @param spriteA
	 * @param spriteB
	 */
	public void handleCollision(T1 spriteA, T2 spriteB) { }
	
	/**
	 * @return The maximum distance between a pair of these Sprite subclasses
	 * required for a proximity event to trigger. Override this method if you
	 * want to handle proximity events for this Sprite subclass pair.
	 */
	public double getProximityDistance() {
		return 0;
	}
	
	/**
	 * Called when a pair of these Sprite subclasses is close enough to trigger
	 * a proximity event. Override this method if you want to handle proximity
	 * events for this Sprite subclass pair.
	 * @param spriteA
	 * @param spriteB
	 */
	public void handleProximityEvent(GameEngine game, T1 spriteA, T2 spriteB) { }
}
