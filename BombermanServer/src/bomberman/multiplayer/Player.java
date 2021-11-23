package bomberman.multiplayer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents player.
 * @author andre
 *
 */
public class Player extends ActiveElement {

	private Game game;
	
	private int speed = 1, bombRange = 1, score = 0, color;
	private String username;
	private Socket socket;
	private boolean dropedBomb = false, exit = false;
	private long initBomb = 0, initRange = 0, currTimeRange = 0, initSpeed = 0, currTimeSpeed = 0;
	private String action = "";

	/**
	 * Create player.
	 * @param game Game.
	 * @param maze Maze.
	 * @param socket Corresponding socket.
	 * @param username COrresponding username.
	 * @param color CHaracter color.
	 */
	public Player(Game game, Maze maze, Socket socket, String username, int color) {
		this.map = maze;
		this.game = game;
		this.socket = socket;
		this.username = username;
		this.color = color;
		switch(color) {
			case 1:
				this.x = 1 * 16;
				this.y = 1 * 16;
				break;
			case 2:
				this.x = (map.getWidth() - 2) * 16;
				this.y = 1 * 16;
				break;
			
			case 3:
				this.x = (map.getWidth() - 2) * 16;
				this.y = (map.getHeight() - 2) * 16;
				break;
			case 4:
				this.x = 1 * 16;
				this.y = (map.getHeight() - 2) * 16;
				break;
		}
	}
	
	/**
	 * Updates the player state.
	 */
	@Override
	public void update() {
		int xa = 0, ya = 0;

		if(action.equals("up")) ya--;
		if(action.equals("down")) ya++;
		if(action.equals("left")) xa--;
		if(action.equals("right")) xa++;
		
		if(xa != 0 || ya != 0) {
			if(alive && !die) {
				if(!moving) {
					newX = (16 * xa) + x;
					newY = (16 * ya) + y;
					dirX = xa;
					dirY = ya;
					moving = true;
					if(!wallCollision(newX, newY))
						game.getServer().sendToAll(username + "/move" + dirX + "," + dirY);
				}
				else 
					move(dirX, dirY);
			}
		}
		else moving = false;
	}
	
	/**
	 * Move the player given the absolute coordinates.
	 * @param xa Absolute X coordinate.
	 * @param ya Absolute Y coordinate.
	 */
	public void move(int xa, int ya) {
		if(!wallCollision(newX, newY)) {
			if(x != newX || y != newY) {
				x += (xa * speed);
				y += (ya * speed);
			}
			else {
				moving = false;
				action = "";
			}
		}
		else {
			moving = false;
			action = "";
		}
	}
	/**
	 * Kill player.
	 */
	public void die() {
		this.alive = false;
	}
	
	/**
	 * Drop bomb.
	 * @param player Socket to send command to.
	 */
	public void dropBomb(Socket player) {
		if(System.currentTimeMillis() - initBomb > 1700) {
			dropedBomb = true;
			initBomb = System.currentTimeMillis();
			game.getBombs().add(new Bomb(game, this, map));
			game.getServer().sendToAll("actiondrop" + x + "," + y);
		}
		else
			game.getServer().send("nothing", player);
	}
	
	/**
	 * 
	 * @param action Set player action to be performed.
	 */
	public void setAction(String action) {
		this.action = action;
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
	 * @param b If player dropped bomb or not.
	 */
	public void setDropedBomb(boolean b) {
		this.dropedBomb = b;
	}
	/**
	 * 
	 * @return Player's username.
	 */
	public String getUsername() {
		return this.username;
	}
	/*
	 * Socket.
	 */
	public Socket getSocket() {
		return this.socket;
	}
	/**
	 * 
	 * @return Player character color.
	 */
	public int getColor() {
		return this.color;
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
	 * @return Player spped.
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
	 * 
	 * @param map Set player's map.
	 */
	public void setMap(Maze map) {
		this.map = map;
	}
	/**
	 * 
	 * @param b Revive or not.
	 */
	public void setAlive(boolean b) {
		this.alive = b;
	}
	/**
	 * 
	 * @param moving Set player moving.
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	/**
	 * Set player coordinates.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public void setCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
