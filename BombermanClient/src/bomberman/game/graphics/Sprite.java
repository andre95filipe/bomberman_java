package bomberman.game.graphics;

import java.util.ArrayList;


/**
 * Class to represent each sprite. Creates static sprites so it only loads once.
 * @author andre
 *
 */
public class Sprite {

	public final int size;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;

	public static Sprite voidSprite = new Sprite(16, 0x6938a4); 
	
	public static ArrayList <Sprite> powerup = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 8, 10, SpriteSheet.tiles));	// clock up
		add(new Sprite(16, 1, 10, SpriteSheet.tiles));  // bomb range
		add(new Sprite(16, 3, 10, SpriteSheet.tiles));	// speed up
		add(new Sprite(16, 4, 10, SpriteSheet.tiles));	// life up
	}};
	
	public static ArrayList <Sprite> playerSelect = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 0, 12, SpriteSheet.tiles));
		add(new Sprite(16, 1, 12, SpriteSheet.tiles));
		add(new Sprite(16, 2, 12, SpriteSheet.tiles));
		add(new Sprite(16, 3, 12, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> playerDown = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 2, 0, SpriteSheet.tiles));
		add(new Sprite(16, 2, 1, SpriteSheet.tiles));
		add(new Sprite(16, 2, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> playerUp = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 0, 0, SpriteSheet.tiles));
		add(new Sprite(16, 0, 1, SpriteSheet.tiles));
		add(new Sprite(16, 0, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> playerLeft = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 3, 0, SpriteSheet.tiles));
		add(new Sprite(16, 3, 1, SpriteSheet.tiles));
		add(new Sprite(16, 3, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> playerRight = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 1, 0, SpriteSheet.tiles));
		add(new Sprite(16, 1, 1, SpriteSheet.tiles));
		add(new Sprite(16, 1, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> playerDead = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 4, 2, SpriteSheet.tiles));
		add(new Sprite(16, 5, 2, SpriteSheet.tiles));
		add(new Sprite(16, 6, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> enemyLeft = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 9, 0, SpriteSheet.tiles));
		add(new Sprite(16, 9, 1, SpriteSheet.tiles));
		add(new Sprite(16, 9, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> enemyRight = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 10, 0, SpriteSheet.tiles));
		add(new Sprite(16, 10, 1, SpriteSheet.tiles));
		add(new Sprite(16, 10, 2, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> enemyDead = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 15, 0, SpriteSheet.tiles));
		add(new Sprite(16, 15, 1, SpriteSheet.tiles));
		add(new Sprite(16, 15, 2, SpriteSheet.tiles));
	}};
	
	public static Sprite grass = new Sprite(16, 6, 0, SpriteSheet.tiles);
	public static Sprite fixedWall = new Sprite(16, 5, 0, SpriteSheet.tiles);
	public static Sprite exit = new Sprite(16, 4, 0, SpriteSheet.tiles);
	
	public static ArrayList <Sprite> destroyableWall = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 7, 0, SpriteSheet.tiles));
		add(new Sprite(16, 7, 1, SpriteSheet.tiles));
		add(new Sprite(16, 7, 2, SpriteSheet.tiles));
		add(new Sprite(16, 7, 3, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> bomb = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 0, 3, SpriteSheet.tiles));
		add(new Sprite(16, 1, 3, SpriteSheet.tiles));
		add(new Sprite(16, 2, 3, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionCenter = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 0, 4, SpriteSheet.tiles));
		add(new Sprite(16, 0, 5, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionRight = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 2, 7, SpriteSheet.tiles));
		add(new Sprite(16, 2, 8, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionLeft = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 0, 7, SpriteSheet.tiles));
		add(new Sprite(16, 0, 8, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionUp = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 1, 4, SpriteSheet.tiles));
		add(new Sprite(16, 2, 4, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionDown = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 1, 6, SpriteSheet.tiles));
		add(new Sprite(16, 2, 6, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionHorizontal = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 1, 7, SpriteSheet.tiles));
		add(new Sprite(16, 1, 8, SpriteSheet.tiles));
	}};
	
	public static ArrayList <Sprite> explosionVertical = new ArrayList <Sprite>() {
		private static final long serialVersionUID = 1L;
	{
		add(new Sprite(16, 1, 5, SpriteSheet.tiles));
		add(new Sprite(16, 2, 5, SpriteSheet.tiles));
	}};
	
	/**
	 * Create new sprite.
	 * @param size Sprite size. Used is 16 pixels.
	 * @param x X coordinate in the sprite sheet
	 * @param y Y coordinate in the sprite sheet.
	 * @param sheet the corresponding sprite sheet.
	 */
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.size = size;
		pixels = new int[this.size * this.size];
		this.x = x * this.size;
		this.y = y * this.size;
		this.sheet = sheet;
		load();
	}
	
	/**
	 * Loads sprite pixels from the sprite sheet.
	 */

	private void load() {
		for(int y = 0; y < size; y++) {
			for(int x = 0; x < size; x++) {
				pixels[x + y * size] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.size];
			}
		}
	}
	
	/**
	 * Creates an unicolor sprite
	 * @param size Sprite size
	 * @param color Sprite color
	 */

	public Sprite(int size, int color) {
		this.size = size;
		pixels = new int[size * size];
		setColor(color);
	}
	
	/**
	 * Set sprite color
	 * @param color Sprite color
	 */
	private void setColor(int color) {
		for(int i = 0; i < size * size; i++) {
			pixels[i] = color;
		}
	}
	
	/**
	 * 
	 * @param i Returns pixel
	 * @return Sprite pixel
	 */
	public int getPixel(int i) {
		return this.pixels[i];
	}
	
}
