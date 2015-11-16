package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
	int posX;
	int posY;
	int speed;
		
	public Sprite(int initX, int initY) {
		posX = initX;
		posY = initY;
		speed = 2;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(posX, posY, 40, 40);
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, 40, 40);
	}
	
	public void move(Constants.Direction dir, List<Sprite> collisionObjects) {
		int lastPosX = posX;
		int lastPosY = posY;
		
		tempMove(dir);
		
		if (isCollision(this, collisionObjects)) {
			posX = lastPosX;
			posY = lastPosY;
			
			List<Constants.Direction> componentDirs = getComponentDirections(dir);
			for (Constants.Direction componentDir : componentDirs) {
				tempMove(componentDir);
				if (isCollision(this, collisionObjects)) {
					posX = lastPosX;
					posY = lastPosY;
				}
				else {
					break;
				}
			}

		}
		
	}
	
	public void tempMove(Constants.Direction dir) {
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
	
	public List<Constants.Direction> getComponentDirections(Constants.Direction dir) {
		List<Constants.Direction> result = new ArrayList<Constants.Direction>(); 
		if (dir == Constants.Direction.UP_LEFT) {
			result.add(Constants.Direction.UP);
			result.add(Constants.Direction.LEFT);
		}
		else if (dir == Constants.Direction.UP_RIGHT) {
			result.add(Constants.Direction.UP);
			result.add(Constants.Direction.RIGHT);
		}
		else if (dir == Constants.Direction.DOWN_LEFT) {
			result.add(Constants.Direction.DOWN);
			result.add(Constants.Direction.LEFT);
		}
		else if (dir == Constants.Direction.DOWN_RIGHT) {
			result.add(Constants.Direction.DOWN);
			result.add(Constants.Direction.RIGHT);
		}
		else {
			result.add(dir);
		}
		return result;
	}
	
	public boolean isCollision(Sprite sprite, List<Sprite> spriteList) {
		boolean result = false;
		Rectangle spriteBoundingBox = sprite.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox())) {
				result = true;
			}
		}
		return result;
	}
}
