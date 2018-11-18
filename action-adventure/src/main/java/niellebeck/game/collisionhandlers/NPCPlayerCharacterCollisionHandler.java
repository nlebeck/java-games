package niellebeck.game.collisionhandlers;

import niellebeck.game.NPC;
import niellebeck.game.PlayerCharacter;
import niellebeck.gameengine.CollisionHandler;
import niellebeck.gameengine.GameEngine;

public class NPCPlayerCharacterCollisionHandler extends CollisionHandler<NPC, PlayerCharacter> {

	public NPCPlayerCharacterCollisionHandler() {
		super(NPC.class, PlayerCharacter.class);
	}

	@Override
	public double getProximityDistance() {
		return 56.6;
	}
	
	@Override
	public void handleProximityEvent(GameEngine game, NPC npc, PlayerCharacter player) {
		game.registerInteractable(npc);
	}
}
