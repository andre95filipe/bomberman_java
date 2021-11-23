package bomberman.multiplayer;

public class DestroyableTile extends Tile {
	
	private Maze map;
	private Game game;
	private int x, y;
	private int tile = 0;
	private boolean destroy = false;
	private long initTime = 0;

	public DestroyableTile(Maze map, Game game, int x, int y) {
		initialize(map, game, x, y);
	}
	
	private void initialize(Maze map, Game game, int x, int y) {
		this.map = map;
		this.game = game;
		this.x = x;
		this.y = y;
		solid = true;
		breakable = true;
	}

	public void destroy(int range) {
		if(System.currentTimeMillis() - initTime > 200 / range  && !game.isPaused()) {
			initTime = System.currentTimeMillis();
			destroy = true;
			if(tile == 3)
				map.getWalls().remove(this);
		}
	}
	
	public void update() {
		if(destroy && (System.currentTimeMillis() - initTime > 200)) {
			tile++;
			initTime = System.currentTimeMillis();
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getTile() {
		return this.tile;
	}
	
	public boolean isDestroy() {
		return this.destroy;
	}

}
