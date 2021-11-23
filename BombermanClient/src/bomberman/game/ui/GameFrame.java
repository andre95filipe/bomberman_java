package bomberman.game.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import bomberman.game.logic.Game;
import bomberman.game.logic.GameMultiplayer;
import bomberman.main.Main;
import bomberman.sockets.Client;

/**
 * Class to represent the frame where it will be placed the game panel, the game and the status bar.
 * @author thebomberman
 *
 */
public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private GameFrame frame;
	private GamePanel gamePanel;
	private StatusBar statusBar;
	private Game game;
	private GameMultiplayer gameMulti;
	/**
	 * Constructor for new game.
	 * @param level Level to load
	 * @param fullScreen Fullscreen mode.
	 * @param frame Game frame.
	 */
	
	public GameFrame(int level, boolean fullScreen, JFrame frame) {
		frame.dispose();
		gamePanel = new GamePanel(this, level);
		statusBar = new StatusBar(this, gamePanel);
		ImageIcon icon = new ImageIcon("res/images/Bomberman-icon.png");
		setIconImage(icon.getImage());
		initialize();
	}
	/**
	 * Constructor for loading a saved game.
	 * @param map Formated String containing game state.
	 * @param frame Game frame.
	 */
	public GameFrame(String map, JFrame frame) {
		frame.dispose();
		gamePanel = new GamePanel(this, map);
		statusBar = new StatusBar(this, gamePanel);
		ImageIcon icon = new ImageIcon("res/images/Bomberman-icon.png");
		setIconImage(icon.getImage());
		initialize();
	}
	/**
	 * Constructor for multiplayer game.
	 * @param fullScreen Fullscreen.
	 * @param frame Game frame.
	 * @param s Formated String containing map
	 * @param username Main player username.
	 */
	
	public GameFrame(boolean fullScreen, JFrame frame, String s, String username) {
		frame.dispose();
		gamePanel = new GamePanel(s, this, username);
		statusBar = new StatusBar(this, gamePanel, username);
		ImageIcon icon = new ImageIcon("res/images/Bomberman-icon.png");
		setIconImage(icon.getImage());
		initialize(username);
	}
	
	/**
	 * Initialize game frame.
	 */
	
	private void initialize() {
		game = gamePanel.getGame();
		game.setStatusBar(statusBar);
		
		add(statusBar, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize();
		setTitle("Bomberman");
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	try {
            		Main.client.close();
            	} catch(Exception x) {
            		System.exit(0);
            	} finally {
	    			FileWriter fw;
					try {
						fw = new FileWriter("settings.sv", false);
						fw.write(String.valueOf(Main.sound));
						fw.write("\n");
						fw.write(String.valueOf(Main.music));
						fw.write("\n");
						fw.write(String.valueOf(Main.fullScreen));
	        			fw.close();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error saving settings.");
						System.exit(0);
					}
	            	System.exit(0);
	            }
            	}
        });
		
		game.start();
	}
	
	/**
	 * Initialize game frame for multiplayer game.
	 * @param username Username.
	 */
	
	private void initialize(String username) {
		gameMulti = gamePanel.getMultiGame();
		gameMulti.setStatusBar(statusBar);
		
		this.frame = this;
		
		add(statusBar, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		setVisible(false);
		
		if(Main.fullScreen) {
			setExtendedState(Frame.MAXIMIZED_BOTH);
			setUndecorated(true);
			setLocation(0, 0);
		}
		else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double w = screenSize.getWidth();
			setUndecorated(false);
			setLocation((int)(w - (gameMulti.getGameWidth() * gameMulti.getScale())) / 2, 0);
		}
		pack();
		
		setTitle("Bomberman");
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	try {
            		Main.client.close();
            	} catch(Exception x) {
            		System.exit(0);
            	} finally {
	    			FileWriter fw;
					try {
						fw = new FileWriter("settings.sv", false);
						fw.write(String.valueOf(Main.sound));
						fw.write("\n");
						fw.write(String.valueOf(Main.music));
						fw.write("\n");
						fw.write(String.valueOf(Main.fullScreen));
	        			fw.close();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Error saving settings.");
						System.exit(0);
					}
	            	System.exit(0);
	            }
            	}
        });
		
		Thread thread = new Thread() {
			public void run(){
				while(gameMulti.isRunning()) {
					Main.client.receiving(frame, gamePanel);
				}
			}
		{}};
		thread.start();
	}
	
	/**
	 * Set frame size.
	 */
	private void setSize() {
		setVisible(false);
		
		if(Main.fullScreen) {
			setExtendedState(Frame.MAXIMIZED_BOTH);
			setUndecorated(true);
			setLocation(0, 0);
		}
		else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double w = screenSize.getWidth();
			setUndecorated(false);
			setLocation((int)(w - (game.getGameWidth() * game.getScale())) / 2, 0);
		}
		pack();
	}
	
	/**
	 * Set frame size for multiplayer game.
	 * @param username Username
	 */
	private void setSize(String username) {
		setVisible(false);
		
		if(Main.fullScreen) {
			setExtendedState(Frame.MAXIMIZED_BOTH);
			setUndecorated(true);
			setLocation(0, 0);
		}
		else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double w = screenSize.getWidth();
			setUndecorated(false);
			setLocation((int)(w - (game.getGameWidth() * game.getScale())) / 2, 0);
		}
		pack();
	}
	
	/**
	 * Change frame size.
	 */
	public void changeSize() {
		dispose();
		setSize();
		revalidate();
		repaint();
		setVisible(true);
	}
	/**
	 * Change frame size for multiplayer game.
	 * @param username Username.
	 */
	public void changeSize(String username) {
		dispose();
		setSize(username);
		revalidate();
		repaint();
		setVisible(true);
	}
	/**
	 * 
	 * @return Corresponding status bar.
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}
	/**
	 * 
	 * @return Corresponding game pane
	 */
	public GamePanel getPane() {
		return this.gamePanel;
	}

}
