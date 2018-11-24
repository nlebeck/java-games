package niellebeck.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable {
	public static final int PANEL_WIDTH = 640;
	public static final int PANEL_HEIGHT = 480;
	public static final int RENDER_PERIOD_MS = 17;
	
	private Thread animator;
	private boolean running = false;
	
	private Graphics bufferGraphics;
	private Image bufferImage = null;
	
	GameState gameState;
	KeyboardInput keyboard;
	Menu menu;
	GameEngine game;
	
	public static void createWindow(GameLogic gameLogic) {
		JFrame f = new JFrame("Game Window");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(640, 480);
		f.add(new GamePanel(gameLogic));
		f.pack();
		f.setVisible(true);
	}
	
	private GamePanel(GameLogic gameLogic) {
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setFocusable(true);
				
		gameState = GameState.START_MENU;
		
		keyboard = new KeyboardInput(this);
		menu = new Menu();
		game = GameEngine.createGameEngine(gameLogic);
		
		this.setFocusTraversalKeysEnabled(false);
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
			game.draw(bufferGraphics);
		}
		else if (gameState == GameState.MENU) {
			game.draw(bufferGraphics);
			menu.draw(bufferGraphics);
		}
		else if (gameState == GameState.DIALOGUE) {
			game.draw(bufferGraphics);
			DialogueManager.getInstance().draw(bufferGraphics);
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
			gameState = game.update(keyboard);
		}
		else if (gameState == GameState.MENU) {
			gameState = menu.update(keyboard);
		}
		else if (gameState == GameState.DIALOGUE) {
			gameState = DialogueManager.getInstance().update(keyboard);
		}
		else if (gameState == GameState.START_MENU) {
			if (keyboard.keyPressed(KeyEvent.VK_A)) {
				gameState = GameState.PLAYING;
			}
		}
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
		
		long beforeTimeNs = System.nanoTime();
		
		while (running) {
			updateGameState();
			render();
			paintScreen();
			
			keyboard.update();
			
			long timeDiffMs = (System.nanoTime() - beforeTimeNs) / (1000 * 1000);
			long sleepTimeMs = RENDER_PERIOD_MS - timeDiffMs;
			if (sleepTimeMs < 0) {
				sleepTimeMs = 5;
			}
			
			try {
				Thread.sleep(sleepTimeMs);
			}
			catch (InterruptedException e) {}
			
			beforeTimeNs = System.nanoTime();
		}
	}
}
