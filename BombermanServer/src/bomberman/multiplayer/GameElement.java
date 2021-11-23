package bomberman.multiplayer;

/**
 * Class to represent game element.
 * @author andre
 *
 */
public abstract class GameElement {
	
	protected int x, y;
	private boolean removed = false;
	protected Maze map;
	
	/**
	 * Method to be implemented by subclasses.
	 */
	public abstract void update();

	/**
	 * 
	 * @return X coordinate.
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * 
	 * @return Y coordinate.
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Remove element.
	 */
	public void remove() { 
		removed = true;
	}
	
	/**
	 * 
	 * @return If element is removed.
	 */
	public boolean isRemoved() {
		return removed;
	}
	
}
