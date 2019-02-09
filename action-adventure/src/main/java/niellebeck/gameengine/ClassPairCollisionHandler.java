package niellebeck.gameengine;

/**
 * A ClassPairCollisionHandler's handleEvent() method is called when two
 * objects of the specified Sprite subclasses collide during a given frame.
 */
public abstract class ClassPairCollisionHandler<T1 extends Sprite, T2 extends Sprite> 
		extends ClassPairEventHandler<T1, T2> {
	
	public ClassPairCollisionHandler(Class<T1> classA, Class<T2> classB) {
		super(classA, classB);
	}
}
