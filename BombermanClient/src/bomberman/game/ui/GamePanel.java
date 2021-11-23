package bomberman.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

import bomberman.game.logic.Game;
import bomberman.game.logic.GameMultiplayer;


/**
 * Class to represent the panel where the game is going to be presented.
 * @author andre
 *
 */
public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private StatusBar statusBar;
	private GameFrame frame;
	private Game game;
	private GameMultiplayer gameMulti;
	
	/**
	 * Contructor for new game.
	 * @param frame Game Frame.
	 * @param level Level to load.
	 */
	public GamePanel(GameFrame frame, int level) {
		this.frame = frame;
		game = new Game(frame, statusBar, level);
		initialize(game, frame);
	}
	/**
	 * Constructor for saved game.
	 * @param frame Game frame.
	 * @param map Formated String with game state.
	 */
	public GamePanel(GameFrame frame, String map) {
		game = new Game(frame, statusBar, map);
		initialize(game, frame);
	}
	
	/**
	 * Constructor for multiplayer game.
	 * @param s Formated String with map.
	 * @param frame Game frame.
	 * @param username Username.
	 */
	public GamePanel(String s, GameFrame frame, String username) {
		this.frame = frame;
		gameMulti = new GameMultiplayer(frame, statusBar, s, username);
		initialize(gameMulti, frame, username);
	}
	
	/**
	 * Initializations for singleplayer game.
	 * @param game Game.
	 * @param frame Game frame.
	 */
	private void initialize(Game game, GameFrame frame) {
		setBackground(Color.BLACK);
		add(game);
		game.setVisible(true);
		setVisible(true);
		setFocusable(true);
		game.setPanel(this);
	}
	
	/**
	 * Initializations for multiplayer game.
	 * @param game Game.
	 * @param frame Game frame.
	 * @param username Username.
	 */
	
	private void initialize(GameMultiplayer game, GameFrame frame, String username) {
		setBackground(Color.BLACK);
		add(game);
		game.setVisible(true);
		setVisible(true);
		setFocusable(true);
		game.setPanel(this);
	}
	/**
	 * Set panel size according to the game size.
	 */
	private void setSize() {
		setBounds(0, (int)(game.getTileSize() * game.getScale()), (int)(game.getGameWidth() * game.getScale()), (int)(game.getGameHeight() * game.getScale()));
	}
	
	/**
	 * Change panel size according to the game size.
	 */
	public void changeSize() {
		setSize();
		revalidate();
		repaint();
		statusBar.changeSize();
	}
	/**
	 * 
	 * @return Game
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * 
	 * @return Multiplayer game.
	 */
	public GameMultiplayer getMultiGame() {
		return this.gameMulti;
	}
	/**
	 * 
	 * @param b Status bar.
	 */
	public void setStatBar(StatusBar b) {
		this.statusBar = b;
	}
	/**
	 * 
	 * @param game Set corresponding game.
	 */
	public void setGame(Game game) {
		remove(this.game);
		this.game = game;
		add(game);
		game.start();
	}
	
}
