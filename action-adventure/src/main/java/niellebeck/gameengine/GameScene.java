package niellebeck.gameengine;

/**
 * A Scene in which the game engine is running normally, updating and drawing
 * Sprites on top of a TileMap.
 */
public abstract class GameScene extends Scene {
	/**
	 * The place where a GameScene subclass should initialize its Sprites and
	 * register them with the GameEngine, in addition to initializing any other
	 * state.
	 */
	public abstract void init();
	
	public abstract int getCameraX();
	
	public abstract int getCameraY();
	
	public abstract Tilemap getTilemap();
	
	public abstract void update(KeyboardInput keyboard);
	
	public abstract Sprite getPlayerCharacter();
}
