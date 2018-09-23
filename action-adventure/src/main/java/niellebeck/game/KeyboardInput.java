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
	
	private Set<Integer> keySet;
	private Set<Integer> prevKeySet;
	private final Object lock;
	
	public KeyboardInput(GamePanel gp) {
		keySet = new HashSet<Integer>();
		prevKeySet = new HashSet<Integer>();
		lock = new Object();
		
		gp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				synchronized (lock) {
					keySet.add(e.getKeyCode());
				}
			}
			public void keyReleased(KeyEvent e) {
				synchronized (lock) {
					keySet.remove(e.getKeyCode());
				}
			}
		});
	}
	
	public void update() {
		synchronized (lock) {
			prevKeySet = keySet;
			keySet = new HashSet<Integer>(prevKeySet);
		}
	}
	
	public boolean keyPressed(int keyCode) {
		synchronized (lock) {
			return keySet.contains(keyCode) && !prevKeySet.contains(keyCode);
		}
	}
	
	public boolean keyReleased(int keyCode) {
		synchronized (lock) {
			return !keySet.contains(keyCode) && prevKeySet.contains(keyCode);
		}
	}
	
	public boolean keyIsDown(int keyCode) {
		synchronized (lock) {
			return keySet.contains(keyCode);
		}
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
