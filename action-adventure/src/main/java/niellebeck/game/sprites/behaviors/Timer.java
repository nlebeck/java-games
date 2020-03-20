package niellebeck.game.sprites.behaviors;

import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.TimedBehavior;

public class Timer implements TimedBehavior {

	private int timeLeft;
	
	public Timer(int time) {
		timeLeft = time;
	}
	@Override
	public void update(KeyboardInput keyboard) {
		timeLeft--;
	}

	@Override
	public boolean isDone() {
		return timeLeft <= 0;
	}

}
