package niellebeck.gameengine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Sprite {
	
	private static final boolean HIGHLIGHT_INVISIBLE_SPRITES = false;
	
	protected int posX; //x- and y-coordinates of the center of the sprite
	protected int posY;
	protected boolean destroyed;
	protected int width;
	protected int height;
	protected Image img;
	
	private InteractionHandler interactionHandler;
	
	private MoveBehavior staticMoveBehavior;
	private TimedMoveBehavior timedMoveBehavior;
	
	private List<Behavior> behaviors;
	private List<TimedBehavior> timedBehaviors;
		
	public Sprite(int initX, int initY, int initWidth, int initHeight, String imagePath) {
		this(initX, initY, initWidth, initHeight);
		img = ResourceLoader.loadImage(imagePath);
	}
	
	protected Sprite(int initX, int initY, int initWidth, int initHeight) {
		posX = initX;
		posY = initY;
		width = initWidth;
		height = initHeight;
		destroyed = false;
		img = null;
		interactionHandler = null;
		
		staticMoveBehavior = null;
		timedMoveBehavior = null;
		
		behaviors = new ArrayList<Behavior>();
		timedBehaviors = new ArrayList<TimedBehavior>();
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isInteractable() {
		return (interactionHandler != null);
	}
	
	public InteractionHandler getInteractionHandler() {
		return interactionHandler;
	}
	
	public void setInteractionHandler(InteractionHandler handler) {
		interactionHandler = handler;
	}
	
	public void draw(Graphics g, int cameraX, int cameraY) {
		if (img == null) {
			if (HIGHLIGHT_INVISIBLE_SPRITES) {
				g.setColor(java.awt.Color.RED);
				g.drawRect(posX - (width / 2) - cameraX, posY - (height / 2) - cameraY, width, height);
			}
		}
		else {
			g.drawImage(img, posX - (width / 2) - cameraX, posY - (height / 2) - cameraY, width, height, null);
		}
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX - (width / 2), posY - (height / 2), width, height);
	}
	
	public void setStaticMoveBehavior(MoveBehavior mb) {
		staticMoveBehavior = mb;
	}
	
	public MoveBehavior getStaticMoveBehavior() {
		return staticMoveBehavior;
	}
	
	public void setTimedMoveBehavior(TimedMoveBehavior tmb) {
		timedMoveBehavior = tmb;
	}
	
	public MoveBehavior getTimedMoveBehavior() {
		return timedMoveBehavior;
	}
	
	public MoveBehavior getActiveMoveBehavior() {
		if (timedMoveBehavior != null && !timedMoveBehavior.isDone()) {
			return timedMoveBehavior;
		}
		return staticMoveBehavior;
	}
	
	public void updateBehaviors(KeyboardInput keyboard) {
		for (Behavior behavior : behaviors) {
			behavior.update(keyboard);
		}
		
		List<TimedBehavior> removeList = new ArrayList<TimedBehavior>();
		for (TimedBehavior timedBehavior : timedBehaviors) {
			timedBehavior.update(keyboard);
			if (timedBehavior.isDone()) {
				removeList.add(timedBehavior);
			}
		}
		for (TimedBehavior timedBehavior : removeList) {
			timedBehaviors.remove(timedBehavior);
		}
	}
	
	public void addBehavior(Behavior behavior) {
		behaviors.add(behavior);
	}
	
	public void addTimedBehavior(TimedBehavior timedBehavior) {
		timedBehaviors.add(timedBehavior);
	}
	
	/**
	 * Move the Sprite according to the direction and distance
	 * specified by its MoveBehavior. If this Sprite has both a timed
	 * and a static MoveBehavior, the timed MoveBehavior takes
	 * precedence. This method should only be called by the game
	 * engine.
	 */
	public void move() {
		Direction moveDir = Direction.NONE;
		int moveDistance = 0;
		MoveBehavior activeMoveBehavior = staticMoveBehavior;
		if (timedMoveBehavior != null) {
			if (timedMoveBehavior.isDone()) {
				timedMoveBehavior = null;
			}
			else {
				activeMoveBehavior = timedMoveBehavior;
			}
		}
		
		if (activeMoveBehavior != null) {
			moveDir = activeMoveBehavior.getMoveDirection();
			moveDistance = activeMoveBehavior.getMoveDistance();
		}
		
		if (moveDir == Direction.NONE) {
			return;
		}
		
		int lastPosX = posX;
		int lastPosY = posY;
		
		tempMove(moveDir, moveDistance);
		
		EventManager collisionManager = GameEngine.getGameEngine().getEventManager();
		boolean collision = collisionManager.testAndAddCollisions(this);
		if (collision) {
			posX = lastPosX;
			posY = lastPosY;
			
			List<Direction> componentDirs = DirectionUtils.getComponentDirections(moveDir);
			for (Direction componentDir : componentDirs) {
				tempMove(componentDir, moveDistance);
				collision = collisionManager.testAndAddCollisions(this);
				if (collision) {
					posX = lastPosX;
					posY = lastPosY;
				}
				else {
					break;
				}
			}
		}
		
		moveDir = Direction.NONE;
		moveDistance = 0;
	}
	
	public Set<Sprite> getCollisionSet(List<Sprite> spriteList) {
		Set<Sprite> collisionSet = new HashSet<Sprite>();
		Rectangle spriteBoundingBox = this.getBoundingBox();
		for (Sprite otherSprite : spriteList) {
			if (spriteBoundingBox.intersects(otherSprite.getBoundingBox()) && otherSprite != this) {
				collisionSet.add(otherSprite);
			}
		}
		return collisionSet;
	}
		
	private void tempMove(Direction dir, int distance) {
		if (dir == Direction.LEFT || dir == Direction.UP_LEFT
				|| dir == Direction.DOWN_LEFT) {
			posX -= distance;
		}
		if (dir == Direction.RIGHT || dir == Direction.UP_RIGHT
				|| dir == Direction.DOWN_RIGHT) {
			posX += distance;
		}
		if (dir == Direction.UP || dir == Direction.UP_LEFT
				|| dir == Direction.UP_RIGHT) {
			posY -= distance;
		}
		if (dir == Direction.DOWN || dir == Direction.DOWN_LEFT
				|| dir == Direction.DOWN_RIGHT) {
			posY += distance;
		}
	}

	public abstract void update(KeyboardInput keyboard);
	
	public abstract void onCollideTilemap();
}
