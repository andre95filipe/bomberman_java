package bomberman.multiplayer;
/**
 * Class used to instantiate bombs whenever the player/gamer drops. Inherits from ActiveElement
 * @author thebomberman
 *
 */
public class Bomb extends ActiveElement {

	private Game game;
	private long timeLeft;
	private long initTime;
	private int duration = 3;
	private int range;
	/**
	 * Constructor used for whenever a player/gamer drops a bomb.
	 * @param game Bomb belongs to this game.
	 * @param player To get player coordinates and place the bomb.
	 * @param map  Bomb belongs only to this map.
	 */
	public Bomb(Game game, Player player, Maze map) {
		this.game = game;
		this.map = map;
		this.x = player.getX();
		this.y = player.getY();
		this.range = player.getRange();
		this.initTime = System.currentTimeMillis();
		this.animation = 0;
	}
	/**
	 * Method used to update the bomb state by check it's duration. Removes after reaching limit time.
	 */
	public void update() {		
		timeLeft = System.currentTimeMillis() - initTime;
		if(timeLeft > duration * 1000) {
			remove();
			game.getBombs().remove(this);
			game.getFlames().add(new Flames(this, game, map));
		}
	}
	/**
	 * 
	 * @return Bomb flame range
	 */
	
	public int getRange() {
		return this.range;
	}
	/**
	 * 
	 * @return Time since bomb has been dropped
	 */
	public long getTimeLeft() {
		return timeLeft;
	}
}
