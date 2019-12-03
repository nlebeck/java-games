package niellebeck.gameengine;

import java.awt.Rectangle;

public class RectangleUtils {
	
	/**
	 * Return true if rectA and rectB are within the threshold distance of
	 * each other along one dimension (vertical or horizontal) and overlapping
	 * along the other dimension.
	 */
	public static boolean areAdjacent(Rectangle rectA, Rectangle rectB, int threshold) {
		int leftA = rectA.x;
		int rightA = rectA.x + rectA.width;
		int topA = rectA.y;
		int bottomA = rectA.y + rectA.height;
		
		int leftB = rectB.x;
		int rightB = rectB.x + rectB.width;
		int topB = rectB.y;
		int bottomB = rectB.y + rectB.height;
		
		if (Math.abs(rightA - leftB) <= threshold || Math.abs(rightB - leftA) <= threshold) {
			return intervalsOverlap(topA, bottomA, topB, bottomB);
		}
		
		if (Math.abs(topA - bottomB) <= threshold || Math.abs(bottomA - topB) <= threshold) {
			return intervalsOverlap(leftA, rightA, leftB, rightB);
		}
		
		return false;
	}
	
	/**
	 * Checks if two one-dimensional intervals overlap. This method should be
	 * replaced by a standard library method if one exists.
	 * <p>
	 * Preconditions: endA >= startA, endB >= startB
	 */
	private static boolean intervalsOverlap(int startA, int endA, int startB, int endB) {
		/*
		 * I think the three cases I check below are sufficient to test
		 * whether the intervals overlap, but I'm not totally sure.
		 */
		
		// Interval B entirely contains interval A
		if (startB <= startA && endB >= endA) {
			return true;
		}
		
		// Interval A contains the start of interval B
		if (startB >= startA && startB <= endA) {
			return true;
		}
		
		// Interval A contains the end of interval B
		if (endB >= startA && endB <= endA) {
			return true;
		}
		
		return false;
	}
}
