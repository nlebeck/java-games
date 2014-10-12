package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

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
	private static final int PWIDTH = 640;
	private static final int PHEIGHT = 480;
	
	private Thread animator;
	private boolean running = false;
	private int renderPeriod = 17; //in ms
	
	private Graphics bufferGraphics;
	private Image bufferImage = null;
	
	//temp
	float secondCounter = 0;
	int milliCounter = 0;
	
	public GamePanel() {
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
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
			bufferImage = createImage(PWIDTH, PHEIGHT);
			bufferGraphics = bufferImage.getGraphics();
		}
		
		//TODO: render game
		
		//draw background
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, PWIDTH, PHEIGHT);
		
	}
	
	public void updateState() {
		//TODO: actually do stuff
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
			updateState();
			render();
			paintScreen();
			
			timeDiff = System.currentTimeMillis() - beforeTime;
			sleepTime = renderPeriod - timeDiff;
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
