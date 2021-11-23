package bomberman.game.tile;

import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.level.Map;
import bomberman.game.logic.Game;

/**
 * Class to represent destroyable tiles.
 * @author thebomberman
 *
 */
public class DestroyableTile extends Tile {
	private Map map;
	private Game game;
	private int x, y;
	private int tile = 0;
	private boolean destroy = false;
	private long initTime = 0;
	
	/**
	 * Constructor for adding new wall.
	 * @param map Map.
	 * @param game Game.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public DestroyableTile(Map map, Game game, int x, int y) {
		super(Sprite.destroyableWall.get(0));
		initialize(map, game, x, y);
	}
	
	/**
	 * Constructor for adding a wall from a saved game.
	 * @param map Map.
	 * @param game Game.
	 * @param wall Formated String with coordinates.
	 */
	public DestroyableTile(Map map, Game game, String wall) {
		super(Sprite.destroyableWall.get(0));
		String[] aux = wall.split(",");
		this.tile = Integer.valueOf(aux[2]);
		this.destroy = Boolean.valueOf(aux[3]);
		initialize(map, game, Integer.valueOf(aux[0]), Integer.valueOf(aux[1]));
	}
	
	/**
	 * Initialize the wall.
	 * @param map Map.
	 * @param game Game.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	private void initialize(Map map, Game game, int x, int y) {
		this.map = map;
		this.game = game;
		this.x = x;
		this.y = y;
		solid = true;
		breakable = true;
	}
	
	/**
	 * 
	 * @param screen Screen to render wall.
	 */
	public void render(Screen screen) {
		screen.render(x, y, sprite, true, 0);
	}
	/**
	 * Method to destroy wall and give it an animation.
	 * @param range Range of the bomb explosion.
	 */
	public void destroy(int range) {
		if(System.currentTimeMillis() - initTime > 200 / range  && !game.isPaused()) {
			initTime = System.currentTimeMillis();
			destroy = true;
			if(tile == 3)
				map.getWalls().remove(this);
			else {
				tile++;
				sprite = Sprite.destroyableWall.get(tile);
			}
		}
	}
	/**
	 * Update the bomb state.
	 */
	public void update() {
		if(destroy && (System.currentTimeMillis() - initTime > 200)) {
			tile++;
			initTime = System.currentTimeMillis();
		}
	}
	/**
	 * 
	 * @return X coordinate.
	 */
	public int getX() {
		return x;
	}
	/**
	 * 
	 * @param x Set X coordinate.
	 */

	public void setX(int x) {
		this.x = x;
	}
/**
 * 
 * @return Y coordinate.
 */
	public int getY() {
		return y;
	}
	/**
	 * 
	 * @param y Set Y coordinate.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 
	 * @return This tile.
	 */
	
	public int getTile() {
		return this.tile;
	}
	
	/**
	 * 
	 * @return If has been destroyed.
	 */
	
	public boolean isDestroy() {
		return this.destroy;
	}

}
