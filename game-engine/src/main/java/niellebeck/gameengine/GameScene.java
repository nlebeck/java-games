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
	
	public abstract Tilemap getTilemap();
	
	public abstract void update(KeyboardInput keyboard);
	
	public abstract Sprite getPlayerCharacter();
	
	/**
	 * Must return the number of pixels the player character can move each
	 * frame.
	 * <p>
	 * The game engine would ideally not need to know this information at all,
	 * but my current interaction-handling implementation needs to know the
	 * player character's speed in order to tell if the player character is
	 * "next to" another Sprite.
	 */
	public abstract int getPlayerCharacterSpeed();
}
