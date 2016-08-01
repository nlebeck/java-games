package niellebeck.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class Tilemap {
	private static final String TILEMAP_MAGIC_WORD = "tilemap";
	
	private int[] tiles;
	private int numRows;
	private int numCols;
	private int tileWidth;
	private Image[] images;
	private Set<Integer> collidableTiles;
	
	public Tilemap(String tilemapFile) {
		collidableTiles = new HashSet<Integer>();
		
		numRows = -1;
		numCols = -1;
		int numTiles = -1;
		
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream(tilemapFile)))) {
			
			String header = reader.readLine();
			String[] headerSplit = header.split("\\s+");
			if (headerSplit.length < 5 || !headerSplit[0].equalsIgnoreCase(TILEMAP_MAGIC_WORD)) {
				Logger.panic("Malformed tilemap header");
			}
			numRows = Integer.parseInt(headerSplit[1]);
			numCols = Integer.parseInt(headerSplit[2]);
			numTiles = Integer.parseInt(headerSplit[3]);
			tileWidth = Integer.parseInt(headerSplit[4]);
			
			tiles = new int[numRows * numCols];
			images = new Image[numTiles];
			
			for (int k = 0; k < numTiles; k++) {
				String tileLine = reader.readLine();
				String[] tileLineSplit = tileLine.split("\\s+");
				
				int tileIndex = Integer.parseInt(tileLineSplit[0]);
				
				String imagePath = tileLineSplit[1];
				images[tileIndex] = ResourceLoader.loadImage(imagePath);
				
				if (tileLineSplit.length >= 3 && tileLineSplit[2].equals("collidable")) {
					collidableTiles.add(tileIndex);
				}
			}
			
			for (int j = 0; j < numRows; j++) {
				String tileMapLine = reader.readLine();
				String[] tileMapLineSplit = tileMapLine.split("\\s+");
				for (int i = 0; i < numCols; i++) {
					tiles[j * numCols + i] = Integer.parseInt(tileMapLineSplit[i]);
				}
			}
		} catch (IOException e) {
			Logger.panic("Error reading tilemap file: " + e);
		}
	}
	
	public void draw(Graphics g, int cameraX, int cameraY) {
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				int tile = tiles[j * numCols + i];
				g.drawImage(images[tile], i * tileWidth - cameraX, j * tileWidth - cameraY, null);
			}
		}
	}
	
	public boolean collidesWithSprite(Sprite sprite) {
		Rectangle boundingBox = sprite.getBoundingBox();
		
		int leftmostTileCol = boundingBox.x / tileWidth;
		int rightmostTileCol = (boundingBox.x + boundingBox.width) / tileWidth + 1;
		int uppermostTileRow = boundingBox.y / tileWidth;
		int lowermostTileRow = (boundingBox.y + boundingBox.height) / tileWidth + 1;
		
		for (int i = leftmostTileCol; i < rightmostTileCol; i++) {
			for (int j = uppermostTileRow; j < lowermostTileRow; j++) {
				Rectangle tileBoundingBox = new Rectangle(i * tileWidth, j * tileWidth, tileWidth, tileWidth);
				int tile = tiles[j * numCols + i];
				if (tileBoundingBox.intersects(boundingBox) && collidableTiles.contains(tile)) {
					return true;
				}
			}
		}
		return false;
	}
}
