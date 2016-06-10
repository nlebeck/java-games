package niellebeck.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tilemap {
	private static final String TILEMAP_MAGIC_WORD = "tilemap";
	
	private int[] tiles;
	private int numRows;
	private int numCols;
	private int tileWidth;
	private Image[] images;
	
	public Tilemap(String tilemapFile) {		
		numRows = -1;
		numCols = -1;
		int numTiles = -1;
		String tileDir = null;
		String suffix = null;
		
		int currentRow = 0;
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream(tilemapFile)))) {
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] split = line.split("\\s+");
				if (split.length < 7) {
					Logger.panic("Malformed tilemap header");
				}
				if (split[0].equalsIgnoreCase(TILEMAP_MAGIC_WORD)) {
					numRows = Integer.parseInt(split[1]);
					numCols = Integer.parseInt(split[2]);
					tileDir = split[3];
					numTiles = Integer.parseInt(split[4]);
					int tileWidth = Integer.parseInt(split[5]);
					suffix = split[6];
					
					tiles = new int[numRows * numCols];
					this.tileWidth = tileWidth;
				}
				else {
					for (int i = 0; i < numCols; i++) {
						tiles[currentRow * numCols + i] = Integer.parseInt(split[i]);
					}
					currentRow++;
				}
			}
		} catch (IOException e) {
			Logger.panic("Error reading tilemap file: " + e);
		}
		
		images = new Image[numTiles];
		for (int i = 0; i < numTiles; i++) {
			String imagePath = tileDir + "/" + i + suffix;
			images[i] = ResourceLoader.loadImage(imagePath);
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
}
