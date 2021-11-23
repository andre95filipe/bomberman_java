package bomberman.game.activeelements;

import java.util.ArrayList;
import java.util.HashMap;

import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.input.Keyboard;
import bomberman.game.level.Map;
import bomberman.game.logic.Game;
import bomberman.game.logic.GameMultiplayer;
import bomberman.game.powerups.PowerUp;
import bomberman.game.tile.Tile;
import bomberman.sound.Sound;


/**
 * 
 * Class used to instantiate a player only in a singleplayer game.
 * @author thebomberman
 *
 */
public class Player extends ActiveElement {
	
	private Keyboard input;
	private Game game;
	
	private int speed = 1, bombRange = 1, life = 3, score = 0;
	private boolean dropedBomb = false, exit = false;
	private long initBomb = 0, initRange = 0, currTimeRange = 0, initSpeed = 0, currTimeSpeed = 0;
	
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
	 * Constructor to instantiate a player in a new game.
	 * @param map Corresponding map.
	 * @param game Corresponding game.
	 * @param input Commands are given by this object.
	 */
	public Player(Map map, Game game, Keyboard input) {
		this.map = map;
		this.game = game;
		this.x = map.getCoordinates('p').get(0)[0] * 16;
		this.y = map.getCoordinates('p').get(0)[1] * 16;
		this.input = input;
		this.sprite = dir.get(direction).get(0);
		map.addPlayer(this);
	}
	
	/**
	 * Constructor to instantiate a player from a saved game.
	 * @param map Corresponding map.
	 * @param game Corresponding game.
	 * @param input Object that commands player actions.
	 * @param player Formated String with saved player parameters. Split by commas.
	 * @param score Score from saved game.
	 */
	
	public Player(Map map, Game game, Keyboard input, String player, int score) {
		this.map = map;
		this.game = game;
		this.input = input;
		String[] aux = player.split(",");
		
		this.x = Integer.valueOf(aux[0]);
		this.y = Integer.valueOf(aux[1]);
		this.newX = Integer.valueOf(aux[2]);
		this.newY = Integer.valueOf(aux[3]);
		this.dirX = Integer.valueOf(aux[4]);
		this.dirY = Integer.valueOf(aux[5]);
		this.moving = Boolean.valueOf(aux[6]);
		this.alive = Boolean.valueOf(aux[7]);
		this.die = Boolean.valueOf(aux[8]);
		this.aux = Integer.valueOf(aux[9]);
		this.currTime = Long.valueOf(aux[10]);
		this.initTime = System.currentTimeMillis() - currTime;
		this.life = Integer.valueOf(aux[11]);
		this.speed = Integer.valueOf(aux[12]);
		this.currTimeSpeed = Long.valueOf(aux[13]);
		this.initSpeed = System.currentTimeMillis() - currTimeSpeed;
		this.bombRange = Integer.valueOf(aux[14]);
		this.currTimeRange = Long.valueOf(aux[15]);
		this.initRange = System.currentTimeMillis() - currTimeRange;
		this.sprite = dir.get(direction).get(0);
		this.score = score;

		map.addPlayer(this);
	}
	
	/**
	 * Method to update the gamer state, like the position and the corresponding movement sprite.
	 * This method makes player react to the given keyboard input.
	 */
	
