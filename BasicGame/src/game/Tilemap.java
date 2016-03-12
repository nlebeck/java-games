package game;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Tilemap {
	private static final String TILEMAP_MAGIC_WORD = "tilemap";
	
	private int[] tiles;
	private int numRows;
	private int numCols;
	private int tileWidth;
	
	public Tilemap(Path p) {
		tileWidth = 40;
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
			System.err.println("Error reading tilemap file");
		}
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				int tile = tiles[j * numCols + i];
				if (tile == 0) {
					g.setColor(Color.GREEN);
				}
				else if (tile == 1) {
					g.setColor(Color.BLUE);
				}
				g.fillRect(i * tileWidth, j * tileWidth, tileWidth, tileWidth);
			}
		}
	}
}
