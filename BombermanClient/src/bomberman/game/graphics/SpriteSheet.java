package bomberman.game.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class to represent sprite sheet. Sheet is only loaded once since it is created a static sprite sheet, avoiding to have to load every time we use it.
 * @author thebomberman
 *
 */
public class SpriteSheet {
	
	private String path;
	public final int size;
	public int[] pixels;
	
	public static SpriteSheet tiles = new SpriteSheet("res/textures/spritesheet.png", 256);
	
	
	/**
	 * Creates sprite sheet.
	 * @param path Where sprite sheet file is placed.
	 * @param size Sheet size.
	 */
	public SpriteSheet(String path, int size) {
		this.path = path;
		this.size = size;
		pixels = new int[this.size * this.size];
		load(this.path);
	}
	
	/**
	 * Load sprite sheet file.
	 * @param p Sheet path.
	 */
	
	private void load(String p) {
		try {
			BufferedImage image = ImageIO.read(new File(p));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w , h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
