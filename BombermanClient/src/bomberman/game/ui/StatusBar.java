package bomberman.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

import bomberman.game.logic.Game;
import bomberman.game.logic.GameMultiplayer;
import bomberman.main.Main;
import bomberman.sound.Sound;

/**
 * Class to represent status bar.
 * @author thebomberman
 *
 */
public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	private int barWidth;
	private int fontSize;
	
	public TextShadow time, life, range, speed, score;
	private JButton pause;
	private PauseFrame pauseFrame;
	private GamePanel gamePane;
	private GameFrame frame;
	private Game game;
	private GameMultiplayer gameMulti;
	/**
	 * Create a status bar.
	 * @param frame Game frame.
	 * @param gp Game panel.
	 */
	public StatusBar(GameFrame frame, GamePanel gp) {
		this.frame = frame;	
		this.gamePane = gp;
		this.game = gamePane.getGame();

		setSize();
		
		setVisible(true);
		setFocusable(true);
		setBackground(new Color(0x60, 0x60, 0x60, 0xFF));
		setLayout(null);
		fontSize = (int)(width * 22 / 624);
		addLabels();
	}
	/**
	 * Create status bar for multiplayer game.
	 * @param frame Game frame.
	 * @param gp Game panel.
	 * @param username Main player username.
	 */
	public StatusBar(GameFrame frame, GamePanel gp, String username) {
		this.frame = frame;	
		this.gamePane = gp;
		this.gameMulti = gamePane.getMultiGame();

		if(Main.fullScreen) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			width = (int) screenSize.getWidth();
		}
		else 
			width = (int) (gameMulti.getScale() * gameMulti.getGameWidth());
		fontSize = (int)(width * 22 / 624);
		height = (int) (gameMulti.getScale() * gameMulti.getTileSize()); 
		setPreferredSize(new Dimension(width, height));
		setBounds(0, 0, width, height);
		
		setVisible(true);
		setFocusable(true);
		setBackground(new Color(0x60, 0x60, 0x60, 0xFF));
		setLayout(null);
		fontSize = (int)(width * 22 / 624);
	}
	/**
	 * Adds text contains the player status and remaining time.
	 */
	private void addLabels() {
		barWidth = width / 31;
		
		pause = new JButton("Pause");
		
		time = new TextShadow("Time ", fontSize);
		time.setBounds(1 * barWidth, height / 3, width / 5, height / 3);
		time.place(this);
		
		life = new TextShadow("Life ", fontSize);
		life.setBounds(7 * barWidth, height / 3, width / 5, height / 3);
		life.place(this);
		
		speed = new TextShadow("Speed ", fontSize);
		speed.setBounds(11 * barWidth, height / 3, width / 5, height / 3);
		speed.place(this);
		
		range = new TextShadow("Range ", fontSize);
		range.setBounds(16 * barWidth, height / 3, width / 5, height / 3);
		range.place(this);
		
		score = new TextShadow("Score ", fontSize);
		score.setBounds(21 * barWidth, height / 3, width / 5, height / 3);
		score.place(this);
		
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				game.setPause(!game.isPaused());
				pause.setText("Play");
				pause.setEnabled(false);
				frame.remove(frame.getPane());
				pauseFrame = new PauseFrame(pause, frame, gamePane.getGame());
				frame.add(pauseFrame);
			}
		});
		
		pause.setFont(new Font("Eight-Bit Madness", Font.PLAIN, (int)(width * 18 / 624)));
		pause.setBounds(26 * barWidth, height / 8, width / 7, 3 * height / 4);
		add(pause);
		gamePane.setStatBar(this);
		
	}
	/**
	 * Set bar size according to the game size.
	 */
	private void setSize() {
		if(Main.fullScreen) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			width = (int) screenSize.getWidth();
		}
		else 
			width = (int) (game.getScale() * game.getGameWidth());
		fontSize = (int)(width * 22 / 624);
		height = (int) (game.getScale() * game.getTileSize()); 
		setPreferredSize(new Dimension(width, height));
		setBounds(0, 0, width, height);
	}
	/**
	 * Change size according to game size.
	 */
	public void changeSize() {
		setSize();
		changeBounds();
		pauseFrame.changeSize();
	}
	
	/**
	 * Change button and labels bounds according to this panel size.
	 */
	private void changeBounds() {
		barWidth = width / 31;
		time.setFontSize(fontSize);
		time.setBounds(1 * barWidth, height / 3, width / 5, height / 3);
		life.setFontSize(fontSize);
		life.setBounds(7 * barWidth, height / 3, width / 5, height / 3);
		speed.setFontSize(fontSize);
		speed.setBounds(11 * barWidth, height / 3, width / 5, height / 3);
		range.setFontSize(fontSize);
		range.setBounds(16 * barWidth, height / 3, width / 5, height / 3);
		score.setFontSize(fontSize);
		score.setBounds(21 * barWidth, height / 3, width / 5, height / 3);
		pause.setFont(new Font("Eight-Bit Madness", Font.PLAIN, (int)(width * 18 / 624)));
		pause.setBounds(26 * barWidth, height / 8, width / 7, 3 * height / 4);
	}

}
