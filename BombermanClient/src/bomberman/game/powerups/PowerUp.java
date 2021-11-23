package bomberman.game.powerups;


import bomberman.game.graphics.Sprite;
import bomberman.game.tile.Tile;


/**
 * Class to represent powerups.
 * @author thebomberman
 *
 */
public class PowerUp extends Tile {

	protected int gameTime = 0;
	protected int range = 0;
	protected int speed = 0;
	protected int life = 0;
	private int pTime = 10;
	
	/**
	 * 
	 * @param sprite Sprite given to this powerup.
	 */
	public PowerUp(Sprite sprite) {
		super(sprite);
	}
	
	/**
	 * 
	 * @return Powerup duration value.
	 */
	public int powerTime() {
		return pTime;
	}
	/**
	 * 
	 * @return Game time powerup value.
	 */
	public int getGameTime() {
		return this.gameTime;
	}
	/**
	 * 
	 * @return Bomb range powerup value.
	 */
	public int getRange() {
		return this.range;
	}
	/**
	 * 
	 * @return Speed increase powerup value.
	 */
	public int getSpeed() {
		return this.speed;
	}
	/**
	 * 
	 * @return Add life powerup value.
	 */
	public int getLife() {
		return this.life;
	}
	/**
	 * 
	 * @return Powerup duration value.
	 */
	public int getPowerTime() {
		return pTime;
	}

}
