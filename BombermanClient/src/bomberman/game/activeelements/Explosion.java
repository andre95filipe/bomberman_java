package bomberman.game.activeelements;

import java.util.ArrayList;
import java.util.HashMap;

import bomberman.game.graphics.Screen;
import bomberman.game.graphics.Sprite;
import bomberman.game.level.Map;
import bomberman.sound.Sound;


/**
 * Class used to instantiate explosion after removing the corresponding bomb. THis class is used only in multiplayer games.
 * @author thebomberman
 *
 */
public class Explosion extends ActiveElement {
	
	private int range;
	private double duration = 1.8;
	private boolean[][] flags = new boolean [4][3]; // 0 left, 1 up, 2 down, 3 right
	private int[][] dir = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
	
	/**
	 * For a given direction corresponds a sprite.
	 */
	
	private HashMap <Integer, ArrayList <Sprite>> dirSprite = new HashMap <Integer, ArrayList <Sprite>>(){
		private static final long serialVersionUID = 1L;
	{
		put(0, Sprite.explosionHorizontal);
		put(1, Sprite.explosionVertical);
		put(2, Sprite.explosionVertical);
		put(3, Sprite.explosionHorizontal);
	}};
	
	/**
	 * Constructor to instantiate an explosion.
	 * @param info Formated String with the parameters initialization. Split by commas
	 * @param map Belongs to only one Map.
	 */
	
	public Explosion(String info, Map map) {
		Sound.explosion.play();
		String[] aux = info.split(",");
		this.x = Integer.valueOf(aux[0]);
		this.y = Integer.valueOf(aux[1]);
		this.map = map;
		for(int i = 2; i < aux.length; i++) {
			String[] a = aux[i].split("-");
			for(int j = 0; j < a.length; j++) {
				flags[i - 2][j] = Boolean.valueOf(a[j]);
			}
		}
		this.range = flags[0].length - 1;
		initTime = System.currentTimeMillis();
	}

	
	@Override
	public void update() {
	}
	
	/**
	 * The method renders the explosion in the given screen.
	 */

	@Override
	public void render(Screen screen) {
		screen.render(x, y, Sprite.explosionCenter.get(range - 1), false, 0);
		for(int j = 0; j < 4; j++) {
			for(int i = 0; i < range; i++) {
				if(flags[j][i]) 
					screen.render(x + (i + 1) * 16 * dir[j][0], y + (i + 1) * 16 * dir[j][1], dirSprite.get(j).get(range - 1), false, 0);
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
}