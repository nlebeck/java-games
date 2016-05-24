package niellebeck.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private static final int BULLET_COOLDOWN = 10; //in frames
	
	PlayerCharacter playerChar;
	List<Sprite> objects;
	Direction lastPlayerDir;
	int timeUntilNextBullet;
	
	//graphics state
	Tilemap tilemap;
	
	public Game() {
		playerChar = new PlayerCharacter(300, 220);
		objects = new ArrayList<Sprite>();
		objects.add(new Rock(100, 100));
		objects.add(new Rock(500, 150));
		objects.add(new Rock(200, 200));
		lastPlayerDir = Direction.RIGHT;
		timeUntilNextBullet = BULLET_COOLDOWN;
		
		tilemap = new Tilemap("/tilemap.txt");
	}
	
	public void draw(Graphics bufferGraphics) {
		//clear buffer
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		
		//draw background
		tilemap.draw(bufferGraphics);
	
		//render game
		playerChar.draw(bufferGraphics);
		for (Sprite s : objects) {
			s.draw(bufferGraphics);
		}
	}
	
	public GameState update(KeyboardInput keyboard) {
		GameState nextState = GameState.PLAYING;
		if (keyboard.keyPressed(KeyEvent.VK_TAB)) {
			nextState = GameState.MENU;
		}
		else {
			Direction dir = keyboard.getArrowKeyDirection();
			if (keyboard.keyIsDown(KeyEvent.VK_A) && timeUntilNextBullet <= 0) {
				shootBullet(dir);
			}
			playerChar.move(dir, objects);
			playerChar.update(keyboard, objects);
			for (Sprite object : objects) {
				object.update(keyboard, objects);
			}
			List<Sprite> newObjectList = new ArrayList<Sprite>();
			for (Sprite object : objects) {
				if (!object.isDestroyed()) {
					newObjectList.add(object);
				}
			}
			if (playerChar.isDestroyed()) {
				nextState = GameState.GAME_OVER;
			}
			objects = newObjectList;
			if (dir != Direction.NONE) {
				lastPlayerDir = dir;
			}
			if (timeUntilNextBullet > 0) {
				timeUntilNextBullet--;
			}
		}
		return nextState;
	}
	
	
	public void shootBullet(Direction dir) {
		int offsetX = 0;
		int offsetY = 0;
		Direction bulletDir = dir;
		if (bulletDir == Direction.NONE) {
			bulletDir = lastPlayerDir;
		}
		bulletDir = DirectionUtils.getComponentDirections(bulletDir).get(0);
		if (bulletDir == Direction.LEFT) {
			offsetX = -(playerChar.getWidth() / 2) - Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.RIGHT) {
			offsetX = (playerChar.getWidth() / 2) + Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.UP) {
			offsetY = -(playerChar.getHeight() / 2) - Bullet.BULLET_HEIGHT;
		}
		else if (bulletDir == Direction.DOWN) {
			offsetY = (playerChar.getHeight() / 2) + Bullet.BULLET_HEIGHT;
		}
		Bullet bullet = new Bullet(playerChar.getX() + offsetX, playerChar.getY() + offsetY, bulletDir);
		objects.add(bullet);
		timeUntilNextBullet = BULLET_COOLDOWN;
	}
}
