package niellebeck.game;

import niellebeck.game.dialogues.TestDialogue;

public class NPC extends Sprite implements Interactable {

	public NPC(int initX, int initY) {
		super(initX, initY, 40, 40, "/sprites/initial_npc/initial_npc.png");
	}

	@Override
	public void update(KeyboardInput keyboard, CollisionManager collisionManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollideTilemap() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String getInteractionMessage() {
		return "talk with NPC";
	}
	
	public void interact() {
		DialogueManager.getInstance().startDialogue(new TestDialogue());
	}
}
