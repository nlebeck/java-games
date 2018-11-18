package niellebeck.gameengine;

public abstract class Dialogue {
	private String[] lines;
	private int currentIndex = 0;
	
	public Dialogue(String[] lines) {
		this.lines = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			this.lines[i] = lines[i];
		}
	}
	
	public String getNextLine() {
		String nextLine = null;
		if (currentIndex < lines.length) {
			nextLine = lines[currentIndex];
		}
		currentIndex++;
		return nextLine;
	}
}
