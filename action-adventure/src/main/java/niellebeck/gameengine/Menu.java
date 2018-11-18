package niellebeck.gameengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Menu {
	List<String> menuItems;
	int selectedItem;
	
	public Menu() {
		menuItems = new ArrayList<String>();
		menuItems.add("Status");
		menuItems.add("Items");
		menuItems.add("Save");
		selectedItem = 0;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int)(GamePanel.PANEL_WIDTH * 2.0 / 3.0), 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		g.setColor(Color.black);
		g.drawString("Menu (to be implemented)", GamePanel.PANEL_WIDTH - 180, 20);
		
		for (int i = 0; i < menuItems.size(); i++) {
			int vertOffset = i * 20 + 40;
			int horizOffset = GamePanel.PANEL_WIDTH - 180;
			g.drawString(menuItems.get(i), horizOffset, vertOffset);
			if (selectedItem == i) {
				g.drawRect(horizOffset - 10, vertOffset - 15, 60, 20);
			}
		}
	}
	
	public GameState update(KeyboardInput keyboard) {
		GameState nextState = GameState.MENU;
		if (keyboard.keyPressed(KeyEvent.VK_TAB)) {
			nextState = GameState.PLAYING;
		}
		else {
			if (keyboard.keyPressed(KeyEvent.VK_UP)) {
				moveCursorUp();
			}
			else if (keyboard.keyPressed(KeyEvent.VK_DOWN)) {
				moveCursorDown();
			}
		}
		return nextState;
	}
	
	public void moveCursorUp() {
		selectedItem = selectedItem - 1;
		if (selectedItem < 0) {
			selectedItem = menuItems.size() - 1;
		}
	}
	
	public void moveCursorDown() {
		selectedItem = selectedItem + 1;
		if (selectedItem >= menuItems.size()) {
			selectedItem = 0;
		}
	}
}
