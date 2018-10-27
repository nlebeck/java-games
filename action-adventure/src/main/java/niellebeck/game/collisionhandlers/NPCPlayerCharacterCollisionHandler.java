package niellebeck.game.collisionhandlers;

import niellebeck.game.Game;
import niellebeck.game.NPC;
import niellebeck.game.PlayerCharacter;

public class NPCPlayerCharacterCollisionHandler extends CollisionHandler<NPC, PlayerCharacter> {

	public NPCPlayerCharacterCollisionHandler() {
		super(NPC.class, PlayerCharacter.class);
	}

	@Override
	public double getProximityDistance() {
		return 56.6;
	}
	
	@Override
	public void handleProximityEvent(Game game, NPC npc, PlayerCharacter player) {
		game.registerInteractable(npc);
	}
}
