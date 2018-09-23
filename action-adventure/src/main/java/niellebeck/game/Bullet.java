package niellebeck.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Bullet extends Sprite {
	public static final int BULLET_WIDTH = 10;
	public static final int BULLET_HEIGHT = 10;
	private static final int MAX_DISTANCE = 1000;
	private static final int SPEED = 10;
	
	Direction dir;
	int startingX;
	int startingY;
	
	public Bullet(int initX, int initY, Direction initDir) {
		super(initX, initY, BULLET_WIDTH, BULLET_HEIGHT);
		this.dir = initDir;
		this.startingX = initX;
		this.startingY = initY;
	}
	
	@Override
	public void draw(Graphics g, int cameraX, int cameraY) {
		g.setColor(Color.black);
		g.fillOval(posX - cameraX, posY - cameraY, width, height);
	}
	
	public void destroy() {
		super.destroy();
	}
	
	@Override
	public void update(KeyboardInput keyboard, CollisionManager collisionManager) {
		this.move(dir, SPEED, collisionManager);
		if (Math.abs(this.posX - this.startingX) > MAX_DISTANCE || Math.abs(this.posY - this.startingY) > MAX_DISTANCE) {
			this.destroy();
		}
	}
	
	@Override
	public void onCollide(Sprite sprite) {
		this.destroy();
	}
	
	@Override
	public void onCollideTilemap() {
		this.destroy();
	}
}
