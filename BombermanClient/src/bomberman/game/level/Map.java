package bomberman.game.level;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import bomberman.game.activeelements.Bomb;
import bomberman.game.activeelements.Enemy;
import bomberman.game.activeelements.Flames;
import bomberman.game.activeelements.Player;
import bomberman.game.elements.GameElement;
import bomberman.game.graphics.Screen;
import bomberman.game.logic.Game;
import bomberman.game.tile.DestroyableTile;
import bomberman.game.tile.Tile;
import bomberman.game.ui.GameFrame;


/**
 * Class representing the game Map.
 * 
 * @author thebomberman
 *
 */
public class Map {
	private int width;
	private int height;
	private int level;
	private static char[] tiles;
	private Game game;
	
	/**
	 * Each character in the level file corresponds to a tile/sprite.
	 */
	private HashMap <Character, Tile> tilesMap = new HashMap <Character, Tile> (){
		private static final long serialVersionUID = 1L;
	{
		put('#', Tile.fixedWall);
		put(' ', Tile.grass);
		put('c', Tile.clockUp);
		put('b', Tile.bombRange);
		put('s', Tile.speedUp);
		put('l', Tile.lifeUp);
		put('x', Tile.exit);
	}};
	
	/**
	 * This stores the coordinates of the enemies and player when loading the level file. After they are used when 
	 * any of the active elements mentioned before. 
	 */
	
	private HashMap <Character, ArrayList <Integer[]>> coordinates = new HashMap <Character, ArrayList <Integer[]>> (){
		private static final long serialVersionUID = 1L;
	{
		put('p', new ArrayList <Integer[]> ());
		put('e', new ArrayList <Integer[]> ());
	}};
	
	/**
	 * Stores the elements player, enemy and flames.
	 */
	
	private HashMap <String, ArrayList<GameElement>> elements = new  HashMap <String, ArrayList <GameElement>>() {
		private static final long serialVersionUID = 1L;
	{
		put("player", new ArrayList <GameElement>());
		put("enemies", new ArrayList <GameElement>());
		put("flames", new ArrayList <GameElement>());
	}};
	
	/**
	 * Stores all the destroyable walls.
	 */
	private ArrayList <DestroyableTile> walls = new ArrayList <DestroyableTile>();
	
	/**
	 * Construtor for instantiating a new Map in a new game.
	 * @param level Which level you want to load.
	 * @param game Corresponding game.
	 */
	
	public Map(int level, Game game) {
		this.game = game;
		loadLevel(readFile(String.valueOf(level)));
	}

	/**
	 * Constructor for creating a saved game map.
	 * @param save Formated String with all the information obtained from the database. 
	 * 
	 * @param game Corresponding game.
	 */
	public Map(String save, Game game) {
		this.game = game;
		String[] g = save.split("/");
		this.level = Integer.valueOf(g[0]);
		game.setTime(Integer.valueOf(g[1]));
		int score = Integer.valueOf(g[2]);
		loadLevel(readFile(String.valueOf(level) + "-empty"));
		
		addPowerUp('c', g[3]);
		addPowerUp('b', g[4]);
		addPowerUp('s', g[5]);
		addPowerUp('l', g[6]);
		
		addWalls(g[7]);
		addBombs(g[8]);
		addFlames(g[9]);
		addEnemies(g[10]);
		addPlayer(g[11], score);
	}
	
	/**
	 * Constructor for a map in a multiplayer game. 
	 * @param m Formated String containing the multiplayer map.
	 */
	
	public Map(String m) {
		String[] mp = m.split("/");
		mp[0] += mp[0] + "/" + mp[1] + "/" + mp[2];
		for(int i = 3; i < mp.length; i++) 
			mp[i - 2] = mp[i];
		loadLevel(mp);
	}
	
	/**
	 * Method to add powerups to the game. Used when loading saved game.
	 * @param pu Character of the corresponding powerup.
	 * @param coords Formated String with the powerup coordinates. May contain more than one powerup.
	 */
	
	private void addPowerUp(char pu, String coords) {
		String[] aux = coords.split("#");
		for(int i = 1; i < aux.length; i++) {
			String[] c = aux[i].split(",");
			int x = Integer.valueOf(c[0]);
			int y = Integer.valueOf(c[1]);
			tiles[x + y * width] = pu;
			walls.add(new DestroyableTile(this, this.game,  x * 16, y * 16));
		}
	}
	
