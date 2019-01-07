package niellebeck.game.scenes;

import java.awt.Color;
import java.awt.Graphics;

import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.SelfDrawingScene;

public class GameOverScene extends SelfDrawingScene {

	@Override
	public void draw(Graphics g, int panelWidth, int panelHeight) {
		g.setColor(Color.white);
		g.fillRect(0, 0, panelWidth, panelHeight);
		g.setColor(Color.black);
		g.drawString("Game over", panelWidth / 2 - 60, panelHeight / 2 - 40);
	}

	@Override
	public void update(KeyboardInput keyboard) {
		
	}
}
