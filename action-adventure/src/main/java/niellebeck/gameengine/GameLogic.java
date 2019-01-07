package niellebeck.gameengine;

public abstract class GameLogic {
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
	
	public abstract Scene getFirstScene();
	
	public abstract void update(KeyboardInput keyboard);
	
	/**
	 * The place where a GameLogic subclass should perform any resetting or
	 * modifying of its state in response to a Scene change.
	 */
	public abstract void onChangeScene();
}
