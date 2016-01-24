package game;

import java.awt.Color;
import java.awt.Graphics;

import game.Constants.SpriteType;

public class Rock extends Sprite {
	public Rock(int initX, int initY) {
		super(initX, initY);
		type = SpriteType.ROCK;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(posX, posY, 40, 40);
	}
}
