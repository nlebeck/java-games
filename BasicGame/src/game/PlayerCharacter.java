package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class PlayerCharacter extends Sprite {

	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, 40, 40);
		speed = 2;
		type = SpriteType.PLAYER_CHARACTER;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillRect((int)getBoundingBox().getX(),
				   (int)getBoundingBox().getY(),
				   (int)getBoundingBox().getWidth(),
				   (int)getBoundingBox().getHeight());
	}
}
