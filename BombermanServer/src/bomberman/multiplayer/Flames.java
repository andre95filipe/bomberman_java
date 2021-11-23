package bomberman.multiplayer;
/**
 * This class is used to instantiate flames when the bomb explodes.
 * @author thebomberman
 *
 */
public class Flames extends ActiveElement {
	
	private int range;
	private Game game;
	private double duration = 1.8;
	private boolean[][] flags = new boolean[4][3]; // 0 left, 1 up, 2 down, 3 right
	private int[][] dir = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
	
	/**
	 * Constructor for whenever a bomb explodes
	 * @param bomb Places flames around this bomb's coordinates.
	 * @param game Game.
	 * @param map Belongs only to one map.
	 */
	
	public Flames(Bomb bomb, Game game, Maze map) {
		this.animation = 0;
		this.x = bomb.getX();
		this.y = bomb.getY();
		this.game = game;
		this.range = bomb.getRange();
		this.map = map;
		int k = 0;
		for(int i = -2; i < 3; i++) {
			if(i != 0) {
				for(int j = 1; j <= range; j++) {
					flags[k][j - 1] = !wallCollision(x + j * (i / 2) * 16, y + j * (i % 2) * 16);
					System.out.println("flames " + (x + j * (i / 2) * 16) + ", "  + (y + j * (i % 2) * 16) + " collision " + flags[k][j - 1]);
				}
				k++;
			}	
		}
		String info = "addflames" + x + "," + y + ",";
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < range; j++)
				info += flags[i][j] + "-";
			info += ",";
		}
		game.getServer().sendToAll(info);
		initTime = System.currentTimeMillis();
	}
	
	/**
	 * Update flame state.
	 */
	@Override
	public void update() {
		if(animation < (int)(60 * duration)) {
			animation++;
			for(Player player : game.getPlayers().values()) {
				if(elementCollision(0,  0, player)) {
					game.getServer().sendToAll("killplayer" + player.getUsername());
					player.die();
				}
				for(int j = 0; j < 4; j++) {
					for(int k = 1; k <= range; k++) {
						if(flags[j][k - 1]) {
							if(elementCollision(k * dir[j][0] * 16,  k * dir[j][1] * 16, player) && !player.isDead()) {
								game.getServer().sendToAll("killplayer" + player.getUsername());
								player.die();
							}
						}	
					}
				}
			}
		}
		else {
			game.getServer().sendToAll("removeflames" + x + "," + y);
			game.getFlames().remove(this);
		}
	}
	
	/**
	 * 
	 * @return Get flame range.
	 */
	public int getRange() {
		return this.range;
	}
}
