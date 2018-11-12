package niellebeck.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInput {
	/*
	 * I referenced the following StackOverflow posts when adding locking to
	 * this class:
	 * 
	 * https://stackoverflow.com/a/6711917
	 * I made the lock Object final based on this answer's recommendation.
	 * 
	 * https://stackoverflow.com/q/7971946
	 * The answers to this question told me that it was safe to have a return
	 * statement inside of a synchronized block.
	 */
	
	/*
	 * There are so many sets required due to the discrepancy between the
	 * asynchronous KeyListener interface and the per-frame key information
	 * that we want. Under the current design, the "current" keyboard state
	 * according to this class's methods is the keyboard state as of the end
	 * of the previous frame.
	 * 
	 * Note that this implementation risks a "false negative" when a key is
	 * quickly pressed, released, and then pressed again during a single frame.
	 * Any such key that is incorrectly counted as "not down" during that frame
	 * will be correctly counted as "down" during the next frame, though, if
	 * the player continues to hold the key down.
	 * 
	 * keySet: holds all keys that were down as of the end of the previous
	 *     frame.
	 * prevKeySet: holds all keys that were down as of the end of the frame
	 *     before the previous frame.
	 * futureKeySet: holds all keys that have been pressed during the current
	 *     frame.
	 * futureReleaseSet: holds all keys that have been released during the
	 *     current frame.
	 */
	private Set<Integer> keySet;
	private Set<Integer> prevKeySet;
	private Set<Integer> futureKeySet;
	private Set<Integer> futureReleaseSet;
	private final Object lock;
	
	public KeyboardInput(GamePanel gp) {
		keySet = new HashSet<Integer>();
		prevKeySet = new HashSet<Integer>();
		futureKeySet = new HashSet<Integer>();
		futureReleaseSet = new HashSet<Integer>();
		lock = new Object();
		
		gp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				synchronized (lock) {
					futureKeySet.add(e.getKeyCode());
				}
			}
			public void keyReleased(KeyEvent e) {
				synchronized (lock) {
					futureReleaseSet.add(e.getKeyCode());
				}
			}
		});
	}
	
	public void update() {
		prevKeySet = keySet;
		keySet = new HashSet<Integer>(prevKeySet);
		synchronized (lock) {
			for (int keyCode : futureKeySet) {
				keySet.add(keyCode);
			}
			for (int keyCode : futureReleaseSet) {
				keySet.remove(keyCode);
			}
			futureKeySet.clear();
			futureReleaseSet.clear();
		}
	}
	
	public boolean keyPressed(int keyCode) {
		return keySet.contains(keyCode) && !prevKeySet.contains(keyCode);
	}
	
	public boolean keyReleased(int keyCode) {
		return !keySet.contains(keyCode) && prevKeySet.contains(keyCode);
	}
	
	public boolean keyIsDown(int keyCode) {
		return keySet.contains(keyCode);
	}
	
	public Direction getArrowKeyDirection() {
		Direction dir = Direction.NONE;
		if (keyIsDown(KeyEvent.VK_LEFT) && keyIsDown(KeyEvent.VK_UP)) {
			dir = Direction.UP_LEFT;
		}
		else if (keyIsDown(KeyEvent.VK_LEFT) && keyIsDown(KeyEvent.VK_DOWN)) {
			dir = Direction.DOWN_LEFT;
		}
		else if (keyIsDown(KeyEvent.VK_RIGHT) && keyIsDown(KeyEvent.VK_UP)) {
			dir = Direction.UP_RIGHT;
		}
		else if (keyIsDown(KeyEvent.VK_RIGHT) && keyIsDown(KeyEvent.VK_DOWN)) {
			dir = Direction.DOWN_RIGHT;
		}
		else if (keyIsDown(KeyEvent.VK_LEFT)) {
			dir = Direction.LEFT;
		}
		else if (keyIsDown(KeyEvent.VK_RIGHT)) {
			dir = Direction.RIGHT;
		}
		else if (keyIsDown(KeyEvent.VK_UP)) {
			dir = Direction.UP;
		}
		else if (keyIsDown(KeyEvent.VK_DOWN)) {
			dir = Direction.DOWN;
		}
		return dir;
	}
}
