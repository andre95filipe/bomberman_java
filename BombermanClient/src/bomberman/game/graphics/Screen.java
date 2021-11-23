package bomberman.game.graphics;

import bomberman.game.logic.Game;
import bomberman.game.logic.GameMultiplayer;

/**
 * Class to represent the screen.
 * @author thebomberman
 *
 */
public class Screen {
	
	private int width, height;
	private int[] pixels;
	private int xOffset, yOffset;
	private Game game;
	private GameMultiplayer gameMulti;

	/**
	 * Constructor to create new Screen for singleplayer games.
	 * @param game Corresponding game.
	 * @param width Screen width.
	 * @param height Screen height.
	 */
	public Screen(Game game, int width, int height) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}
	/**
	 * Constructor to create new Screen for multiplayer games.
	 * @param gameMulti Corresponding game.
	 * @param width Screen width.
	 * @param height Screen height.
	 */
	public Screen(GameMultiplayer gameMulti, int width, int height) {
		this.gameMulti = gameMulti;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	/**
	 * Clear screen.
	 */
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	
	/**
	 * Render a given element
	 * @param xp X position
	 * @param yp Y position
	 * @param sprite Sprite to render
	 * @param background If want to add grass to the element background
	 * @param colorChange Change element color. Used to render the different gamers in the multiplayer game.
	 */
	public void render(int xp, int yp, Sprite sprite, boolean background, int colorChange) {
		xp -= xOffset;
		yp -= yOffset;
		for(int y = 0; y < sprite.size; y++) {
			int ya = y + yp;
			for(int x = 0; x < sprite.size; x++) {
				int xa = x + xp;
				if(xa < - sprite.size || xa >= width || ya < 0 || ya >= height) 
					break;
				if(xa < 0)
					xa = 0;
				if(sprite.pixels[x + y * sprite.size] != 0xFFFF00FF)
					pixels[xa + ya * width] = sprite.pixels[x + y * sprite.size];
				if(sprite.pixels[x + y * sprite.size] == 0xFFFF00FF && background)
					pixels[xa + ya * width] = Sprite.grass.getPixel(x + y * game.getTileSize());
				if(sprite.pixels[x + y * sprite.size] == 0xFF3CBCFF && colorChange != 0)
					switch(colorChange) {
						case 1:
							pixels[xa + ya * width] = 0xFF3CBCFF; // blue
							break;
						case 2:
							pixels[xa + ya * width] = 0xFFB200FF;  // purple
							break;
						case 3:
							pixels[xa + ya * width] = 0xFF000000;  // black
							break;
						case 4:
							pixels[xa + ya * width] = 0xFFFFD800;  // yellow
							break;
					}
			}	
		}
	} 
	
	/**
	 * Sets offset in the screen so the main player can be the main focus.
	 * @param xOffset X offset
	 * @param yOffset Y offset
	 */
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	/**
	 * 
	 * @return Screen width
	 */
	public int getWidth() {
		return this.width;
	}
	/**
	 * 
	 * @return Screen height
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * 
	 * @return Screen pixels
	 */
	public int[] getPixels() {
		return pixels;
	}

}
