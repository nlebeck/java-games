package niellebeck.game.collisionhandlers;

/**
 * An unordered pair of Classes.
 * <p>
 * The equals() and hashCode() methods are defined so that two ClassPair
 * objects are considered equal if they contain the same Class objects in the
 * opposite order.
 * </p>
 */
public class ClassPair {
	public Class<?> classA;
	public Class<?> classB;
	
	public ClassPair(Class<?> classA, Class<?> classB) {
		this.classA = classA;
		this.classB = classB;
	}
	
	@Override
	public boolean equals(Object obj) {
		ClassPair other = (ClassPair)obj;
		return (   (classA.equals(other.classA) && classB.equals(other.classB))
				|| (classA.equals(other.classB) && classB.equals(other.classA)));
	}
	
	@Override
	public int hashCode() {
		return classA.hashCode() + classB.hashCode();
	}
}
