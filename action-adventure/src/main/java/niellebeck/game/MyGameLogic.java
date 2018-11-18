package niellebeck.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import niellebeck.game.collisionhandlers.BulletEnemyCollisionHandler;
import niellebeck.game.collisionhandlers.BulletNPCCollisionHandler;
import niellebeck.game.collisionhandlers.EnemyPlayerCharacterCollisionHandler;
import niellebeck.game.collisionhandlers.NPCPlayerCharacterCollisionHandler;
import niellebeck.game.overlays.HpOverlay;

/**
 * A concrete GameLogic implementation for my action-adventure game.
 */
public class MyGameLogic extends GameLogic {
	
	private static final int BULLET_COOLDOWN = 10; //in frames
	
	private PlayerCharacter playerChar;
	private NPC npc;
	private List<Enemy> enemies;
	
	Direction lastPlayerDir;
	int timeUntilNextBullet;
	
	@Override
	public void init() {
		playerChar = new PlayerCharacter(300, 220);
		npc = new NPC(80, 350);
		enemies = new ArrayList<Enemy>();
		enemies.add(new Enemy(400, 400));
		enemies.add(new Enemy(320, 100));
		
		getGame().addSprite(playerChar);
		getGame().addSprite(npc);
		getGame().addSprites(enemies);
		
		getGame().registerCollisionHandler(new BulletEnemyCollisionHandler());
		getGame().registerCollisionHandler(new EnemyPlayerCharacterCollisionHandler());
		getGame().registerCollisionHandler(new BulletNPCCollisionHandler());
		getGame().registerCollisionHandler(new NPCPlayerCharacterCollisionHandler());
		
		getGame().addOverlay(new HpOverlay());
		
		lastPlayerDir = Direction.RIGHT;
		timeUntilNextBullet = BULLET_COOLDOWN;
	}
	
	@Override
	public int getCameraX() {
		return playerChar.getX() + (playerChar.getWidth() / 2);
	}
	
	@Override
	public int getCameraY() {
		return playerChar.getY() + (playerChar.getHeight() / 2);
	}
	
	@Override
	public Tilemap getTilemap() {
		return new Tilemap("/tilemap.txt");
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		if (playerChar.isDestroyed()) {
			getGame().endGame();
			return;
		}
		
		Direction dir = keyboard.getArrowKeyDirection();
		if (keyboard.keyIsDown(KeyEvent.VK_A) && timeUntilNextBullet <= 0) {
			shootBullet(dir);
		}
		
		if (dir != Direction.NONE) {
			lastPlayerDir = dir;
		}
		if (timeUntilNextBullet > 0) {
			timeUntilNextBullet--;
		}
	}
	
	public int getPlayerHp() {
		return playerChar.getHp();
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
		getGame().addSprite(bullet);
		timeUntilNextBullet = BULLET_COOLDOWN;
	}
}
