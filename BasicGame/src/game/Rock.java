package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Rock extends Sprite {
	public Rock(int initX, int initY) {
		super(initX, initY, 40, 40);
		type = SpriteType.ROCK;
	}

	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillOval((int)getBoundingBox().getX(),
				   (int)getBoundingBox().getY(),
				   (int)getBoundingBox().getWidth(),
				   (int)getBoundingBox().getHeight());
	}
}
