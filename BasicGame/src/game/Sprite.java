package game;

import java.awt.Color;
import java.awt.Graphics;

public class Sprite {
	int posX;
	int posY;
	
	public Sprite(int initX, int initY) {
		posX = initX;
		posY = initY;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(posX, posY, 40, 40);
	}
	
	public void moveLeft() {
		posX -= 2;
	}
	
	public void moveRight() {
		posX += 2;
	}
	
	public void moveUp() {
		posY -= 2;
	}
	
	public void moveDown() {
		posY += 2;
	}
}
