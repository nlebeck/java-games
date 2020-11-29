package niellebeck.gameengine;

import static niellebeck.gameengine.GameEngine.getGameEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Menu {
	public static class Item {
		private String name;
		private Action action;
		
		public Item(String name, Action action) {
			this.name = name;
			this.action = action;
		}
	}
	
	/** A closure that runs when a menu item is selected. */
	public static interface Action {
		void onSelect();
	}
	
	/**
	 * An {@link Action} that navigates to a particular child menu, pushing the
	 * current {@link Menu} onto the menu stack.
	 */
	public static class NavigateAction implements Action {
		private Menu childMenu;
		
		public NavigateAction(Menu childMenu) {
			this.childMenu = childMenu;
		}
		
		@Override
		public void onSelect() {
			getGameEngine().pushMenu(childMenu);
		}
	}
	
	List<Item> menuItems;
	int selectedItem;
	
	public Menu(List<Item> items) {
		menuItems = new ArrayList<Item>();
		menuItems.addAll(items);
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
			g.drawString(menuItems.get(i).name, horizOffset, vertOffset);
			if (selectedItem == i) {
				g.drawRect(horizOffset - 10, vertOffset - 15, 60, 20);
			}
		}
	}
	
	public GameState update(KeyboardInput keyboard) {
		GameState nextState = GameState.MENU;
		if (keyboard.keyPressed(KeyEvent.VK_TAB)) {
			// Pop the menu stack all the way back to the root menu and return
			// to the game.
			while (!getGameEngine().onRootMenu()) {
				getGameEngine().popMenu();
			}
			nextState = GameState.PLAYING;
		}
		else if (keyboard.keyPressed(KeyEvent.VK_BACK_SPACE)) {
			// Pop the menu stack once, or return to the game if the root menu
			// is currently active.
			if (getGameEngine().onRootMenu()) {
				nextState = GameState.PLAYING;
			} else {
				getGameEngine().popMenu();
			}
		}
		else if (keyboard.keyPressed(KeyEvent.VK_UP)) {
			moveCursorUp();
		}
		else if (keyboard.keyPressed(KeyEvent.VK_DOWN)) {
			moveCursorDown();
		} else if (keyboard.keyPressed(KeyEvent.VK_ENTER)) {
			menuItems.get(selectedItem).action.onSelect();
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
