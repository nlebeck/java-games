package niellebeck.gameengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import niellebeck.game.MyGameLogic;

public class GameEngine {
	
	GameLogic gameLogic;
	boolean isGameOver;
	List<Sprite> sprites;
	List<Interactable> interactables;
	CollisionManager collisionManager;
	
	//graphics state
	Tilemap tilemap;
	List<Overlay> overlays;
	
	public GameEngine() {
		isGameOver = false;
		sprites = new ArrayList<Sprite>();
		interactables = new ArrayList<Interactable>();
		
		collisionManager = new CollisionManager(this);
		
		overlays = new ArrayList<Overlay>();
		
		gameLogic = new MyGameLogic();
		gameLogic.setGameEngine(this);
		gameLogic.init();
		tilemap = gameLogic.getTilemap();
	}
	
	public void addOverlay(Overlay overlay) {
		overlays.add(overlay);
	}
	
	public void registerCollisionHandler(CollisionHandler<? extends Sprite, ? extends Sprite> collisionHandler) {
		collisionManager.registerCollisionHandler(collisionHandler);
	}
	
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}
	
	public void addSprites(Collection<? extends Sprite> spriteCollection) {
		sprites.addAll(spriteCollection);
	}
	
	public List<Sprite> getSpriteList() {
		return sprites;
	}
	
	public Tilemap getTilemap() {
		return tilemap;
	}
	
	public void draw(Graphics bufferGraphics) {
		//clear buffer
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		
		int cameraX = gameLogic.getCameraX() - (GamePanel.PANEL_WIDTH / 2);
		int cameraY = gameLogic.getCameraY() - (GamePanel.PANEL_HEIGHT / 2);
		
		//draw background
		tilemap.draw(bufferGraphics, cameraX, cameraY);
	
		//draw sprites
		for (Sprite s : sprites) {
			s.draw(bufferGraphics, cameraX, cameraY);
		}
		
		//draw overlay
		for (Overlay overlay : overlays) {
			overlay.draw(bufferGraphics, gameLogic);
		}
		
		//draw interaction message
		if (interactables.size() > 0) {
			Interactable interactable = chooseInteractable();
			String message = interactable.getInteractionMessage();
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawRect(GamePanel.PANEL_WIDTH / 4, 0, GamePanel.PANEL_WIDTH / 2, 40);
			bufferGraphics.setColor(Color.white);
			bufferGraphics.fillRect(GamePanel.PANEL_WIDTH / 4 + 1, 1, GamePanel.PANEL_WIDTH / 2 - 1, 39);
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawString("Press ENTER to " + message, GamePanel.PANEL_WIDTH / 4 + 10, 20);
		}
	}
	
	public GameState update(KeyboardInput keyboard) {
		GameState nextState = GameState.PLAYING;
		if (keyboard.keyPressed(KeyEvent.VK_TAB)) {
			nextState = GameState.MENU;
		}
		else if (DialogueManager.getInstance().inDialogue()) {
			nextState = GameState.DIALOGUE;
		}
		else {
			interactables.clear();
			
			gameLogic.update(keyboard);
			
			// Call individual Sprite update methods.
			for (Sprite sprite : sprites) {
				sprite.update(keyboard, collisionManager);
			}
			
			// Process collisions.
			collisionManager.processCollisions();
			collisionManager.processProximityEvents();
			
			// Process Interactable interactions.
			if (interactables.size() > 0) {
				if (keyboard.keyPressed(KeyEvent.VK_ENTER)) {
					Interactable interactable = chooseInteractable();
					interactable.interact(gameLogic);
					interactables.clear();
				}
			}
			
			//Remove destroyed sprites.
			List<Sprite> newSpriteList = new ArrayList<Sprite>();
			for (Sprite sprite : sprites) {
				if (!sprite.isDestroyed()) {
					newSpriteList.add(sprite);
				}
			}
			sprites = newSpriteList;
			
			if (isGameOver) {
				nextState = GameState.GAME_OVER;
			}
		}
		return nextState;
	}
	
	public void endGame() {
		isGameOver = true;
	}
	
	/**
	 * Make the given Interactable available for the player character to
	 * interact with by this frame pressing the Enter key.
	 */
	public void registerInteractable(Interactable interactable) {
		interactables.add(interactable);
	}
	
	/**
	 * Select a single Interactable that will be used this frame. If the Enter
	 * key is pressed this frame, the player will interact with this
	 * Interactable; otherwise, an interaction message will be displayed.
	 */
	private Interactable chooseInteractable() {
		if (interactables.size() > 0) {
			return interactables.get(0);
		}
		else {
			return null;
		}
	}
}
