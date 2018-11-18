package niellebeck.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import niellebeck.game.dialogues.Dialogue;

/*
 * NOTE: This class is ugly and has a bunch of hard-coded constants, copied
 * code, etc., right now because I wanted to start by focusing on the code to
 * trigger dialogues and transition in and out of the dialogue state. Once I
 * get that stuff figured out, I will circle around and clean up this class,
 * and enable it to actually show different dialogues.
 */
public class DialogueManager {
	private static DialogueManager singleton = null;
	private Dialogue currentDialogue = null;
	private String currentLine = null;
	private boolean inDialogue = false;
	private int debugDialogueCounter = 0;
	
	public static DialogueManager getInstance() {
		if (singleton == null) {
			singleton = new DialogueManager();
		}
		return singleton;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, (int)(GamePanel.PANEL_HEIGHT * 2.0 / 3.0), GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		
		g.setColor(Color.black);
		g.drawRect(0, (int)(GamePanel.PANEL_HEIGHT * 2.0 / 3.0), GamePanel.PANEL_WIDTH - 1, GamePanel.PANEL_HEIGHT - 1);
		g.drawString(currentLine, 20, (int)(GamePanel.PANEL_HEIGHT * 2.0 / 3.0) + 40);
		g.drawString("Press ENTER to continue...", 20, GamePanel.PANEL_HEIGHT - 40);
	}
	
	public void startDialogue(Dialogue dialogue) {
		currentDialogue = dialogue;
		currentLine = currentDialogue.getNextLine();
		inDialogue = true;
	}
	
	public boolean inDialogue() {
		return inDialogue;
	}
	
	public GameState update(KeyboardInput keyboard) {
		if (inDialogue) {
			if (keyboard.keyPressed(KeyEvent.VK_ENTER)) {
				currentLine = currentDialogue.getNextLine();
				if (currentLine == null) {
					inDialogue = false;
				}
			}
		}
		
		if (inDialogue) {
			return GameState.DIALOGUE;
		}
		else {
			return GameState.PLAYING;
		}
	}
}
