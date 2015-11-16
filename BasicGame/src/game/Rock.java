package game;

import java.awt.Color;
import java.awt.Graphics;

public class Rock extends Sprite {
	public Rock(int initX, int initY) {
		super(initX, initY);
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(posX, posY, 40, 40);
	}
}
