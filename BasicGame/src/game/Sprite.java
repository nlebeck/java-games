package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Sprite {
	int posX;
	int posY;
	int speed;
	
	int lastPosX;
	int lastPosY;
	
	public Sprite(int initX, int initY) {
		posX = initX;
		posY = initY;
		speed = 2;
		lastPosX = initX;
		lastPosY = initY;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(posX, posY, 40, 40);
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, 40, 40);
	}
	
	public void move(Constants.Direction dir) {
		lastPosX = posX;
		lastPosY = posY;
		
		if (dir == Constants.Direction.LEFT || dir == Constants.Direction.UP_LEFT
				|| dir == Constants.Direction.DOWN_LEFT) {
			posX -= speed;
		}
		if (dir == Constants.Direction.RIGHT || dir == Constants.Direction.UP_RIGHT
				|| dir == Constants.Direction.DOWN_RIGHT) {
			posX += speed;
		}
		if (dir == Constants.Direction.UP || dir == Constants.Direction.UP_LEFT
				|| dir == Constants.Direction.UP_RIGHT) {
			posY -= speed;
		}
		if (dir == Constants.Direction.DOWN || dir == Constants.Direction.DOWN_LEFT
				|| dir == Constants.Direction.DOWN_RIGHT) {
			posY += speed;
		}
		
	}
	
	public void undoLastMove() {
		posX = lastPosX;
		posY = lastPosY;
	}
}
