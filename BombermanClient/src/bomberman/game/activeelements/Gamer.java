package bomberman.game.activeelements;

import java.util.ArrayList;
import java.util.HashMap;

import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.input.Keyboard;
import bomberman.game.level.Map;
import bomberman.game.logic.Game;
import bomberman.game.logic.GameMultiplayer;
import bomberman.main.Main;
import bomberman.sound.Sound;


/**
 * 
 * Class used to instantiate a gamer only in a multiplayer game.
 * @author andre
 *
 */
public class Gamer extends ActiveElement {
	
	private Keyboard input;
	private Game game;
	
	private int speed = 1, bombRange = 1, life = 3, score = 0, color;
	private boolean dropedBomb = false, exit = false;
	private long initBomb = 0, initRange = 0, currTimeRange = 0, initSpeed = 0, currTimeSpeed = 0;
	private int xa = 0, ya = 0;
	private String username;
	
	
	/**
	 * List of Sprites by a given diren direction.
	 */
	private HashMap <String, ArrayList <Sprite>> dir = new HashMap <String, ArrayList <Sprite>> () {
		private static final long serialVersionUID = 1L;
	{
		put(null, Sprite.playerDown);
		put("up", Sprite.playerUp);
		put("down", Sprite.playerDown);
		put("left", Sprite.playerLeft);
		put("right", Sprite.playerRight);
	}};
	
	/**
	 * Constructor to instantiate a new gamer.
	 * @param gMulti Corresponding game.
	 * @param map Corresponding map.
	 * @param username Username of the respective gamer.
	 * @param color Which color belongs to the gamer
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	
	public Gamer(GameMultiplayer gMulti, Map map, String username, int color, int x, int y) {
		this.gameMulti = gMulti;
		this.map = map;
		this.username = username;
		this.color = color;
		this.x = x;
		this.y = y;
		this.sprite = dir.get(direction).get(0);
	}
	
	/**
	 * Method to update the gamer state, like the position and the corresponding movement sprite.
	 */
	
	@Override
	public void update() {
		if(animation < 9000) animation++;
		else animation = 0;
		if(xa != 0 || ya != 0) {
			if(alive && !die) {
				if(!moving) {
					Sound.move.play();
					newX = (16 * xa) + x;
					newY = (16 * ya) + y;
					dirX = xa;
					dirY = ya;
					moving = true;
				}
				else 
					move(dirX, dirY);
			}
		}
		else {
			xa = ya = 0;
			moving = false;
		}
		
		if(alive && !die) sprite = dir.get(direction).get(0);
		if(moving && !die && alive) {
			sprite = (animation % 17 < 10) ? dir.get(direction).get(1) : dir.get(direction).get(2);
		}
		
		if(alive && die) {
			if(aux == 0) {
				sprite = Sprite.playerDead.get(aux);
				initTime = System.currentTimeMillis();
				aux++;
			}
			else {
				if(System.currentTimeMillis() - initTime > 300) {
					initTime = System.currentTimeMillis();
					if(aux < 3) {
						sprite = Sprite.playerDead.get(aux);
						aux++;
					}
					else {
						alive = false;
						die = false;
					}
				}
			}
		}
	}
	
	/**
	 * Method to render the gamer in the given screen.
	 */
	
	@Override
	public void render(Screen screen) {
		screen.render(x , y, sprite, false, color);
	}
	
	/**
	 * Method to move the gamer if there is no collisions.
	 * @param xa  Absolute X coordinates.
	 * @param ya Absolute Y coordinates.
	 */
	
	public void move(int xa, int ya) {
		if(xa > 0) direction = "right"; 
		if(xa < 0) direction = "left"; 
		if(ya > 0) direction = "down"; 
		if(ya < 0) direction = "up"; 
		
		if(!wallCollision(xa, ya)) {
			if(x != newX || y != newY) {
				x += (xa * speed);
				y += (ya * speed);
			}
			else {
				this.xa = this.ya = 0;
				Main.client.sendData("arrive" + x + "," + y);
			}
		}
		else {
			this.xa = this.ya = 0;
			moving = false;
		}
	}
	
	/**
	 * 
	 * Method used to kill gamer. Can only die once.
	 */
	
	public void die() {
		if(!die) {
			Sound.dead.play();
			aux = 0;
			die = true;
		}
	}
	
	/**
	 * Set gamer absolute direction.
	 * @param xa absolute X coordinate.
	 * @param ya absolute Y coordinate.
	 */
	
	public void setDirection(int xa, int ya) {
		this.xa = xa;
		this.ya = ya;
	}
	
	/**
	 * 
	 * @return Bomb range.
	 */

	public int getRange() {
		return this.bombRange;
	}
	
	/**
	 * 
	 * @return Gamer username.
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * 
	 * @return Gamer speed.
	 */
	public int getSpeed() {
		return this.speed;
	}
	
	/**
	 * 
	 * @return If gamer is dying.
	 */
	
	public boolean isDead() {
		return this.die;
	}
	
	/**
	 * @return If gamer is alive.
	 */
	public boolean isAlive() {
		return this.alive;
	}
	
	/**
	 * 
	 * @param map Set gamers map.
	 */
	
	public void setMap(Map map) {
		this.map = map;
	}
	/**
	 * 
	 * @param b Set gamer alive or not.
	 */
	public void setAlive(boolean b) {
		this.alive = b;
	}
	
	/**
	 * 
	 * @return Remaing speed powerup time.
	 */
	
	public long getCurrTimeSpeed() {
		return this.currTimeSpeed;
	}
	
	/**
	 * 
	 * @return Remaining bomb range powerup time.
	 */
	
	public long getCurrTimeRange() {
		return this.currTimeRange;
	}
	
	/**
	 * 
	 * @param moving Sets gamer moving or not.
	 */
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	/**
	 * Sets gamer coordinates.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	
	public void setCoord(int x, int y) {
		this.newX = this.x = x;
		this.newY = this.y = y;
		this.dirX = this.dirY = this.xa = this.ya = 0;
		moving = false;
	}
}