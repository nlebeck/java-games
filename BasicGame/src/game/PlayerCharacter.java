package game;

import game.Constants.SpriteType;

public class PlayerCharacter extends Sprite {

	public PlayerCharacter(int initX, int initY) {
		super(initX, initY);
		speed = 2;
		type = SpriteType.PLAYER_CHARACTER;
	}
	
	public void processCollision(Sprite collidedObject) {
		System.out.println("Player char collided with object of type " + collidedObject.type);
	}

}
