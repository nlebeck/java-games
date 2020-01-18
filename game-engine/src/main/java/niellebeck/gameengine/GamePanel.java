package niellebeck.gameengine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

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
	
	KeyboardInput keyboard;
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
		
		keyboard = new KeyboardInput(this);
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
		
		game.draw(bufferGraphics);
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
			game.update(keyboard);
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
