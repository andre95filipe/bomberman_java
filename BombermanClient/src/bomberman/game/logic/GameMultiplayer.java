package bomberman.game.logic;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bomberman.game.activeelements.Bomb;
import bomberman.game.activeelements.Explosion;
import bomberman.game.activeelements.Flames;
import bomberman.game.activeelements.Gamer;
import bomberman.game.graphics.Screen;
import bomberman.game.input.Keyboard;
import bomberman.game.level.Map;
import bomberman.game.ui.StatusBar;
import bomberman.main.Main;
import bomberman.sound.Sound;


/**
 * Class to represent multiplayer game. Whenever is instantiated, the game runs on a thread.
 * @author thebomberman
 *
 */
public class GameMultiplayer extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JPanel panel;
	private Thread thread, keyListener;
	private StatusBar statusBar;
	private Map map;
	private Keyboard keyboard;
	private String title = "Bomberman V1.0";
	private String username;
	private Screen screen;
	private final int tileSize = 16;
	private int width = 13 * tileSize, height = 13 * tileSize;
	private double scale;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	private HashMap <String, Gamer> players = new HashMap <String, Gamer>();
	private ArrayList <Bomb> bombs = new ArrayList <Bomb>();
	private ArrayList <Explosion> flames = new ArrayList <Explosion>();
	private ArrayList <Gamer> playersRemove = new ArrayList <Gamer>();
	
	private boolean running = false;
	
	/**
	 * COntructor to instantiate a game with a given Map String.
	 * @param frame Game frame.
	 * @param sb Status bar.
	 * @param map Formated String containing the map.
	 * @param username Username.
	 */
	public GameMultiplayer(JFrame frame, StatusBar sb, String map, String username) {
		running = true;
		this.frame = frame;
		this.statusBar = sb;

		map = "0/" + map;
		this.map = new Map(map);
		this.username = Main.client.getUsername();
		
		setSize();
		
		this.screen = new Screen(this, width, height);
		this.keyboard = new Keyboard(true);

		Main.client.sendData("sendplayers");
		addKeyListener(keyboard);
	}
	
	/**
	 * Method to set game size. Used in the initialization and whenever the fullscreen mode is changed.
	 */
	private void setSize() {
		if(Main.fullScreen) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double h = screenSize.getHeight();
			scale = h / (height + tileSize);
		}
		else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double h = screenSize.getHeight();
			scale = 3 * h / 768;
		}
		
		Dimension size = new Dimension((int)(width * scale), (int)(height * scale));
		setPreferredSize(size);
	}
	/**
	 * Method to start game.
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}	
	/**
	 * Method to stop game. Used only when game is not needed.
	 */
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method used when game starts. Contains the main game loop in which the game is updated and rendered.
	 * Rendering is used to render pixels to the screen. This is used every loop iteration.
	 * Update is used to maintain the game velocity independent from the pc used. The game updates it's state ate a constant frequency.
	 * (60 updates per second).
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		int overTime = 0;
		requestFocus(); 
		Sound.theme.play();
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + updates + " ups  |  " + frames + " fps");
				updates = 0;
				frames = 0;
			}	
		}
	}
	/**
	 * Method to render every element in the game, including the map.
	 */
	private void render() {
		int[] scroll = new int[2];
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		scroll = followPlayer();
		map.render(scroll[0], scroll[1], screen);
		for(int i = 0; i < bombs.size(); i++) 
			bombs.get(i).render(screen);
		for(int i = 0; i < flames.size(); i++)
			flames.get(i).render(screen);
		for(Gamer player : players.values()) {
			player.render(screen);
		}
		draw(bs, screen);
	}
	/**
	 *  
	 * @return Coordinates offset to render the map. The offset is calculated according to the player position, make the player the main focus.
	 */
	private int[] followPlayer() {
		int xScroll = 0, yScroll = 0;
		int[] aux = new int [2];
		
		if(players.get(username).getX() - (screen.getWidth() / 2) > 0 && players.get(username).getX() < (map.getWidth() * 16) - (screen.getWidth() / 2))
			xScroll = players.get(username).getX() - screen.getWidth() / 2;
		if((players.get(username).getX() >= (map.getWidth() * 16) - (screen.getWidth() / 2))  && map.getWidth() * 16 != screen.getWidth()) 
			xScroll = (map.getWidth() * 16) - screen.getWidth();
		if(players.get(username).getY() - ((screen.getHeight()) / 2) > 0 && players.get(username).getY() < (map.getHeight() * 16) - ((screen.getHeight()) / 2))
			yScroll = players.get(username).getY() - screen.getHeight() / 2;
		if((players.get(username).getY() >= (map.getHeight() * 16) - ((screen.getHeight()) / 2)) && map.getHeight() * 16 != (screen.getHeight()))
			yScroll = (map.getHeight() * 16) - screen.getHeight();
		aux[0] = xScroll;
		aux[1] = yScroll;
		return aux;
	}
	/**
	 * Method to draw generated pixels to the given screen. Used by the render method.
	 * @param bs BufferStrategy, buffer that is used to store pixels before rendering to screen.
	 * @param screen Screen to get pixels from.
	 */
	private void draw(BufferStrategy bs, Screen screen){
		for(int i = 0; i < pixels.length; i++) 
			pixels[i] = screen.getPixels()[i];
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	/**
	 * Method to update the game elements.
	 */
	public void update() {
		for(int i = 0; i < bombs.size(); i++)
			bombs.get(i).update();
		for(int i = 0; i < flames.size(); i++)
			flames.get(i).update();
		for(Gamer player : players.values()) {
			if(player.isAlive())
				player.update();
			else {
				if(!player.getUsername().equals(username))
					playersRemove.add(player);
			}	
		}
		for(int i = 0; i < playersRemove.size(); i++) {
			players.remove(playersRemove.get(i).getUsername());
			playersRemove.remove(playersRemove.get(i));
		}
	}
	/**
	 * Method to move player by a given command.
	 * @param user Username which action is applied.
	 * @param dirX Absolute X coordinate.
	 * @param dirY Absolute Y coordinate.
	 */
	public void movePlayer(String user, int dirX, int dirY) {
		players.get(user).setDirection(dirX, dirY);
	}
	
	/**
	 * Add player to the game.
	 * @param user Player username.
	 * @param color Player character color.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	
	public void addPlayer(String user, int color, int x, int y) {
		players.put(user, new Gamer(this, map, user, color, x, y));
		if(Main.client.getUsername().equals(user))
			keyboard.addGamer(players.get(user));
	}
	/**
	 * 
	 * @param panel Set game panel.
	 */
	
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * 
	 * @return Get the main player username.
	 */
	
	public String getUsername() {
		return this.username;
	}
	/**
	 * 
	 * @return Tile size (16 pixels in this case)
	 */
	
	public int getTileSize() {
		return this.tileSize;
	}
	/**
	 * 
	 * @return Game scale.
	 */
	
	public double getScale() {
		return this.scale;
	}
	
	/**
	 * 
	 * @return Game width.
	 */
	public int getGameWidth() {
		return this.width;
	}
	
	/**
	 * 
	 * @return Game height.
	 */
	public int getGameHeight() {
		return this.height;
	}
	
	/**
	 * 
	 * @param statusBar Set game status bar.
	 */
	
	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
	
	/**
	 * 
	 * @return Map.
	 */
	
	public Map getMap() {
		return this.map;
	}
	
	/**
	 * 
	 * @return If game is running.
	 */
	public boolean isRunning() {
		return this.running;
	}
	/**
	 * 
	 * @return Get bombs.
	 */
	public ArrayList <Bomb> getBombs(){
		return this.bombs;
	}
	/**
	 * 
	 * @return Get flames.
	 */
	public ArrayList <Explosion> getFlames(){
		return this.flames;
	}
	
	/**
	 * Explode bomb at the given coordinates.
	 * @param x X coordinate of the bomb.
	 * @param y Y coordinate of the bomb.
	 * @param s Formated String containing parameters of the generated flames.
	 */
	public void bombExplode(int x, int y, String s) {
		for(int i = 0; i < bombs.size(); i++) {
			if(bombs.get(i).getX() == x && bombs.get(i).getY() == y)
				bombs.get(i).explode();
		}
		flames.add(new Explosion(s, map));
	}
	/*
	 * Method to remove flames from the game.
	 */
	public void removeFlames(int x, int y) {
		for(int i = 0; i < flames.size(); i++) {
			if(x == flames.get(i).getX() && y == flames.get(i).getY()) 
				flames.remove(flames.get(i));
		}
	}
	
	/**
	 * 
	 * @param username Player to kill.
	 */
	public void killPlayer(String username) {
		players.get(username).die();
	}
	
	/**
	 * 
	 * @return Get players list.
	 */
	public HashMap <String, Gamer> getPlayers(){
		return this.players;
	}
}