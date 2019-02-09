package niellebeck.gameengine;

/**
 * A ClassPairProximityEventHandler's handleEvent() method is called when two
 * objects of the specified Sprite subclasses are within the distance specified
 * by its getProximityDistance() method during a given frame.
 */
public abstract class ClassPairProximityEventHandler<T1 extends Sprite, T2 extends Sprite>
		extends ClassPairEventHandler<T1, T2> {
	
	public ClassPairProximityEventHandler(Class<T1> classA, Class<T2> classB) {
		super(classA, classB);
	}
	
	/**
	 * @return The maximum distance between a pair of these Sprite subclasses
	 * required for a proximity event to trigger.
	 */
	public abstract double getProximityDistance();
}
