package bomberman.game.tile;


import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.powerups.PowerUp;


/**
 * Class to represent tiles. Tiles are instantiated in static way inside class, avoiding having to load sprites every use.
 * @author thebomberman
 *
 */
public class Tile {
	
	protected Sprite sprite;
	protected boolean solid = false;
	protected boolean breakable = false;
	
	public static Tile voidTile = new Tile(Sprite.voidSprite);
	public static Tile grass = new Tile(Sprite.grass);
	public static Tile fixedWall = new Tile(Sprite.fixedWall) {{ solid = true; }};
	public static Tile exit = new Tile(Sprite.exit);
	
	public static Tile clockUp = new PowerUp(Sprite.powerup.get(0)) {{ gameTime = 10; }};
	public static Tile bombRange = new PowerUp(Sprite.powerup.get(1)) {{ range = 1; }};
	public static Tile speedUp = new PowerUp(Sprite.powerup.get(2)) {{ speed = 1; }};
	public static Tile lifeUp = new PowerUp(Sprite.powerup.get(3)) {{ life = 1; }};

	/**
	 * 
	 * @param sprite Gives a sprite to this tile.
	 */
	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	/**
	 * Render tile.
	 * @param x X coordinate to render tile.
	 * @param y Y coordinate to render tile.
	 * @param screen Screen to render tile.
	 */
	public void render(int x, int y, Screen screen) {
		screen.render(x * 16, y * 16, this.sprite, true, 0);
	}
	/**
	 * 
	 * @return If tile is solid.
	 */
	public boolean isSolid() {
		return solid;
	}
	/**
	 * 
	 * @return If tile is breakable.
	 */
	public boolean isBreakable() {
		return breakable;
	}
	/**
	 * 
	 * @return Tile sprite.
	 */
	public Sprite getSprite() {
		return this.sprite;
	}

}
