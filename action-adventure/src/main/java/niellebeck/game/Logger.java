package niellebeck.game;

public class Logger {
	public static void panic(String message) {
		System.err.println("PANIC: " + message);
		System.exit(1);
	}
	
	public static void info(String message) {
		System.out.println("INFO: " + message);
	}
}
