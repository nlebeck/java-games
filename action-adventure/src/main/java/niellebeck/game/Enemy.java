package niellebeck.game;

public class Enemy extends Sprite {
	public Enemy(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/enemy/enemy.png");
	}
	
	@Override
	public void onCollide(Sprite sprite) {
		if (sprite.getClass() == Bullet.class) {
			this.destroy();
		}
	}
}
