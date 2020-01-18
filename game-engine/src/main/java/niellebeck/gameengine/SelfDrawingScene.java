package niellebeck.gameengine;

import java.awt.Graphics;

/**
 * A Scene that draws its own image on the screen.
 */
public abstract class SelfDrawingScene extends Scene {
	public abstract void draw(Graphics g, int panelWidth, int panelHeight);
	public abstract void update(KeyboardInput keyboard);
}
