package bomberman.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import bomberman.game.activeelements.Gamer;
import bomberman.main.Main;


/**
 * 
 * Class to instantiate a keyboard listener.
 * 
 * @author andre
 *
 */
public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[120];  // saves state of keys on keyboard, pressed or released and the max key code is 87 (w key)
	private boolean up, down, left, right, drop, multiplayer = false;
	private Gamer gamer;
	
	/**
	 * 
	 * @param multiplayer If game is multiplayer or not.
	 */
	public Keyboard(boolean multiplayer) {
		this.multiplayer = multiplayer;
	}
	
	/**
	 * Updates keyboard state and activates/deactivates key flags.
	 */
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		drop = keys[KeyEvent.VK_CONTROL]  || keys[KeyEvent.VK_SPACE];
	}
	
	/**
	 * This method is called when key is pressed. The state of the pressed key is saved in an array, which will then be used in the update method.
	 * If multiplayer mode, a key corresponds to an action. THis action only happens if the gamer is not moving.
	 */
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
		if(multiplayer) {
			update();
			if(!gamer.isMoving()) {
				if(down) 
					Main.client.sendData("actiondown");
				if(up) 
					Main.client.sendData("actionup");
				if(left) 
					Main.client.sendData("actionleft");
				if(right) 
					Main.client.sendData("actionright");
			}
			if(drop)
				Main.client.sendData("actiondrop");
		}
	}
	/**
	 * Method run when key is released. Clears the array state of the corresponding key.
	 */

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	/**
	 * 
	 * @return Flag command up.
	 */

	public boolean isUp() {
		return up;
	}
	
	/**
	 * 
	 * @return Flag command down.
	 */

	public boolean isDown() {
		return down;
	}
	
	/**
	 * 
	 * @return Flag command left.
	 */

	public boolean isLeft() {
		return left;
	}
	
	/**
	 * 
	 * @return Flag command right.
	 */

	public boolean isRight() {
		return right;
	}
	
	/**
	 * 
	 * @return Flag command drop bomb.
	 */
	
	public boolean isDrop() {
		return drop;
	}
	
	/**
	 * Clears keyboard flags.
	 */
	
	public void clearInput() {
		up = down = left = right = drop = false;
	}
	
	/**
	 * 
	 * @param gamer Adds gamer to the keyboard. Only used when in multiplayer mode.
	 */
	
	public void addGamer(Gamer gamer) {
		this.gamer = gamer;
	}
}
