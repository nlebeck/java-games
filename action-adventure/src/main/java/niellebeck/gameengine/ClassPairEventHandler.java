package niellebeck.gameengine;

/**
 * The base class for event handlers for specific pairs of Sprite subclasses.
 * This class was written to simplify the code for the abstract
 * ClassPairCollisionHandler and ClassPairProximityEventHandler classes. Games
 * should extend those classes rather than extending this class directly.
 * <p>
 * The point of this class is to make it easy to both define the event-handling
 * logic for a pair of Sprite subclasses in one place and execute that
 * event-handling logic for a pair of Sprites without having to type-check
 * or cast those Sprites. To accomplish that goal, this class does some ugly
 * things with reflection. My hope is that the experience of writing concrete
 * subclasses of the different types of ClassPairEventHandlers is intuitive
 * enough to make the ugliness worth it.
 * </p>
 * <p>
 * I wanted to be able to get Class objects for the type parameters, but Java
 * doesn't allow that. As a result, I used the workaround suggested in this
 * StackOverflow answer: https://stackoverflow.com/a/3403987.
 * </p>
 */
public abstract class ClassPairEventHandler<T1 extends Sprite, T2 extends Sprite> {
	private Class<T1> classA;
	private Class<T2> classB;

	public ClassPairEventHandler(Class<T1> classA, Class<T2> classB) {
		this.classA = classA;
		this.classB = classB;
	}
	
	public ClassPair getClassPair() {
		return new ClassPair(classA, classB);
	}
	
	/**
	 * Figure out which argument is of which Sprite subclass, then invoke
	 * handleEvent() with the arguments in the right order.
	 */
	public void castObjectsAndHandleEvent(Sprite spriteA, Sprite spriteB) {
		if (spriteA.getClass().equals(classA)) {
			handleEvent((T1)spriteA, (T2)spriteB);
		}
		else if (spriteB.getClass().equals(classA)) {
			handleEvent((T1)spriteB, (T2)spriteA);
		}
		else {
			throw new IllegalArgumentException("The passed-in sprites do not have the required types.");
		}
	}
	
	public abstract void handleEvent(T1 spriteA, T2 spriteB);
}
