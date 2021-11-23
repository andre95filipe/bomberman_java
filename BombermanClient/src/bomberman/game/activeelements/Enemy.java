package bomberman.game.activeelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.level.Map;
import bomberman.game.logic.Game;

public class Enemy extends ActiveElement {
	
	private Random random = new Random();
	private int speed = 0;
	private String direction = null;
	
	/**
	 * Contains the directions and respetive sprite.
	 */
	
	private HashMap <String, ArrayList <Sprite>> dir = new HashMap <String, ArrayList <Sprite>> () {
		private static final long serialVersionUID = 1L;
	{
		put(null, Sprite.enemyLeft);
		put("left", Sprite.enemyLeft);
		put("right", Sprite.enemyRight);
		put("up", Sprite.enemyLeft);
		put("down", Sprite.enemyRight);
	}};
	
	/**
	 * Constructor used for instantiating an enemy in a new game.
	 * @param map Belongs only to one map.
	 * @param n The enemy identifier, used the respective enemy from the enemies list in the corresponding Map.
	 */
	
	public Enemy(Map map, int n) {
		this.animation = 0;
		this.map = map;
		this.x = map.getCoordinates('e').get(n)[0] * 16;
		this.y = map.getCoordinates('e').get(n)[1] * 16;
		this.sprite = Sprite.enemyLeft.get(0);
		map.addEnemy(this);
	}
	
	/**
	 * COnstructur used to instantiate an enemy from saved game.
	 * @param map Belongs only to one Map.
	 * @param enemies Formating string with enemy state. Information is split by commas.
	 */
	
	public Enemy(Map map, String enemies) {
		this.map = map;
		this.sprite = Sprite.enemyLeft.get(0);
		String[] aux = enemies.split(",");
		this.x = Integer.valueOf(aux[0]);
		this.y = Integer.valueOf(aux[1]);
		this.newX = Integer.valueOf(aux[2]);
		this.newY = Integer.valueOf(aux[3]);
		this.dirX = Integer.valueOf(aux[4]);
		this.dirY = Integer.valueOf(aux[5]);
		this.moving = Boolean.valueOf(aux[6]);
		this.die = Boolean.valueOf(aux[7]);
		this.aux = Integer.valueOf(aux[8]);
		this.currTime = Long.valueOf(aux[9]);
		this.initTime = System.currentTimeMillis() - currTime;
		map.addEnemy(this);
	}
	
	/**
	 * Method used to update the enemy state. Here is where the enemy takes decisions like moving and changing it's sprite.
	 */
	
	@Override
	public void update() {
		int[] coords = new int[2];
		animation = (animation < 60) ? animation++ : 0;
		
		if(!moving) {	
			coords = generateMove();
			newX = (16 * coords[0]) + x;
			newY = (16 * coords[1]) + y;
			
			dirX = coords[0];
			dirY = coords[1];
			moving = true;
		}
		else {
			if(!((Player)map.getElements().get("player").get(0)).isDead() && !die)
				move(dirX, dirY);
		}
		if(moving && !die) {
			if(animation % 20 == 0) {
				System.out.println();
				sprite = dir.get(direction).get(aux);
				aux++;
				if(aux == 3) aux = 0;
			}
		}
		if(alive && die) {
			if(aux == 0) {
				sprite = Sprite.enemyDead.get(aux);
				initTime = System.currentTimeMillis();
				aux++;
			}
			else {
				currTime = System.currentTimeMillis() - initTime;
				if(currTime > 250) {
					initTime = System.currentTimeMillis();
					if(aux < 3) {
						sprite = Sprite.enemyDead.get(aux);
						aux++;
					}
					else {
						if(alive)
							((Player)map.getElements().get("player").get(0)).scoreUp();
						alive = false;
					}
				}
			}
		}
		
		if(elementCollision(dirX, dirY, map.getElements().get("player").get(0)))
			((Player)map.getElements().get("player").get(0)).die();
	}
	
	/**
	 * Method used to generate a random movement.
	 * @return Generated coordinates.
	 */
	
	public int[] generateMove() {
		int[] coords = new int[2];
		int d;
		do {
			coords[0] = coords[1] = 0;
			d = random.nextInt(4);
			if(d == 0) coords[1]--;
			if(d == 1) coords[1]++;
			if(d == 2) coords[0]--;
			if(d == 3) coords[0]++;
		} while(wallCollision(coords[0], coords[1]));
		return coords;
	}
	
	/**
	 * Method used to move enemy.
	 * @param xa absolute X coordinate
	 * @param ya absolute Y coordinate
	 */
	
	public void move(int xa, int ya) {
		if(xa > 0) direction = "right"; 
		if(xa < 0) direction = "left"; 
		if(ya > 0) direction = "down"; 
		if(ya < 0) direction = "up"; 
		if(speed / 2 == 1) {
			speed = 0;
			if(x != newX || y != newY) {
				x += xa;
				y += ya;
			}
			else moving = false;
		}
		else speed++;
	}
	
	/**
	 * Render enemy in the given screen.
	 */

	@Override
	public void render(Screen screen) {
		screen.render(x , y, sprite, false, 0);
	}
	
	/**
	 * Method used to kill enemy. Enemy only dies once. Removes it from enemies list in the corresponding Map.
	 */
	
	public void die() {
		if(!die) {
			aux = 0;
			map.getElements().get("enemies").remove(this);
			die = true;
		}
	}
}
