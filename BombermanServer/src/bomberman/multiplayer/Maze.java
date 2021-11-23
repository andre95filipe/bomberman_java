package bomberman.multiplayer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Class that represents the game maze/ map.
 * @author andre
 *
 */
public class Maze {
	
	private Game game;
	
	private int width;
	private int height;
	private int level;
	private static char[] tiles;

	/**
	 * Creates new maze.
	 * @param game Corresponding game.
	 */
	public Maze(Game game) {
		this.game = game;
		loadLevel(readFile("res/map/multiplayer.lvl"));
	}
	/**
	 * Load multiplayer level from file.
	 * @param path Path where file is at.
	 * @return Formated String with maze.
	 */
	private String[] readFile(String path) {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			System.out.println("Error loading level file");
		}
		String[] line = new String[50];
		
	    try {
	    	int aux = 50;
	    	for(int i = 0; i <= aux; i++) {
	    		line[i] = file.readLine();
	    		if(i == 0)
	    			aux = getStringValues(line[i])[1];
	    	}
			file.close();
		} catch (IOException e) {
			System.out.println("Error reading level file");
		}
		return line;
	}
	
	/**
	 * Loads level given a formated String.
	 * @param game Formated String with maze info.
	 */
	private void loadLevel(String[] game) {
		level = getStringValues(game[0])[0];
		height = getStringValues(game[0])[1];
		width = getStringValues(game[0])[2];
		tiles = new char[height * width];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tiles[x + y * width] = game[y + 1].charAt(x);
			}
		}
		//generateElements('*', 13);
	}
	/**
	 * Method to obtain the size of the maze.
	 * @param line Line that contains size info.
	 * @return The sizes (width and height).
	 */
	private int[] getStringValues(String line) {
		int[] nums = {0, 0, 0};
		int j = 0;
		for(int i = 0; i < line.length(); i++) {
			if(Character.getNumericValue(line.charAt(i)) >= 0)
				nums[j] = nums[j] * 10 + Character.getNumericValue(line.charAt(i));
			else
				j++;
		}
		return nums;
	}
	/**
	 * 
	 * @return Maze tiles.
	 */
	public String getTiles() {
		String m = width + "/" + height + "/";
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				m += String.valueOf(tiles[x + y * width]);
			}
			m += "/";
		}
		return m;
	}
	
	/**
	 * 
	 * @param x X coordinate to check tile.
	 * @param y Y coordinate to check tile.
	 * @return Tile at given coordinates.
	 */
	public char getTile(int x, int y) {
		return tiles[x + y * width];
	}
	/**
	 * Change maze tile.
	 * @param x X coordinate to change tile.
	 * @param y Y coordinate to change tile.
	 * @param c Character of the tile to place.
	 */
	public void setTile(int x, int y, char c) {
		tiles[x + y * width] = c;
	}
	/**
	 * 
	 * @return Maze width.
	 */
	public int getWidth() {
		return this.width;
	}
	/**
	 * 
	 * @return Maze height.
	 */
	public int getHeight() {
		return this.height;
	}
	/**
	 * 
	 * @return Level.
	 */
	public int getLevel() {
		return this.level;
	}
	
}