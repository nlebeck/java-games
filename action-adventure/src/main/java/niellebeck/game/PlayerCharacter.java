package niellebeck.game;

public class PlayerCharacter extends Sprite {

	public PlayerCharacter(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/stickfigure/standing.png");
		speed = 2;
		type = SpriteType.PLAYER_CHARACTER;
	}
}
