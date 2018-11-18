package niellebeck.game.dialogues;

import niellebeck.gameengine.Dialogue;

public class TestDialogue extends Dialogue {

	public TestDialogue() {
		super(new String[]{
			"This dialogue is very meaningful.",
			"It contains real words that someone might say.",
			"I'll need to figure out how to handle word wrapping..."
		});
	}

}
