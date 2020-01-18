package niellebeck.gameengine;

public abstract class Scene {
	/**
	 * Convenience wrapper for GameEngine.getGameEngine() like in GameLogic.
	 */
	protected GameEngine getGameEngine() {
		return GameEngine.getGameEngine();
	}
}
