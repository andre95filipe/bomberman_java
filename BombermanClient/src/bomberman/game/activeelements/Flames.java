package bomberman.game.activeelements;

import java.util.ArrayList;

import bomberman.game.elements.GameElement;
import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.level.Map;
import bomberman.sound.Sound;

/**
 * This class is used to instantiate flames when the bomb explodes. This class is only used in singleplayer games.
 * @author thebomberman
 *
 */

public class Flames extends ActiveElement {
	
	private int range;
	private double duration = 1.8;
	private boolean flagUp = false, 
			flagDown = false, 
			flagLeft = false, 
			flagRight = false;
	
	/**
	 * Constructor for whenever a bomb explodes
	 * @param bomb Places flames around this bomb's coordinates.
	 * @param map Belongs only to one map.
	 */
	
	public Flames(Bomb bomb, Map map) {
		Sound.explosion.play();
		this.x = bomb.getX();
		this.y = bomb.getY();
		this.range = bomb.getRange();
		this.map = map;
		initTime = System.currentTimeMillis();
	}
	
	/**
	 * Constructor used for saved games.
	 * @param map Belongs to one map only.
	 * @param flames Formated String with saved parameters. Split by commas.
	 */
	
	public Flames(Map map, String flames) {
		this.map = map;
		String[] aux = flames.split(",");
		this.x = Integer.valueOf(aux[0]);
		this.y = Integer.valueOf(aux[1]);
		this.range = Integer.valueOf(aux[2]);
		this.flagUp = Boolean.valueOf(aux[3]);
		this.flagRight = Boolean.valueOf(aux[4]);
		this.flagDown = Boolean.valueOf(aux[5]);
		this.flagLeft = Boolean.valueOf(aux[6]);
	}
	
	/**
	 * Updates flames state. Used to remove flames after a given time.
	 */
	
	@Override
	public void update() {
		if(animation < (int)(60 * duration)) 
			animation++;
		else 
			remove();
	}
	
	/**
	 * Renders flames in the given screen. Calculates which flames should be rendered. Eg. if there's a wall collision, flames
	 * should not be rendered in that position.
	 */

	@Override
	public void render(Screen screen) {
		renderFlame(0, 0, Sprite.explosionCenter.get(range - 1), screen, false);
		for(int i = 0; i < range; i++) {
			int xa = i + 1;
			int ya = i + 1;
			if(xa == range) {
				renderFlame(- xa * 16, 0, Sprite.explosionLeft.get(range - 1), screen, flagLeft);
				renderFlame(0 , - ya * 16, Sprite.explosionUp.get(range - 1), screen, flagUp);
				renderFlame(xa * 16, 0, Sprite.explosionRight.get(range - 1), screen, flagRight);
				renderFlame(0 , ya * 16, Sprite.explosionDown.get(range - 1), screen, flagDown);
				flagUp = flagDown = flagLeft = flagRight = false;
			}
			else {
				renderFlame(- xa * 16, 0, Sprite.explosionHorizontal.get(range - 1), screen, flagLeft);
				renderFlame(0 , - ya * 16, Sprite.explosionVertical.get(range - 1), screen, flagUp);
				renderFlame(xa * 16, 0, Sprite.explosionHorizontal.get(range - 1), screen, flagRight);
				renderFlame(0 , ya * 16, Sprite.explosionVertical.get(range - 1), screen, flagDown);
			}
		}
	}
	
	/**
	 * Method to render each flame in the given absolute coordinates.
	 * @param xa Absolute X coordinate.
	 * @param ya Absolute Y coordinate.
	 * @param sprite Corresponding sprite to the given direction
	 * @param screen Screen to render flame
	 * @param flag Flag used to check is flame can be rendered
	 */
	
	private void renderFlame(int xa, int ya, Sprite sprite, Screen screen, boolean flag) {
		if(!wallCollision(xa, ya) && !flag) {
			screen.render(x + xa, y + ya, sprite, false, 0);
		}
		else {
			if(xa < 0 && ya == 0) flagLeft = true;
			if(xa == 0 && ya < 0) flagUp = true;
			if(xa > 0 && ya == 0) flagRight = true;
			if(xa == 0 && ya > 0) flagDown = true;
			
			if(!flag) {
				for(int i = 0; i < map.getWalls().size(); i++) {
					if(map.getWalls().get(i).getX() == x + xa && map.getWalls().get(i).getY() == y + ya) {
						map.getWalls().get(i).destroy(range);
					}
				}
			}
		}
		checkElements(map.getElements().get("player"), xa, ya, true);
		checkElements(map.getElements().get("enemies"), xa, ya, false);
	}
	
	/**
	 * Checks flames collision with game element, player or enemy and kills them.
	 * @param list List of element to check collision with
	 * @param x X coordinate of the flame
	 * @param y Y coordinate of the flame
	 * @param player Flag to check if calculate collision with player or enemy.
	 */
	
	private void checkElements(ArrayList <GameElement> list, int x, int y, boolean player) {
		for(int i = 0; i < list.size(); i++) {
			if(elementCollision(x, y, list.get(i))) {
				if(player) ((Player)list.get(i)).die();
				else ((Enemy)list.get(i)).die();
			}
		}
	}
	
	/**
	 *
	 * @return Explosion range.
	 */
	
	public int getRange() {
		return this.range;
	}
	
	/**
	 * 
	 * @return Flags used to check if flames can be rendered in a given direction.
	 */
	
	public Boolean[] getFlags() {
		return new Boolean[] {flagUp, flagRight, flagDown, flagLeft};
	}
}
