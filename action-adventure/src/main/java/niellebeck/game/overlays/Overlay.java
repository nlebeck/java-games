package niellebeck.game.overlays;

import java.awt.Graphics;

import niellebeck.game.GameLogic;

public abstract class Overlay {
	public abstract void draw(Graphics g, GameLogic gameLogic);
}
