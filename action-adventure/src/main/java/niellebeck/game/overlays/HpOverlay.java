package niellebeck.game.overlays;

import java.awt.Color;
import java.awt.Graphics;

import niellebeck.game.GameLogic;
import niellebeck.game.MyGameLogic;

public class HpOverlay extends Overlay {
	public void draw(Graphics g, GameLogic gameLogic) {
		MyGameLogic myGameLogic = (MyGameLogic)gameLogic;
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 80, 40);
		g.setColor(Color.white);
		g.fillRect(1, 1, 79, 39);
		g.setColor(Color.black);
		g.drawString("HP: " + myGameLogic.getPlayerHp(), 10, 20);
	}
}
