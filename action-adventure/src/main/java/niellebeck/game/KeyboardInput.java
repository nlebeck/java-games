package niellebeck.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInput {
	private Set<Integer> keySet;
	private Set<Integer> prevKeySet;
	
	public KeyboardInput(GamePanel gp) {
		keySet = new HashSet<Integer>();
		prevKeySet = new HashSet<Integer>();
		
		gp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				keySet.add(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e) {
				keySet.remove(e.getKeyCode());
			}
		});
	}
	
	public void update() {
		prevKeySet = keySet;
		keySet = new HashSet<Integer>(prevKeySet);
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
