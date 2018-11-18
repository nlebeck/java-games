package niellebeck.game;

public abstract class GameLogic {
	private Game game;
	
	protected Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
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