	@Override
	public void update() {
		int xa = 0, ya = 0;
		
		if(animation < 9000) animation++;
		else animation = 0;

		if(input.isUp()) ya--;
		if(input.isDown()) ya++;
		if(input.isLeft()) xa--;
		if(input.isRight()) xa++;
		
		if(input.isDrop() && System.currentTimeMillis() - initBomb > 1700) {
			dropedBomb = true;
			initBomb = System.currentTimeMillis();
		}
		
		currTimeRange = System.currentTimeMillis() - initRange;
		if(currTimeRange >= ((PowerUp)Tile.bombRange).getPowerTime() * 1000) 
			bombRange = 1;
		currTimeSpeed = System.currentTimeMillis() - initSpeed;
		if(currTimeSpeed >= ((PowerUp)Tile.speedUp).getPowerTime() * 1000) 
			speed = 1;
		
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
		else
			 moving = false;
		
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
						life--;
					}
				}
			}
		}
	}
	
	/**
	 * Method to render player to the given screen. It is checked if player is over a powerup or exit.
	 */
	
	@Override
	public void render(Screen screen) {
		checkPowerUps();
		if(!game.isPaused())
			checkExit();
		screen.render(x , y, sprite, false, 0);
	}
	
	/**
	 * Method to check if player is at exit.
	 */
	
	private void checkExit() {
		if(map.getTile(x / 16, y / 16) == Tile.exit) {
			exit = true;
		}
	}
	
	/**
	 * Method to check if player is over a powerup.
	 */
		
	private void checkPowerUps() {
		if(map.getTile(x / 16, y / 16) instanceof PowerUp) {
			
			Sound.power.play();
			
			game.addTime(((PowerUp) map.getTile(x / 16, y / 16)).getGameTime());
			life += ((PowerUp) map.getTile(x / 16, y / 16)).getLife();
			if(bombRange < 2) {
				bombRange += ((PowerUp) map.getTile(x / 16, y / 16)).getRange();
				initRange = System.currentTimeMillis();
			}
			else 
				initRange += (((PowerUp)Tile.bombRange).getPowerTime() * 1000);
			if(speed < 2) {
				speed += ((PowerUp) map.getTile(x / 16, y / 16)).getSpeed();
				initSpeed = System.currentTimeMillis();
			}
			else 
				initSpeed += (((PowerUp)Tile.bombRange).getPowerTime() * 1000);
			map.setTile(x / 16, y / 16, ' ');
		}
	}
	
	/**
	 * Move the player according to the given absolute coordinates.
	 * @param xa absolute X coordinate.
	 * @param ya absolute Y coordinate.
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
			else
				moving = false;
		}
		else moving = false;
	}
	
	/**
	 * Method to kill player. Can only die once.
	 */
	
	public void die() {
		if(!die) {
			Sound.dead.play();
			aux = 0;
			die = true;
		}
	}
	/**
	 * 
	 * @return If player dropped bomb.
	 */

	public boolean dropedBomb() {
		return this.dropedBomb;
	}
	/**
	 * 
	 * @param b Sets dropped bomb or not.
	 */
	public void setDropedBomb(boolean b) {
		this.dropedBomb = b;
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
	 * @return How many lifes player has left.
	 */

	public int getLife() {
		return this.life;
	}
	
	
	/**
	 * Takes player one live.
	 */
	public void takeLife() {
		this.life--;
	}
	
	/**
	 * 
	 * @return Player speed.
	 */
	
	public int getSpeed() {
		return this.speed;
	}
	
	/**
	 * 
	 * @return If player is dying.
	 */
	
	public boolean isDead() {
		return this.die;
	}
	
	/**
	 * If player is alive.
	 */
	
	public boolean isAlive() {
		return this.alive;
	}
	
	/**
	 * 
	 * @param map Set players corresponding map.
	 */
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	/**
	 * 
	 * @param b Revives player.
	 */
	
	public void setAlive(boolean b) {
		this.alive = b;
	}
	
	/**
	 * 
	 * @return To check if player found exit.
	 */
	
	public boolean atExit() {
		return exit;
	}
	
	/**
	 * 
	 * @param b Player at exit.
	 */
	
	public void setExit(boolean b) {
		this.exit = b;
	}
	
	
	/**
	 * 
	 * @return Get player score.
	 */
	public int getScore() {
		return this.score;
	}
	/**
	 *  Add player score.
	 */
	public void scoreUp() {
		this.score += 100;
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
	 * @param moving Makes player move or not
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	
	/**
	 * Sets player coordinates at given coordinates.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public void setCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
