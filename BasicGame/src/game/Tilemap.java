package game;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class Tilemap {
	private static final String TILEMAP_MAGIC_WORD = "tilemap";
	
	private int[] tiles;
	private int numRows;
	private int numCols;
	private int tileWidth;
	private Image[] images;
	
	public Tilemap(Path p, Path tileFolder, int numTiles, String suffix, int tileWidth) {
		this.tileWidth = tileWidth;
		
		images = new Image[numTiles];
		for (int i = 0; i < numTiles; i++) {
			Path imagePath = tileFolder.resolve(i + suffix);
			try {
				images[i] = ImageIO.read(imagePath.toFile());
			} catch (IOException e) {
				System.out.println("Error loading image for tile " + i + ": " + e);
			}
		}
		
		numRows = -1;
		numCols = -1;
		int currentRow = 0;
		try (BufferedReader reader = Files.newBufferedReader(p, Charset.forName("US-ASCII"))) {
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] split = line.split("\\s+");
				if (split[0].equalsIgnoreCase(TILEMAP_MAGIC_WORD)) {
					numRows = Integer.parseInt(split[1]);
					numCols = Integer.parseInt(split[2]);
					tiles = new int[numRows * numCols];
				}
				else {
					for (int i = 0; i < numCols; i++) {
						tiles[currentRow * numCols + i] = Integer.parseInt(split[i]);
					}
					currentRow++;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading tilemap file: " + e);
		}
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				int tile = tiles[j * numCols + i];
				g.drawImage(images[tile], i * tileWidth, j * tileWidth, null);
			}
		}
	}
}
