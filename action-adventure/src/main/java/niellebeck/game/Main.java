package niellebeck.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

enum GameState {
	START_MENU,
	DIALOG,
	MENU,
	PLAYING,
	GAME_OVER
}

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

	private static final int BULLET_COOLDOWN = 10; //in frames
	
	private Thread animator;
	private boolean running = false;
	
	private Graphics bufferGraphics;
	private Image bufferImage = null;
	
	//game state
	GameState gameState;
	PlayerCharacter playerChar;
	List<Sprite> objects;
	Direction lastPlayerDir;
	int timeUntilNextBullet;
	
	//graphics state
	Tilemap tilemap;
	
	//input
	KeyboardInput keyboard;
	
	public GamePanel() {
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setFocusable(true);
		
		initializeGameState();
		initializeGraphicsState();
		
		keyboard = new KeyboardInput(this);
	}
	
	public void initializeGameState() {
		gameState = GameState.START_MENU;
		playerChar = new PlayerCharacter(300, 220);
		objects = new ArrayList<Sprite>();
		objects.add(new Rock(100, 100));
		objects.add(new Rock(500, 150));
		objects.add(new Rock(200, 200));
		lastPlayerDir = Direction.RIGHT;
		timeUntilNextBullet = BULLET_COOLDOWN;
		
	}
	
	public void initializeGraphicsState() {
		tilemap = new Tilemap("/tilemap.txt");
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
		
		if (gameState == GameState.PLAYING) {
			//clear buffer
			bufferGraphics.setColor(Color.white);
			bufferGraphics.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
			
			//draw background
			tilemap.draw(bufferGraphics);
		
			//render game
			playerChar.draw(bufferGraphics);
			for (Sprite s : objects) {
				s.draw(bufferGraphics);
			}
		}
		else if (gameState == GameState.START_MENU) {
			bufferGraphics.setColor(Color.white);
			bufferGraphics.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawString("Niel's awesome action-adventure game", PANEL_WIDTH / 2 - 80, PANEL_HEIGHT / 2 - 40);
			bufferGraphics.drawString("Press A to begin", PANEL_WIDTH / 2 - 20, PANEL_HEIGHT / 2 - 10);
			bufferGraphics.drawString("Controls: arrow keys move, A shoots", PANEL_WIDTH / 2 - 80, PANEL_HEIGHT / 2 + 20);
		}
		else if (gameState == GameState.GAME_OVER) {
			bufferGraphics.setColor(Color.white);
			bufferGraphics.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawString("Game over", PANEL_WIDTH / 2 - 60, PANEL_HEIGHT / 2 - 40);
		}
	}
	
	public void updateGameState() {
		if (gameState == GameState.PLAYING) {
			Direction dir = keyboard.getArrowKeyDirection();
			if (keyboard.keyIsDown(KeyEvent.VK_A) && timeUntilNextBullet <= 0) {
				shootBullet(dir);
			}
			playerChar.move(dir, objects);
			playerChar.update(keyboard, objects);
			for (Sprite object : objects) {
				object.update(keyboard, objects);
			}
			List<Sprite> newObjectList = new ArrayList<Sprite>();
			for (Sprite object : objects) {
				if (!object.isDestroyed()) {
					newObjectList.add(object);
				}
			}
			if (playerChar.isDestroyed()) {
				gameState = GameState.GAME_OVER;
			}
			objects = newObjectList;
			if (dir != Direction.NONE) {
				lastPlayerDir = dir;
			}
			if (timeUntilNextBullet > 0) {
				timeUntilNextBullet--;
			}
		}
		else if (gameState == GameState.START_MENU) {
			if (keyboard.keyPressed(KeyEvent.VK_A)) {
				gameState = GameState.PLAYING;
			}
		}
	}
	
	public void shootBullet(Direction dir) {
		int offsetX = 0;
		int offsetY = 0;
		Direction bulletDir = dir;
		if (bulletDir == Direction.NONE) {
			bulletDir = lastPlayerDir;
		}
		bulletDir = DirectionUtils.getComponentDirections(bulletDir).get(0);
		if (bulletDir == Direction.LEFT) {
			offsetX = -(playerChar.getWidth() / 2) - Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.RIGHT) {
			offsetX = (playerChar.getWidth() / 2) + Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.UP) {
			offsetY = -(playerChar.getHeight() / 2) - Bullet.BULLET_HEIGHT;
		}
		else if (bulletDir == Direction.DOWN) {
			offsetY = (playerChar.getHeight() / 2) + Bullet.BULLET_HEIGHT;
		}
		Bullet bullet = new Bullet(playerChar.getX() + offsetX, playerChar.getY() + offsetY, bulletDir);
		objects.add(bullet);
		timeUntilNextBullet = BULLET_COOLDOWN;
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
			
			keyboard.update();
			
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
