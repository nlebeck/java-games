package niellebeck.gameengine;

public abstract class GameLogic {
	private GameEngine gameEngine;
	
	protected GameEngine getGameEngine() {
		return gameEngine;
	}
	
	public void setGameEngine(GameEngine game) {
		this.gameEngine = game;
	}
	
	/**
	 * The place where a GameLogic subclass should initialize its state
	 * and register it with its Game instance. Guaranteed to be called
	 * after a Game is bound to this GameLogic with setGame().
	 */
	public abstract void init();
	
	public abstract int getCameraX();
	
	public abstract int getCameraY();
	
	public abstract Tilemap getTilemap();
	
	public abstract void update(KeyboardInput keyboard);
}
