package bomberman.multiplayer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.Timer;

import bomberman.server.Server;

/**
 * CLass to represent multiplayer game logic.
 * @author andre
 *
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private int tileSize = 16;
	private int width = 13 * tileSize;
	private int  height = 13 * tileSize;

	private boolean running = false;
	
	private Thread thread;
	private Maze maze;
	private Server server;
	
	private HashMap <Socket, Player> players = new HashMap <Socket, Player> ();
	private ArrayList <Bomb> bombs = new ArrayList <Bomb>();
	private ArrayList <Flames> flames = new ArrayList <Flames>();

	private boolean gameOver = false;
	
	/**
	 * Instantiate new game.
	 * @param server Corresponding server.
	 * @param gamers List of gamers.
	 */
	public Game(Server server, HashMap <Socket, HashMap <String, Integer>> gamers) {
		this.server = server;
		this.maze = new Maze(this);
		
		for (Socket socket: gamers.keySet()) {
    	    Map.Entry <String, Integer> gamer = gamers.get(socket).entrySet().iterator().next();
    	    players.put(socket, new Player(this, maze, socket, gamer.getKey(), gamer.getValue()));
    	}
		start();
	}
	/**
	 * Start game.
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}	
	/**
	 * Stop game.
	 */
	public synchronized void stop() {
		running = false;
		server.setGamerRunning(false);
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that runs game logic. Updates all the game elements, including the map.
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				delta--;
			}
		}
	}
	
	/**
	 * Updates all the game elements.
	 */
	public void update() {
		for(int i = 0; i < bombs.size(); i++)
			bombs.get(i).update();

		for(int i = 0; i < flames.size(); i++) 
			flames.get(i).update();
		
		for(Player player : players.values()) 
			player.update();
		
		int aux = 0;
		Player p = null;
		for(Player player : players.values()) {
			if(player.isAlive()) {
				aux++;
				p = player;
			}
		}
		if(aux <= 1) {
			long i = System.currentTimeMillis();
			while(System.currentTimeMillis() - i < 3000);
			server.sendToAll("gameover" + players.get((p == null) ? "" : p.getSocket()).getUsername());
			server.chatStart();
			stop();
		}
	}
	
	/**
	 * Moves players
	 * @param socket Corresponding socket connection.
	 * @param action Action for player.
	 */
	public void movePlayer(Socket socket, String action) {
		if(!players.get(socket).isMoving())
			players.get(socket).setAction(action);
		else
			server.send("nothing", socket);
	}
	/**
	 * 
	 * @return Server.
	 */
	public Server getServer() {
		return this.server;
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
	 * @return Map/Maze.
	 */
	public Maze getMaze() {
		return this.maze;
	}
/**
 * 
 * @return Game width.
 */
	public int getGameWidth() {
		return this.width;
	}
	/**
	 * Game height.
	 * @return
	 */
	
	public int getGameHeight() {
		return this.height;
	}
	/**
	 * 
	 * @return List of bombs.
	 */

	public ArrayList <Bomb> getBombs() {
		return this.bombs;
	}

	/**
	 * 
	 * @return List of flames.
	 */
	public ArrayList <Flames> getFlames() {
		return this.flames;
	}
	
	/**
	 * 
	 * @return List of players.
	 */
	public HashMap <Socket, Player> getPlayers(){
		return this.players;
	}

}
