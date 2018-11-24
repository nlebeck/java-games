package niellebeck.gameengine;

public abstract class GameLogic {
	private GameEngine gameEngine;
	
	/**
	 * Convenience wrapper for GameEngine.getGameEngine() to cut down
	 * on code verbosity.
	 */
	protected GameEngine getGameEngine() {
		return GameEngine.getGameEngine();
	}
	
	/**
	 * The place where a GameLogic subclass should initialize its state
	 * and register it with the GameEngine. Guaranteed to be called
	 * after the GameEngine has been created and its state has been
	 * initialized.
	 */
	public abstract void init();
	
	public abstract int getCameraX();
	
	public abstract int getCameraY();
	
	public abstract Tilemap getTilemap();
	
	public abstract void update(KeyboardInput keyboard);
}
