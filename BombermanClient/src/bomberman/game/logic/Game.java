package bomberman.game.logic;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

import bomberman.game.activeelements.Bomb;
import bomberman.game.activeelements.Enemy;
import bomberman.game.activeelements.Flames;
import bomberman.game.activeelements.Player;
import bomberman.game.graphics.Screen;
import bomberman.game.input.Keyboard;
import bomberman.game.level.Map;
import bomberman.game.ui.GameFrame;
import bomberman.game.ui.GamePanel;
import bomberman.game.ui.StatusBar;
import bomberman.main.Main;
import bomberman.main.MainFrame;
import bomberman.menu.panels.MenuPanel;
import bomberman.sound.Sound;


/**
 * Class to represent singleplayer game. Whenever is instantiated, the game runs on a thread.
 * @author thebomberman
 *
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private int tileSize = 16;
	private int width = 13 * tileSize;
	private int  height = 13 * tileSize;
	private double scale;
	
	private String title = "Bomberman";
	private boolean running = false;
	private final int levelMax = 2;
	
	private Thread thread;
	private Screen screen;
	private GameFrame frame;
	private StatusBar statusBar;
	private GamePanel gamePanel;
	
	private Keyboard keyboard;
	
	private Map map;
	private Player player;
	
	private ArrayList <Enemy> enemies = new ArrayList <Enemy>();
	private ArrayList <Bomb> bombs = new ArrayList <Bomb>();
	private ArrayList <Flames> flames = new ArrayList <Flames>();
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

	private int gameTime = 200;
	private boolean gameOver = false;
	private int level;
	private boolean pause = false;
	
	/**
	 * Constructor used to create new game.
	 * @param frame Game frame.
	 * @param sb Status bar.
	 * @param level Level to load.
	 */
	public Game(GameFrame frame, StatusBar sb, int level) {
		this.frame = frame;
		this.statusBar = sb;
		setSize();
		this.level = level;
		map = new Map(level, this);
		initialize();
	}
	/**
	 * Constructor used to instantiate a saved game.
	 * @param frame Game frame.
	 * @param sb Status Bar.
	 * @param mp Formated String containing the saved game state.
	 */

	public Game(GameFrame frame, StatusBar sb, String mp) {
		this.frame = frame;
		this.statusBar = sb;
		pause = false;
		keyboard = new Keyboard(false);
		setSize();
		map = new Map(mp, this);
		this.level = map.getLevel();
		screen = new Screen(this, width, height);
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
	 * Initializations.
	 */
	
	private void initialize() {
		screen = new Screen(this, width, height);
		keyboard = new Keyboard(false);

		player = new Player(map, this, keyboard);
		
		for(int i = 0; i < map.getCoordinates('e').size(); i++)
			enemies.add(new Enemy(map, i));

		addKeyListener(keyboard);
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
				if(!pause)
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
				if(overTime == 3) {
					if(Main.music) {
						Sound.theme.stop();
						Sound.menu.play();
					}
					new MainFrame(frame);
					stop();
				}
				if(!pause)
					gameTime--;
				if(gameOver)
					overTime++;
			}
			
			if(gameTime == 0 && player.getLife() >= 0) {
				Sound.timeOut.play();
				player.setAlive(false);
			}
			
			if(!player.isAlive()) {
				if(player.getLife() >= 0) {
					player.setAlive(true);
					levelUp(level);
				}
				else {
					pause = true;
					gameOver = true;
				}
			}
			
			if(player.atExit()) {
				level++;
				if(level <= levelMax)
					levelUp(level);
				else {
					pause = true;
					gameOver = true;
				}
			}
			updateStatsBar();	
		}
	}
	
	/**
	 * Method to load the next level.
	 * @param level Level to load.
	 */
	private void levelUp(int level) {
		player.setExit(false);
		player.setAlive(true);
		player.setMoving(true);
		
		deleteElements();
		map = new Map(level, this);
		map.addPlayer(player);
		
		for(int i = 0; i < map.getCoordinates('e').size(); i++) 
			enemies.add(new Enemy(map, i));
		keyboard.clearInput();
		
		player.setCoord(map.getCoordinates('p').get(0)[0] * 16, map.getCoordinates('p').get(0)[1] * 16);
		player.setMap(map);
		gameTime = 200;
	}
	
	/**
	 * Method to remove all elements. Used when loading next level.
	 */
	
	private void deleteElements() {
		for(int i = enemies.size() - 1; i >= 0; i--)
			enemies.remove(enemies.get(i));
		
		for(int i = bombs.size() - 1; i >= 0; i--)
			bombs.remove(bombs.get(i));
		
		for(int i = flames.size() - 1; i >= 0; i--)
			flames.remove(flames.get(i));
	}
	
	/**
	 * Updates status bar according to the player state.
	 */
	
	private void updateStatsBar() {
		statusBar.time.setContent("Time " + gameTime);
		statusBar.life.setContent("Life " + player.getLife());
		statusBar.speed.setContent("Speed " + player.getSpeed());
		statusBar.range.setContent("Range " + player.getRange());
		statusBar.score.setContent("Score " + player.getScore());
	}
	
	/**
	 * Method to update the game elements.
	 */

	public void update() {
		if(!player.isMoving())
			keyboard.update();

		for(int i = 0; i < bombs.size(); i++)
			bombs.get(i).update();

		for(int i = 0; i < flames.size(); i++) 
			flames.get(i).update();

		for(int i = 0; i < enemies.size(); i++) 
			enemies.get(i).update();
		
		player.update();
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
		renderBombs();
		renderFlames();
		renderWalls();
		renderEnemies();
		renderPlayer();
		draw(bs, screen);
	}
	
	/**
	 * Method to render the bombs. Used by the render method.
	 */
	
	private void renderBombs() {
		for(int i = 0; i < bombs.size(); i++) {
			if(bombs.get(i).isRemoved()) {
				flames.add(new Flames(bombs.get(i), map));
				bombs.remove(i);
			}
			
			else
				bombs.get(i).render(screen);
		}
	}
	
	/**
	 * Method to render the flames. Used by the render method.
	 */
	
	private void renderFlames() {
		for(int i = 0; i < flames.size(); i++) {
			if(flames.get(i).isRemoved()) 
				flames.remove(i);
			else 
				flames.get(i).render(screen);
		}
	}
	
	/**
	 * Method to render the destroyable walls. Used by the render method.
	 */
	
	private void renderWalls() {
		for(int i = 0; i < map.getWalls().size() ; i++) {
			map.getWalls().get(i).render(screen);
		}
	}
	
	/**
	 * Method to render the enemies. Used by the render method.
	 */
	
	private void renderEnemies() {
		for(int i = 0; i < enemies.size() ; i++) {
			enemies.get(i).render(screen);
			if(!enemies.get(i).isAlive())
				enemies.remove(enemies.get(i));
		}
	}
	
	/**
	 * Method to render the bplayer. Used by the render method.
	 */
	
	private void renderPlayer() {
		player.render(screen);
		if(player.dropedBomb() && !player.isMoving()) {
			player.setDropedBomb(false);
			bombs.add(new Bomb(player, map));
		}
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
		if(gameOver) {
			g.setColor(Color.WHITE);
	        g.setFont(new Font("Eight-Bit Madness", Font.PLAIN, 100));
	        g.drawString("Game Over", (int)(scale * width / 4), (int)(scale * height / 2));
		}
		g.dispose();
		bs.show();
	}
	
	/**
	 *  
	 * @return Coordinates offset to render the map. The offset is calculated according to the player position, make the player the main focus.
	 */
	
	private int[] followPlayer() {
		int xScroll = 0, yScroll = 0;
		int[] aux = new int [2];
		
		if(player.getX() - (screen.getWidth() / 2) > 0 && player.getX() < (map.getWidth() * 16) - (screen.getWidth() / 2))
			xScroll = player.getX() - screen.getWidth() / 2;
		if((player.getX() >= (map.getWidth() * 16) - (screen.getWidth() / 2))  && map.getWidth() * 16 != screen.getWidth()) 
			xScroll = (map.getWidth() * 16) - screen.getWidth();
		if(player.getY() - ((screen.getHeight()) / 2) > 0 && player.getY() < (map.getHeight() * 16) - ((screen.getHeight()) / 2))
			yScroll = player.getY() - screen.getHeight() / 2;
		if((player.getY() >= (map.getHeight() * 16) - ((screen.getHeight()) / 2)) && map.getHeight() * 16 != (screen.getHeight()))
			yScroll = (map.getHeight() * 16) - screen.getHeight();
		aux[0] = xScroll;
		aux[1] = yScroll;
		return aux;
	}
	
	/**
	 * @param t Adds time by a given amount.
	 */
	public void addTime(int t) {
		gameTime += t;
	}
	/**
	 * 
	 * @return Game score.
	 */
	
	public int getScore() {
		return this.player.getScore();
	}
	
	/**
	 * 
	 * @return Tile size (16 pixels in this case).
	 */
	public int getTileSize() {
		return this.tileSize;
	}
	
	/**
	 * 
	 * @return Game scale. Depends on the size of the running computer screen.
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
	 * @param statusBar Set game corresponding status bar.
	 */
	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
	
	/**
	 *  @param gamePanel Set corresponding game panel.
	 */
	public void setPanel(GamePanel gamePanel){
		this.gamePanel = gamePanel;
	}
	
	/**
	 * Change game size. Used when changing fullscreen mode.
	 */
	public void changeSize() {
		frame.setVisible(false);
		if(Main.fullScreen) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double h = screenSize.getHeight();
			scale = h / (width + tileSize);
			setSize();
		}
		else {
			scale = 3;
			setSize();
		}
		
		gamePanel.changeSize();
		revalidate();
		repaint();
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
	 * @return If paused.
	 */
	public boolean isPaused() {
		return this.pause;
	}
	
	/**
	 * 
	 * @param pause Pause the game or not.
	 */
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/**
	 * 
	 * @return Get bombs.
	 */
	public ArrayList <Bomb> getBombs() {
		return this.bombs;
	}

	/**
	 * 
	 * @return Get flames.
	 */
	public ArrayList <Flames> getFlames() {
		return this.flames;
	}
	/**
	 * 
	 * @return Get enemies.
	 */

	public ArrayList <Enemy> getEnemies(){
		return this.enemies;
	}
	/**
	 * 
	 * @return Get player.
	 */
	
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * 
	 * @return Get remaining time.
	 */
	
	public int getTime() {
		return this.gameTime;
	}
	
	/**
	 * 
	 * @param time Set remaining time.
	 */
	
	public void setTime(int time) {
		this.gameTime = time;
	}
	
	/**
	 * 
	 * @param player Set ganme player.
	 */
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * 
	 * @return Get game keyboard.
	 */
	public Keyboard getInput() {
		return this.keyboard;
	}
}
