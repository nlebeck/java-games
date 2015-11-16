package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		createWindow();
	}
	
	private static void createWindow() {
		JFrame f = new JFrame("Game Window");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(640, 480);
		f.add(new GamePanel());
		f.pack();
		f.setVisible(true);
	}
}

class GamePanel extends JPanel implements Runnable {
	private static final int PANEL_WIDTH = 640;
	private static final int PANEL_HEIGHT = 480;
	private static final int RENDER_PERIOD = 17; //in ms

	
	private Thread animator;
	private boolean running = false;
	
	private Graphics bufferGraphics;
	private Image bufferImage = null;
	
	private Set<Integer> keySet;
	private Set<Integer> prevKeySet;
	
	//game state
	Sprite playerChar;
	List<Sprite> objects;
	
	public GamePanel() {
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setFocusable(true);
		
		initializeGameState();
		
		keySet = new HashSet<Integer>();
		prevKeySet = new HashSet<Integer>();
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				keySet.add(e.getKeyCode());
			}
			public void keyReleased(KeyEvent e) {
				keySet.remove(e.getKeyCode());
			}
		});
	}
	
	public void initializeGameState() {
		playerChar = new Sprite(300, 220);
		objects = new ArrayList<Sprite>();
		objects.add(new Rock(100, 100));
		objects.add(new Rock(500, 150));
		objects.add(new Rock(200, 200));
		
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
	
	public void addNotify() {
		super.addNotify();
		startGame();
	}
	
	private void startGame() {
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void render() {
		if (bufferImage == null) {
			bufferImage = createImage(PANEL_WIDTH, PANEL_HEIGHT);
			bufferGraphics = bufferImage.getGraphics();
		}
		
		//draw background
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		//render game
		playerChar.draw(bufferGraphics);
		for (Sprite s : objects) {
			s.draw(bufferGraphics);
		}
	}
	
	public Constants.Direction getArrowKeyDirection() {
		Constants.Direction dir = Constants.Direction.NONE;
		if (keyIsDown(KeyEvent.VK_LEFT) && keyIsDown(KeyEvent.VK_UP)) {
			dir = Constants.Direction.UP_LEFT;
		}
		else if (keyIsDown(KeyEvent.VK_LEFT) && keyIsDown(KeyEvent.VK_DOWN)) {
			dir = Constants.Direction.DOWN_LEFT;
		}
		else if (keyIsDown(KeyEvent.VK_RIGHT) && keyIsDown(KeyEvent.VK_UP)) {
			dir = Constants.Direction.UP_RIGHT;
		}
		else if (keyIsDown(KeyEvent.VK_RIGHT) && keyIsDown(KeyEvent.VK_DOWN)) {
			dir = Constants.Direction.DOWN_RIGHT;
		}
		else if (keyIsDown(KeyEvent.VK_LEFT)) {
			dir = Constants.Direction.LEFT;
		}
		else if (keyIsDown(KeyEvent.VK_RIGHT)) {
			dir = Constants.Direction.RIGHT;
		}
		else if (keyIsDown(KeyEvent.VK_UP)) {
			dir = Constants.Direction.UP;
		}
		else if (keyIsDown(KeyEvent.VK_DOWN)) {
			dir = Constants.Direction.DOWN;
		}
		return dir;
	}
	
	public void updateGameState() {
		Constants.Direction dir = getArrowKeyDirection();
		playerChar.move(dir);
		if (isCollision(playerChar, objects)) {
			playerChar.undoLastMove();
		}
	}
	
	public boolean isCollision(Sprite sprite, List<Sprite> spriteList) {
		boolean result = false;
		Rectangle spriteBoundingBox = sprite.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox())) {
				result = true;
			}
		}
		return result;
	}
	
	public void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics();
			if (g != null && bufferImage != null) {
				g.drawImage(bufferImage, 0, 0, null);
			}
			g.dispose();
		}
		catch (Exception e) {
			System.out.println("Graphics error: " + e);
		}
	}
	
	public void run() {
		running = true;
		
		long beforeTime, timeDiff, sleepTime;
		
		beforeTime = System.currentTimeMillis();
		
		while (running) {
			updateGameState();
			render();
			paintScreen();
			
			prevKeySet = keySet;
			keySet = new HashSet<Integer>(prevKeySet);
			
			timeDiff = System.currentTimeMillis() - beforeTime;
			sleepTime = RENDER_PERIOD - timeDiff;
			if (sleepTime < 0) {
				sleepTime = 5;
			}
			
			try {
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException e) {}
			
			beforeTime = System.currentTimeMillis();
		}
	}
}
