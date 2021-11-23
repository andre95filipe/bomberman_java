package bomberman.multiplayer;

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
	
	public Enemy(Maze map, int n) {
		this.animation = 0;
		this.map = map;
		this.x = map.getCoordinates('e').get(n)[0] * 16;
		this.y = map.getCoordinates('e').get(n)[1] * 16;
		map.addEnemy(this);
	}
	
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
				aux++;
				if(aux == 3) aux = 0;
			}
		}
		if(alive && die) {
			if(aux == 0) {
				initTime = System.currentTimeMillis();
				aux++;
			}
			else {
				currTime = System.currentTimeMillis() - initTime;
				if(currTime > 250) {
					initTime = System.currentTimeMillis();
					if(aux < 3) {
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

	public void die() {
		if(!die) {
			aux = 0;
			map.getElements().get("enemies").remove(this);
			die = true;
		}
	}
}
