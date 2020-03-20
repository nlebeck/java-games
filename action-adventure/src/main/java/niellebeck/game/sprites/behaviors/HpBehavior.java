package niellebeck.game.sprites.behaviors;

import niellebeck.gameengine.Behavior;
import niellebeck.gameengine.KeyboardInput;

public class HpBehavior implements Behavior {

	private int hp;
	
	public HpBehavior(int startingHp) {
		hp = startingHp;
	}
	
	public boolean isDead() {
		return hp <= 0;
	}
	
	public void damage(int damage) {
		hp = hp - damage;
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		
	}

}
