package niellebeck.gameengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameEngine {
	
	private static GameEngine singleton;
	
	Menu menu;
	
	GameLogic gameLogic;
	Scene currentScene;
	GameState gameState;
	List<Sprite> sprites;
	List<Sprite> newlyCreatedSprites;
	List<Sprite> interactables;
	EventManager eventManager;
	
	//graphics state
	Tilemap tilemap;
	List<Overlay> overlays;
	
	private MusicManager musicManager;
	
	public static GameEngine getGameEngine() {
		if (singleton == null) {
			throw new RuntimeException("The GameEngine has not yet been created.");
		}
		return singleton;
	}
	
	public static GameEngine createGameEngine(GameLogic gameLogic) {
		if (singleton != null) {
			throw new RuntimeException("The GameEngine has already been created.");
		}
		singleton = new GameEngine();
		singleton.initGameLogic(gameLogic);
		return singleton;
	}
	
	public GameEngine() {
		sprites = new ArrayList<Sprite>();
		newlyCreatedSprites = new ArrayList<Sprite>();
		interactables = new ArrayList<Sprite>();
		
		eventManager = new EventManager(this);
		
		overlays = new ArrayList<Overlay>();
		
		menu = new Menu();
		
		musicManager = new MusicManager();
	}
	
	private void initGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.gameLogic.init();
		changeScene(this.gameLogic.getFirstScene());
	}
	
	public void changeScene(Scene scene) {
		sprites.clear();
		this.currentScene = scene;
		if (this.currentScene instanceof GameScene) {
			GameScene gameScene = (GameScene)this.currentScene;
			gameScene.init();
			this.tilemap = gameScene.getTilemap();
			this.gameState = GameState.PLAYING;
		}
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public GameLogic getGameLogic() {
		return gameLogic;
	}
	
	public MusicManager getMusicManager() {
		return musicManager;
	}
	
	public void addOverlay(Overlay overlay) {
		overlays.add(overlay);
	}
	
	public void registerClassPairCollisionHandler(ClassPairCollisionHandler<? extends Sprite, ? extends Sprite> collisionHandler) {
		eventManager.registerClassPairCollisionHandler(collisionHandler);
	}
	
	public void registerClassPairProximityEventHandler(ClassPairProximityEventHandler<? extends Sprite, ? extends Sprite> eventHandler) {
		eventManager.registerClassPairProximityEventHandler(eventHandler);
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public void addSprite(Sprite sprite) {
		newlyCreatedSprites.add(sprite);
	}
	
	public void addSprites(Collection<? extends Sprite> spriteCollection) {
		newlyCreatedSprites.addAll(spriteCollection);
	}
	
	public List<Sprite> getSpriteList() {
		return sprites;
	}
	
	public Tilemap getTilemap() {
		return tilemap;
	}
	
	public void draw(Graphics bufferGraphics) {
		if (currentScene instanceof GameScene) {
			drawGameScene(bufferGraphics, (GameScene)currentScene);
		}
		else if (currentScene instanceof SelfDrawingScene) {
			SelfDrawingScene selfDrawingScene = (SelfDrawingScene)currentScene;
			selfDrawingScene.draw(bufferGraphics, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		}
	}
	
	public void drawGameScene(Graphics bufferGraphics, GameScene gameScene) {
		if (gameState == GameState.PLAYING) {
			drawSpritesAndTilemap(bufferGraphics, gameScene);
		}
		else if (gameState == GameState.MENU) {
			drawSpritesAndTilemap(bufferGraphics, gameScene);
			menu.draw(bufferGraphics);
		}
		else if (gameState == GameState.DIALOGUE) {
			drawSpritesAndTilemap(bufferGraphics, gameScene);
			DialogueManager.getInstance().draw(bufferGraphics);
		}
	}
	
	public void drawSpritesAndTilemap(Graphics bufferGraphics, GameScene gameScene) {
		//clear buffer
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		
		//center camera on player character
		int cameraX = gameScene.getPlayerCharacter().getX() - (GamePanel.PANEL_WIDTH / 2);
		int cameraY = gameScene.getPlayerCharacter().getY() - (GamePanel.PANEL_HEIGHT / 2);
		
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
			Sprite interactable = chooseInteractable();
			String message = interactable.getInteractionHandler().getInteractionMessage();
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawRect(GamePanel.PANEL_WIDTH / 4, 0, GamePanel.PANEL_WIDTH / 2, 40);
			bufferGraphics.setColor(Color.white);
			bufferGraphics.fillRect(GamePanel.PANEL_WIDTH / 4 + 1, 1, GamePanel.PANEL_WIDTH / 2 - 1, 39);
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawString("Press ENTER to " + message, GamePanel.PANEL_WIDTH / 4 + 10, 20);
		}
	}
	
	public void update(KeyboardInput keyboard) {
		if (currentScene instanceof GameScene) {
			updateActiveComponent(keyboard, (GameScene)currentScene);
		}
		else if (currentScene instanceof SelfDrawingScene) {
			SelfDrawingScene selfDrawingScene = (SelfDrawingScene)currentScene;
			selfDrawingScene.update(keyboard);
		}
	}
	
	public void updateActiveComponent(KeyboardInput keyboard, GameScene gameScene) {
		if (gameState == GameState.PLAYING) {
			gameState = updateGameScene(keyboard, gameScene);
		}
		else if (gameState == GameState.MENU) {
			gameState = menu.update(keyboard);
		}
		else if (gameState == GameState.DIALOGUE) {
			gameState = DialogueManager.getInstance().update(keyboard);
		}
	}
	
	public GameState updateGameScene(KeyboardInput keyboard, GameScene gameScene) {
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
			gameScene.update(keyboard);
			
			// Call individual Sprite update methods.
			for (Sprite sprite : sprites) {
				sprite.update(keyboard);
			}
			
			// Call Sprite MoveBehaviors' update methods.
			for (Sprite sprite : sprites) {
				if (sprite.getTimedMoveBehavior() != null) {
					sprite.getTimedMoveBehavior().update(keyboard);
				}
				if (sprite.getStaticMoveBehavior() != null) {
					sprite.getStaticMoveBehavior().update(keyboard);
				}
			}
			
			// Move Sprites
			for (Sprite sprite : sprites) {
				sprite.move();
			}
			
			// Process the different kinds of events.
			eventManager.processCollisions();
			eventManager.processProximityEvents();
			eventManager.registerInteractableSprites(gameScene);
			
			// Process interactions.
			if (interactables.size() > 0) {
				if (keyboard.keyPressed(KeyEvent.VK_ENTER)) {
					Sprite interactable = chooseInteractable();
					interactable.getInteractionHandler().interact();
					interactables.clear();
				}
			}
			
			// Remove destroyed sprites and add newly created sprites.
			List<Sprite> newSpriteList = new ArrayList<Sprite>();
			for (Sprite sprite : sprites) {
				if (!sprite.isDestroyed()) {
					newSpriteList.add(sprite);
				}
			}
			for (Sprite sprite : newlyCreatedSprites) {
				newSpriteList.add(sprite);
			}
			sprites = newSpriteList;
			newlyCreatedSprites.clear();
		}
		return nextState;
	}
	
	/**
	 * Make the given Sprite available for the player character to
	 * interact with by this frame pressing the Enter key.
	 */
	public void registerInteractable(Sprite sprite) {
		if (!sprite.isInteractable()) {
			Logger.info("Attempted to register a non-interactable sprite.");
			return;
		}
		interactables.add(sprite);
	}
	
	/**
	 * Select a single interactable Sprite that will be used this frame. If
	 * the Enter key is pressed this frame, the player will interact with this
	 * Sprite; otherwise, an interaction message will be displayed.
	 */
	private Sprite chooseInteractable() {
		if (interactables.size() > 0) {
			return interactables.get(0);
		}
		else {
			return null;
		}
	}
}
