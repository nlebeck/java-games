package niellebeck.game;

import java.awt.event.KeyEvent;

import niellebeck.game.collisionhandlers.BulletBulletCollisionHandler;
import niellebeck.game.collisionhandlers.BulletDoorCollisionHandler;
import niellebeck.game.collisionhandlers.BulletEnemyCollisionHandler;
import niellebeck.game.collisionhandlers.BulletNPCCollisionHandler;
import niellebeck.game.collisionhandlers.BulletPlayerCharacterCollisionHandler;
import niellebeck.game.collisionhandlers.EnemyPlayerCharacterCollisionHandler;
import niellebeck.game.overlays.HpOverlay;
import niellebeck.game.scenes.GameOverScene;
import niellebeck.game.scenes.StartScene;
import niellebeck.game.sprites.Bullet;
import niellebeck.game.sprites.PlayerCharacter;
import niellebeck.gameengine.Direction;
import niellebeck.gameengine.DirectionUtils;
import niellebeck.gameengine.GameLogic;
import niellebeck.gameengine.GameScene;
import niellebeck.gameengine.KeyboardInput;
import niellebeck.gameengine.Scene;

/**
 * A concrete GameLogic implementation for my action-adventure game.
 */
public class MyGameLogic extends GameLogic {
	
	private static final int BULLET_COOLDOWN = 10; //in frames
	private static final int MAX_PLAYER_HP = 20;
	
	private Direction lastPlayerDir;
	private int timeUntilNextBullet;
	
	private int playerHp;
	private boolean levelOneDoorIsOpen;
	
	@Override
	public void init() {
		getGameEngine().registerClassPairCollisionHandler(new BulletEnemyCollisionHandler());
		getGameEngine().registerClassPairCollisionHandler(new EnemyPlayerCharacterCollisionHandler());
		getGameEngine().registerClassPairCollisionHandler(new BulletNPCCollisionHandler());
		getGameEngine().registerClassPairCollisionHandler(new BulletDoorCollisionHandler());
		getGameEngine().registerClassPairCollisionHandler(new BulletBulletCollisionHandler());
		getGameEngine().registerClassPairCollisionHandler(new BulletPlayerCharacterCollisionHandler());
		
		getGameEngine().addOverlay(new HpOverlay());
		
		playerHp = MAX_PLAYER_HP;
		levelOneDoorIsOpen = false;
		
		resetState();
	}
	
	private void resetState() {
		lastPlayerDir = Direction.RIGHT;
		timeUntilNextBullet = BULLET_COOLDOWN;
	}
	
	private PlayerCharacter getPlayerCharacter() {
		GameScene currentScene = (GameScene)getGameEngine().getCurrentScene();
		return (PlayerCharacter)currentScene.getPlayerCharacter();
	}
	
	@Override
	public void update(KeyboardInput keyboard) {
		if (playerHp <= 0) {
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
		return playerHp;
	}
	
	public void damagePlayer() {
		playerHp = (playerHp - 1 >= 0) ? playerHp - 1 : 0;
	}
	
	public void openLevelOneDoor() {
		levelOneDoorIsOpen = true;
	}
	
	public boolean levelOneDoorIsOpen() {
		return levelOneDoorIsOpen;
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
			offsetX = -(playerChar.getWidth()) - Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.RIGHT) {
			offsetX = (playerChar.getWidth()) + Bullet.BULLET_WIDTH;
		}
		else if (bulletDir == Direction.UP) {
			offsetY = -(playerChar.getHeight()) - Bullet.BULLET_HEIGHT;
		}
		else if (bulletDir == Direction.DOWN) {
			offsetY = (playerChar.getHeight()) + Bullet.BULLET_HEIGHT;
		}
		Bullet bullet = new Bullet(playerChar.getX() + offsetX, playerChar.getY() + offsetY, bulletDir);
		getGameEngine().addSprite(bullet);
		playerChar.animateShooting(bulletDir);
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
