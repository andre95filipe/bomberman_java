package bomberman.game.activeelements;

import bomberman.game.elements.GameElement;
import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;


/**
 * The class is used to generalize the active elements in the game.
 * 
 * @author thebomberman
 *
 */
public abstract class ActiveElement extends GameElement {

	protected Sprite sprite;
	protected String direction = null;
	protected boolean moving = false, alive = true, die = false;
	protected long initTime, currTime = 0;
	protected int newX = 0, newY = 0, movement = 0, animation = 0, dirX = 0, dirY = 0, aux = 0;
	
	/**
	 * The method is used for rendering the active element in the screen. Must be implemented by subclasses.
	 */
	public abstract void render(Screen screen);
	
	/**
	 * The method is used to check if the active element is colliding with a wall.
	 * @param xa Absolute value of X coordinate by generated movement
	 * @param ya Absolute value of Y coordinate by generated movement
	 * @return If active element is colliding with a wall
	 */
	
	protected boolean wallCollision(int xa, int ya) {
		for(int i = 0; i < 4; i++) {
			int xt = ((x + xa) + i % 2 * 15) / 16;
			int yt = ((y + ya) + i / 2 * 15) / 16;
			if(map.getTile(xt, yt).isSolid() || map.isWall(xt * 16, yt * 16))
				return true;
		}
		return false;
	}
	
	/**
	 * The method is used to check if active element is colliding with any other game element.
	 * @param xa Absolute value of X coordinate by generated movement
	 * @param ya Absolute value of Y coordinate by generated movement
	 * @param element Element to check collision with
	 * @return If active element is colliding with the given game element
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
	
	/**
	 * 
	 * @return New X coordinate by generated movement.
	 */
	
	public int getNewX() {
		return this.newX;
	}
	
	/**
	 * 
	 * @return New Y coordinate by generated movement.
	 */
	
	public int getNewY() {
		return this.newY;
	}
	
	/**
	 * 
	 * @return If the active element is alive.
	 */
	
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 *
	 * @return If active element is dying.
	 */
	
	public boolean isDie() {
		return this.die;
	}
	
	/**
	 * 
	 * 
	 * @return If active element is moving.
	 */
	
	public boolean isMoving() {
		return this.moving;
	}
	
	/**
	 * 
	 * @return Absolute direction in X coordinate by generated movement.
	 */
	
	public int getDirX() {
		return this.dirX;
	}
	
	/**
	 * 
	 * @return Absolute direction in Y coordinate by generated movement.
	 */
	
	public int getDirY() {
		return this.dirY;
	}
	
	/**
	 * 
	 * @return Current sprite used to print active element in the screen.
	 */
	
	public int getCurrSprite() {
		return this.aux;
	}
	
	/**
	 * 
	 * @return Variable that contains a time which is used for active element animation.
	 */
	
	public long getCurrTime() {
		return this.currTime;
	}

}
