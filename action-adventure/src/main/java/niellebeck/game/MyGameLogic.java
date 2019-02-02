package niellebeck.game;

import java.awt.event.KeyEvent;

import niellebeck.game.collisionhandlers.BulletEnemyCollisionHandler;
import niellebeck.game.collisionhandlers.BulletNPCCollisionHandler;
import niellebeck.game.collisionhandlers.EnemyPlayerCharacterCollisionHandler;
import niellebeck.game.overlays.HpOverlay;
import niellebeck.game.scenes.BaseGameScene;
import niellebeck.game.scenes.GameOverScene;
import niellebeck.game.scenes.StartScene;
import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.GameLogic;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Scene;

/**
 * A concrete GameLogic implementation for my action-adventure game.
 */
public class MyGameLogic extends GameLogic {
	
	private static final int BULLET_COOLDOWN = 10; //in frames
	
	Direction lastPlayerDir;
	int timeUntilNextBullet;
	
	@Override
	public void init() {
		getGameEngine().registerCollisionHandler(new BulletEnemyCollisionHandler());
		getGameEngine().registerCollisionHandler(new EnemyPlayerCharacterCollisionHandler());
		getGameEngine().registerCollisionHandler(new BulletNPCCollisionHandler());
		
		getGameEngine().addOverlay(new HpOverlay());
		
		resetState();
	}
	
	private void resetState() {
		lastPlayerDir = Direction.RIGHT;
		timeUntilNextBullet = BULLET_COOLDOWN;
	}
	
	private BaseGameScene getCurrentGameScene() {
		Scene scene = getGameEngine().getCurrentScene();
		return (BaseGameScene)scene;
	}
	
	private PlayerCharacter getPlayerCharacter() {
		return (PlayerCharacter)getCurrentGameScene().getPlayerCharacter();
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		if (getPlayerCharacter().isDestroyed()) {
			getGameEngine().changeScene(new GameOverScene());
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
		return getPlayerCharacter().getHp();
	}
	
	public void shootBullet(Direction dir) {
		PlayerCharacter playerChar = getPlayerCharacter();
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
		getGameEngine().addSprite(bullet);
		timeUntilNextBullet = BULLET_COOLDOWN;
	}

	@Override
	public Scene getFirstScene() {
		return new StartScene();
	}

	@Override
	public void onChangeScene() {
		resetState();
	}
}
