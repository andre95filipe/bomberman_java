package bomberman.multiplayer;


/**
 * Class to represent active elements in the game. They contains action methods.
 * @author andre
 *
 */
public abstract class ActiveElement extends GameElement {
	protected String direction = null;
	protected boolean moving = false, alive = true, die = false;
	protected long initTime, currTime = 0;
	protected int newX = 0, newY = 0, movement = 0, animation = 0, dirX = 0, dirY = 0, aux = 0;
	
	/**
	 * Method to check collision with wall
	 * @param newX X coordinate to move to.
	 * @param newY Y coordinate to move to.
	 * @return If collision with wall detected.
	 */
	protected boolean wallCollision(int newX, int newY) {
		if(map.getTile(newX / 16, newY / 16) != ' ')
			return true;
		return false;
	}
	/**
	 * Method to check collision with other game elements
	 * @param xa Absolute X coordinate.
	 * @param ya Absolute Y coordinate.
	 * @param element Game element to check collision with.
	 * @return If collision detected.
	 */
	protected boolean elementCollision(int xa, int ya, GameElement element) {	
		for(int i = 2; i < 14; i++) {
			for(int j = 2; j < 14; j++) {
				for(int k = 2; k < 14; k++) {
					if(element.getX() + k > (x + xa) + 2 && element.getX() + k < (x + xa) + 14 && element.getY() + j == (y + ya) + i) return true;
				}
			}
		}
		return false;	
	}
	
	/*
	 * New X coordinate to move to.
	 */
	public int getNewX() {
		return this.newX;
	}
	/*
	 * New Y coordinate to move to.
	 */
	
	public int getNewY() {
		return this.newY;
	}
	
	/**
	 * 
	 * @return If is alive.
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Kill active element.
	 */
	public void die() {
		this.alive = false;
	}
	
	/**
	 * 
	 * @return If is dying.
	 */
	public boolean isDie() {
		return this.die;
	}
	
	/**
	 * 
	 * @return If is moving.
	 */
	public boolean isMoving() {
		return this.moving;
	}
	
	/**
	 * 
	 * @return Absolute X direction to move to.
	 */
	public int getDirX() {
		return this.dirX;
	}
	/**
	 * 
	 * @return Absolute Y direction to move to.
	 */
	public int getDirY() {
		return this.dirY;
	}

}
