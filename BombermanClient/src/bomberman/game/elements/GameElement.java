package bomberman.game.elements;

import bomberman.game.graphics.Screen;
import bomberman.game.level.Map;
import bomberman.game.logic.GameMultiplayer;

/**
 * Class to generalize all the game elements.
 * @author thebomberman
 *
 */

public abstract class GameElement {
	
	protected int x, y;
	private boolean removed = false;
	protected Map map;
	protected GameMultiplayer gameMulti;
	
	
	/**
	 * Method to update game element. To be implemented by subclasses.
	 */
	public abstract void update();
	/**
	 * Method to render game element. To be be implemented by subclasses.
	 * @param screen Where to render element.
	 */
	public abstract void render(Screen screen);
	
	/**
	 * 
	 * @return Get X coordinate.
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * 
	 * @return Get Y coordinate.
	 */
	
	public int getY() {
		return this.y;
	}
	
	/**
	 * Set remove variable to true.
	 */
	
	public void remove() { 
		removed = true;
	}
	
	/**
	 * 
	 * @return If game element has been removed.
	 */
	
	public boolean isRemoved() {
		return removed;
	}
	
}
