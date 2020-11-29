package niellebeck.game.scenes;

import static niellebeck.gameengine.GameEngine.getGameEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.SelfDrawingScene;

public class StartScene extends SelfDrawingScene {

	@Override
	public void draw(Graphics g, int panelWidth, int panelHeight) {
		g.setColor(Color.white);
		g.fillRect(0, 0, panelWidth, panelHeight);
		g.setColor(Color.black);
		g.drawString("Niel's awesome action-adventure game", panelWidth / 2 - 80, panelHeight / 2 - 40);
		g.drawString("Press A to begin", panelWidth / 2 - 20, panelHeight / 2 - 10);
		g.drawString("Controls: arrow keys move, A shoots", panelWidth / 2 - 80, panelHeight / 2 + 20);
	}

	@Override
	public void update(KeyboardInput keyboard) {
		if (keyboard.keyPressed(KeyEvent.VK_A)) {
			getGameEngine().changeScene(new LevelOneScene());
		}
	}
}
