package niellebeck.game.collisionhandlers;

import niellebeck.game.DialogueManager;
import niellebeck.game.NPC;
import niellebeck.game.PlayerCharacter;

/*
 * NOTE: This class is just a stand-in until I can build a system for detecting
 * proximity of objects and switch to using that to trigger dialogue.
 */
public class NPCPlayerCharacterCollisionHandler extends CollisionHandler<NPC, PlayerCharacter> {

	public NPCPlayerCharacterCollisionHandler() {
		super(NPC.class, PlayerCharacter.class);
	}

	@Override
	public void handleCollision(NPC npc, PlayerCharacter player) {
		DialogueManager.getInstance().startDialogue();
	}

}
