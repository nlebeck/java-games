package niellebeck.gameengine;

public interface InteractionHandler {
	void interact(GameLogic gameLogic, GameScene gameScene);
	String getInteractionMessage();
	double getInteractionDistance();
}
