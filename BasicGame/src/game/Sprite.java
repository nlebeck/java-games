package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.Constants.Direction;
import game.Constants.SpriteType;

public class Sprite {
	int posX;
	int posY;
	int speed;
	SpriteType type;
		
	public Sprite(int initX, int initY) {
		posX = initX;
		posY = initY;
		speed = 0;
		type = SpriteType.BASE_SPRITE;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(posX, posY, 40, 40);
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, 40, 40);
	}
	
	public void move(Direction dir, List<Sprite> objects) {
		int lastPosX = posX;
		int lastPosY = posY;
		
		tempMove(dir);
		
		Set<Sprite> collisionSet = getCollisionSet(this, objects);
		if (collisionSet.size() > 0) {
			posX = lastPosX;
			posY = lastPosY;
			
			List<Direction> componentDirs = getComponentDirections(dir);
			for (Direction componentDir : componentDirs) {
				tempMove(componentDir);
				Set<Sprite> tempCollisionSet = getCollisionSet(this, objects);
				collisionSet.addAll(tempCollisionSet);
				if (tempCollisionSet.size() > 0) {
					posX = lastPosX;
					posY = lastPosY;
				}
				else {
					break;
				}
			}
		}
		
		for (Sprite collidedObject : collisionSet) {
			this.processCollision(collidedObject);
		}
	}
	
	public void processCollision(Sprite collidedObject) {}
	
	public void tempMove(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.UP_LEFT
				|| dir == Direction.DOWN_LEFT) {
			posX -= speed;
		}
		if (dir == Direction.RIGHT || dir == Direction.UP_RIGHT
				|| dir == Direction.DOWN_RIGHT) {
			posX += speed;
		}
		if (dir == Direction.UP || dir == Direction.UP_LEFT
				|| dir == Direction.UP_RIGHT) {
			posY -= speed;
		}
		if (dir == Direction.DOWN || dir == Direction.DOWN_LEFT
				|| dir == Direction.DOWN_RIGHT) {
			posY += speed;
		}
	}
	
	public List<Direction> getComponentDirections(Direction dir) {
		List<Direction> result = new ArrayList<Direction>(); 
		if (dir == Direction.UP_LEFT) {
			result.add(Direction.UP);
			result.add(Direction.LEFT);
		}
		else if (dir == Direction.UP_RIGHT) {
			result.add(Direction.UP);
			result.add(Direction.RIGHT);
		}
		else if (dir == Direction.DOWN_LEFT) {
			result.add(Direction.DOWN);
			result.add(Direction.LEFT);
		}
		else if (dir == Direction.DOWN_RIGHT) {
			result.add(Direction.DOWN);
			result.add(Direction.RIGHT);
		}
		else {
			result.add(dir);
		}
		return result;
	}
	
	public Set<Sprite> getCollisionSet(Sprite sprite, List<Sprite> spriteList) {
		Set<Sprite> collisionSet = new HashSet<Sprite>();
		Rectangle spriteBoundingBox = sprite.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox())) {
				collisionSet.add(otherSprite);
			}
		}
		return collisionSet;
	}
}