	/**
	 * Method to add walls to the map.
	 * @param info Formated String containing walls states. Used when loading saved game.
	 */
	private void addWalls(String info) {
		String[] walls = info.split("#");
		for(int i = 1; i < walls.length; i++) 
			this.walls.add(new DestroyableTile(this, this.game, walls[i]));
	}
	
	/**
	 * Method to add bombs to the map. Used when loading saved game.
	 * @param info Formated String containing every bomb state.
	 */
	private void addBombs(String info) {
		String[] bombs = info.split("#");
		for(int i = 1; i < bombs.length; i++)
			game.getBombs().add(new Bomb(this, bombs[i]));
	}
	
	/**
	 * Method to add flames to the map. Used when loading saved game.
	 * @param info Formated String containing every flame state.
	 */
	private void addFlames(String info) {
		String[] flames = info.split("#");
		for(int i = 1; i < flames.length; i++)
			game.getFlames().add(new Flames(this, flames[i]));
	}
	/**
	 * Method to add enemies to the map. Used when loading saved game.
	 * @param info Formated String containing every enemy state.
	 */
	private void addEnemies(String info) {
		String[] enemies = info.split("#");
		for(int i = 1; i < enemies.length; i++) 
			game.getEnemies().add(new Enemy(this, enemies[i]));
	}
	/**
	 * Method to add player to the map. Used when loading saved game.
	 * @param info Formated String containing player state.
	 */
	private void addPlayer(String info, int score) {
		String[] player = info.split("#");
		for(int i = 1; i < player.length; i++)
			game.setPlayer(new Player(this, game, game.getInput(), player[i], score));
	}
	
	/**
	 * Method used to read level file
	 * @param level Which level to load.
	 * @return Array of Strings, each string containing a row of the level/map.
	 */
	
