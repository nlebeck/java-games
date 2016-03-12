package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Bullet extends Sprite {
	public static final int BULLET_WIDTH = 10;
	public static final int BULLET_HEIGHT = 10;
	private static final int MAX_DISTANCE = 1000;
	
	
	Direction dir;
	int startingX;
	int startingY;
	
	public Bullet(int initX, int initY, Direction initDir) {
		super(initX, initY, BULLET_WIDTH, BULLET_HEIGHT);
		this.dir = initDir;
		this.speed = 10;
		this.startingX = initX;
		this.startingY = initY;
		this.type = SpriteType.BULLET;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(posX, posY, width, height);
	}
	
	public void destroy() {
		super.destroy();
	}

	public void update(List<Sprite> objects) {
		this.move(dir, objects);
		if (Math.abs(this.posX - this.startingX) > MAX_DISTANCE || Math.abs(this.posY - this.startingY) > MAX_DISTANCE) {
			this.destroy();
		}
	}
}
