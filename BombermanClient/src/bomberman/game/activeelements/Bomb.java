package bomberman.game.activeelements;

import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.level.Map;
import bomberman.game.logic.GameMultiplayer;
import bomberman.sound.Sound;

/**
 * Class used to instantiate bombs whenever the player/gamer drops. Inherits from ActiveElement
 * @author thebomberman
 *
 */

public class Bomb extends ActiveElement {

	private long timeLeft;
	private long initTime;
	private int duration = 3;
	private int range;
	private boolean multiplayer = false;
	private GameMultiplayer gameMulti;
	
	/**
	 * Constructor used for whenever a player/gamer drops a bomb.
	 * @param player To get player coordinates and place the bomb.
	 * @param map  Bomb belongs only to this map.
	 */
	
	public Bomb(Player player, Map map) {
		Sound.dropBomb.play();
		this.map = map;
		this.x = player.getX();
		this.y = player.getY();
		this.range = player.getRange();
		this.initTime = System.currentTimeMillis();
		this.animation = 0;
	}
	
	/**
	 * This constructor is used for loading games from database. 
	 * This loads a bomb that was saved in a game with important specific characteristics, like remaining time.
	 * @param map Bombs belongs only to this map.
	 * @param bombs Formated String that contains the state of the bomb. The parameters are split by commas.
	 */
	
	public Bomb(Map map, String bombs) {
		this.map = map;
		String[] aux = bombs.split(",");
		this.x = Integer.valueOf(aux[0]);
		this.y = Integer.valueOf(aux[1]);
		this.range = Integer.valueOf(aux[2]);
		this.timeLeft = Long.valueOf(aux[3]);
		this.initTime = System.currentTimeMillis() - timeLeft;
		this.animation = 0;
	}
	
	/**
	 * This constructor is used for multiplayer games.
	 * Instead of the bomb being instantiated in the player position. It is instantiated by the given coordinates,
	 * @param map Belongs only to this map.
	 * @param gameMulti Belongs to this game only.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	
	public Bomb(Map map, GameMultiplayer gameMulti, int x, int y) {
		Sound.dropBomb.play();
		this.map = map;
		this.x = x;
		this.y = y;
		this.multiplayer = true;
		this.animation = 0;
		this.gameMulti = gameMulti;
	}
	
	/**
	 * Method used to update the bomb state by changing the variable that decides which sprite is going to be rendered.
	 */

	public void update() {		
		if(animation <= 60) animation++;
		else animation = 0;
	}
	
	/**
	 * Method renders bomb to the screen. It renders for the previously defined duration time (in milliseconds) 
	 * only if it's not an multiplayer game, which state is decided by given instructions.
	 * @param screen The screen the bomb is going to be renderes
	 */

	@Override
	public void render(Screen screen) {
		timeLeft = System.currentTimeMillis() - initTime;
		
		if(timeLeft > duration * 1000 && !multiplayer)
			remove();
		
		sprite = Sprite.bomb.get(0);
		if(animation < 20) 
			sprite = Sprite.bomb.get(0);
		if(animation < 40 && animation >= 20)
			sprite = Sprite.bomb.get(1);
		if(animation < 60 && animation >= 40) 
			sprite = Sprite.bomb.get(2);
		screen.render(x , y, sprite, false, 0);
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
	
	/**
	 * Explodes the bomb, by setting the remove variable to true and removing it from the list of bombs present in the main game.
	 */
	
	public void explode() {
		remove();
		gameMulti.getBombs().remove(this);
	}
}