	private String[] readFile(String level) {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("res/levels/level" + level + ".lvl"));
		} catch (FileNotFoundException e1) {
			System.out.println("Error loading level file");
		}
		int aux = 50;
		String[] line = new String[aux];
		
	    try {
	    	for(int i = 0; i <= aux; i++) {
	    		line[i] = file.readLine();
	    		if(i == 0);
	    			aux = Integer.valueOf((line[0].split("/"))[1]);
	    	}
			file.close();
		} catch (IOException e) {
			System.out.println("Error reading level file");
		}
		return line;
	}

	
	/**
	 * Loads level from an array of Strings and creates game elements and tiles according to the given array.
	 * @param game Array of Strings, each string containing a row of the level/map.
	 */
	private void loadLevel(String[] game) {
		level = Integer.valueOf(game[0].split("/")[0]);
		height = Integer.valueOf(game[0].split("/")[1]);
		width = Integer.valueOf(game[0].split("/")[2]);
		tiles = new char[height * width];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(tilesMap.containsKey(game[y + 1].charAt(x))) {
					tiles[x + y * width] = game[y + 1].charAt(x);
					if(tiles[x + y * width] != ' ' && tiles[x + y * width] != '#')
						walls.add(new DestroyableTile(this, this.game,  x * 16, y * 16));
				}
				else {
					Integer[] aux = {x, y};
					tiles[x + y * width] = ' ';
					if(game[y + 1].charAt(x) == '*' || game[y + 1].charAt(x) == 'x')
						walls.add(new DestroyableTile(this, this.game, x * 16, y * 16));
					else
						coordinates.get(game[y + 1].charAt(x)).add(aux);
				}
			}
		}
	}
	
	/**
	 * Method to render the map. The map is rendered according to the given coordinates and screen size.
	 * @param xScroll X coordinate to start rendering the map.
	 * @param yScroll Y coordinate to start rendering the map.
	 * @param screen Screen in which the map will be rendered.
	 */
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int xMin = xScroll / 16;
		int xMax = (xScroll + screen.getWidth() + 16) / 16;
		int yMin = yScroll / 16; 
		int yMax = (yScroll + screen.getHeight() + 16) / 16;
		
		for(int y = yMin; y < yMax; y++) {
			for(int x = xMin; x < xMax; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
	}
	/**
	 * 
	 * @return Get game elements (player, enemies, flames).
	 */
	public HashMap <String, ArrayList <GameElement>> getElements() {
		return elements;
	}
	
	/**
	 * 
	 * @param element Adds player to the list of elements.
	 */
	
	public void addPlayer(GameElement element) {
		elements.get("player").add(element);
	}
	/**
	 * 
	 * @param element Adds enemy to the list of elements.
	 */
	public void addEnemy(GameElement element) {
		elements.get("enemies").add(element);
	}
	/**
	 * ´Get tile in the given coordinates. If coordinates out of bounds, return void tile to avoid out of bounds exception.
	 * @param x X coordinate to get tile.
	 * @param y Y coordinate to get tile.
	 * @return Tile in the given coordinates.
	 */
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) 
			return Tile.voidTile;
		return tilesMap.get(tiles[x + y * width]);
	}
	
	/**
	 * Set tile in the tiles array, where the main running map is stored.
	 * @param x X coordinate of tile to add.
	 * @param y Y coordinate of tile to add.
	 * @param c Character of the tile to add.
	 */
	public void setTile(int x, int y, char c) {
		tiles[x + y * width] = c;
	}
	
	/**
	 * 
	 * @return Destroyable walls in this map.
	 */
	public ArrayList <DestroyableTile> getWalls() {
		return walls;
	}
	
	/**
	 * Check if it is a wall in the given coordinates.
	 * @param x X coordinate to check if wall.
	 * @param y Y coordinate to check if wall.
	 * @return Returns if it is a wall.
	 */
	public boolean isWall(int x, int y) {
		for(int i = 0; i < getWalls().size(); i++) {
			if(getWalls().get(i).getX() == x && getWalls().get(i).getY() == y)
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param c Character of the element to get.
	 * @return Coordinates of the element corresponding character
	 */
	
	public ArrayList <Integer[]> getCoordinates(char c){
		return coordinates.get(c);
	}
	/**
	 * 
	 * @return Map width.
	 */
	public int getWidth() {
		return this.width;
	}
	/*
	 *  @return Map height.
	 */
	public int getHeight() {
		return this.height;
	}
	/**
	 * 
	 * @return Map level.
	 */
	public int getLevel() {
		return this.level;
	}
	
	/**
	 * Method used to get formated String with the state of the game to save in the database.
	 * @return Formated string with the game state.
	 */
	public String getMap() {
		String info = "";

		ArrayList <ArrayList <Integer[]>> coordsPu= new ArrayList <ArrayList <Integer[]>> () {
			private static final long serialVersionUID = 1L;
		{
			add(new ArrayList <Integer[]> ()); // clock
			add(new ArrayList <Integer[]> ()); // bomb
			add(new ArrayList <Integer[]> ()); // speed
			add(new ArrayList <Integer[]> ()); // life
		}};
		ArrayList <String> coordsWalls = new ArrayList <String> ();
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(tiles[x + y * width] == 'c')
					coordsPu.get(0).add(new Integer[] {x, y});
				if(tiles[x + y * width] == 'b')
					coordsPu.get(1).add(new Integer[] {x, y});
				if(tiles[x + y * width] == 's')
					coordsPu.get(2).add(new Integer[] {x, y});
				if(tiles[x + y * width] == 'l')
					coordsPu.get(3).add(new Integer[] {x, y});
			}
		}

		for(int k = 0; k < walls.size(); k++) {
			boolean match = false;
			for(int i = 0; i < coordsPu.size(); i++) {
				for(int j = 0; j < coordsPu.get(i).size(); j++) {
					if(coordsPu.get(i).get(j)[0] == walls.get(k).getX() / 16 && coordsPu.get(i).get(j)[1] == walls.get(k).getY() / 16) {
						match = true;
					}	
				}
			}
			if(!match)
				coordsWalls.add("#" +
						String.valueOf(walls.get(k).getX()) + "," +
						String.valueOf(walls.get(k).getY()) + "," +
						String.valueOf(walls.get(k).getTile()) + "," +
						String.valueOf(walls.get(k).isDestroy()));
		}
		
		String wall = "";
		for(int i = 0; i < coordsWalls.size(); i++)
			wall = wall + coordsWalls.get(i) ;
		
		String bombs = "";
		for(int i = 0; i < game.getBombs().size(); i++) {
			bombs = bombs + "#" +
					String.valueOf(game.getBombs().get(i).getX()) + "," +
					String.valueOf(game.getBombs().get(i).getY()) + "," +
					String.valueOf(game.getBombs().get(i).getRange()) + "," +
					String.valueOf(game.getBombs().get(i).getTimeLeft()) + ",";
		}
		
		String flames = "";
		for(int i = 0; i < game.getFlames().size(); i++) {
			flames = flames + "#" +
					String.valueOf(game.getFlames().get(i).getX()) + "," +
					String.valueOf(game.getFlames().get(i).getY()) + "," +
					String.valueOf(game.getFlames().get(i).getRange()) + "," +
					String.valueOf(game.getFlames().get(i).getFlags()[0]) + "," +
					String.valueOf(game.getFlames().get(i).getFlags()[1]) + "," +
					String.valueOf(game.getFlames().get(i).getFlags()[2]) + "," +
					String.valueOf(game.getFlames().get(i).getFlags()[3]) + ",";
		}
		
		String enemies = "";
		for(int i = 0; i < game.getEnemies().size(); i++) {
			enemies = enemies + "#" +
					String.valueOf(game.getEnemies().get(i).getX()) + "," +
					String.valueOf(game.getEnemies().get(i).getY()) + "," +
					String.valueOf(game.getEnemies().get(i).getNewX()) + "," +
					String.valueOf(game.getEnemies().get(i).getNewY()) + "," +
					String.valueOf(game.getEnemies().get(i).getDirX()) + "," +
					String.valueOf(game.getEnemies().get(i).getDirY()) + "," +
					String.valueOf(game.getEnemies().get(i).isMoving()) + "," +
					String.valueOf(game.getEnemies().get(i).isDie()) + "," +
					String.valueOf(game.getEnemies().get(i).getCurrSprite()) + "," +
					String.valueOf(game.getEnemies().get(i).getCurrTime()) + "," ;
		}
		
		String player = "#" +
					String.valueOf(game.getPlayer().getX()) + "," +
					String.valueOf(game.getPlayer().getY()) + "," +
					String.valueOf(game.getPlayer().getNewX()) + "," +
					String.valueOf(game.getPlayer().getNewY()) + "," +
					String.valueOf(game.getPlayer().getDirX()) + "," +
					String.valueOf(game.getPlayer().getDirY()) + "," +
					String.valueOf(game.getPlayer().isMoving()) + "," +
					String.valueOf(game.getPlayer().isAlive()) + "," +
					String.valueOf(game.getPlayer().isDie()) + "," +
					String.valueOf(game.getPlayer().getCurrSprite()) + "," +
					String.valueOf(game.getPlayer().getCurrTime()) + "," +
					String.valueOf(game.getPlayer().getLife()) + "," +
					String.valueOf(game.getPlayer().getSpeed()) + "," +
					String.valueOf(game.getPlayer().getCurrTimeSpeed()) + "," +
					String.valueOf(game.getPlayer().getRange()) + "," +
					String.valueOf(game.getPlayer().getCurrTimeRange()) + ",";
				

		
		info += String.valueOf(level) + '/' + 
				String.valueOf(game.getTime()) + '/' + 
				String.valueOf(game.getScore()) + '/' +
				coords(coordsPu.get(0)) + "/" +
				coords(coordsPu.get(1)) + "/" +
				coords(coordsPu.get(2)) + "/" + 
				coords(coordsPu.get(3)) + "/" +
				wall + "/" +
				bombs + "/" +
				flames + "/" +
				enemies + "/" +
				player + "/";
		
		return info;
	}
	
	/**
	 * Method used by the getMap method, for getting element coordinates.
	 * @param coord Coordinates of the given elements.
	 * @return Returns formated string with elements coordinates ready for being append to game state formated String.
	 */
	
	private String coords(ArrayList <Integer[]> coord) {
		String aux = "";
		for(int i = 0; i < coord.size(); i++)
			aux = aux + "#" + coord.get(i)[0] + "," + coord.get(i)[1];
		return aux;
	}
	
}