package niellebeck.gameengine;

import java.awt.Image;

public class TimedAnimation extends Animation {

	private int maxTime;
	private int timer;
	
	public TimedAnimation(int timeInTicks, int ticksPerImage, String...strings) {
		super(ticksPerImage, strings);
		maxTime = timeInTicks;
		reset();
	}
	
	public Image animate() {
		timer--;
		return super.animate();
	}

	public boolean done() {
		return timer <= 0;
	}
	
	public void reset() {
		timer = maxTime;
	}
}
