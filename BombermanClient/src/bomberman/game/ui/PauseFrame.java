package bomberman.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import bomberman.game.logic.Game;
import bomberman.main.*;
import bomberman.sound.Sound;

/**
 * Class to represent pause frame.
 * @author thebomberman
 *
 */
public class PauseFrame extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	private int fontSize;
	private int barWidth;
	private String font = "Eight-Bit Madness";
	private JButton resume, save, settings, exit, sGame, back;
	private JToggleButton effects, music, screen;
	private JTextField saveGame;
	private Game game;
	private GameFrame frame;
	private JPanel pane;

	/**
	 * Constructor for only singleplayer game.
	 * @param pause Pause button to disable.
	 * @param frame Game frame.
	 * @param game Game.
	 */
	public PauseFrame(JButton pause, GameFrame frame, Game game) {
		this.game = game;
		this.frame = frame;
		this.pane = this;
		width = (int) (game.getScale() * game.getGameWidth());
		height = (int) (game.getScale() * game.getGameHeight()); 
		
		setSize();
			
		setFocusable(true);
		setLayout(null);
		setBackground(new Color(0x60, 0x60, 0x60, 0xFF));
		
		resume = new JButton("Resume");
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				game.setPause(!game.isPaused());
				pause.setText("Pause");
				pause.setEnabled(true);
				frame.remove(pane);
				frame.add(frame.getPane());
				frame.repaint();
				frame.getPane().setVisible(true);
				frame.getPane().requestFocus();
			}
		});

		
		resume.setFont(new Font(font, Font.PLAIN, fontSize));
		resume.setBounds(width / 4, 1 * barWidth, width / 2, barWidth);
		add(resume);
		
		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				Sound.press.play();
				removeAll();
				repaint();
				
				saveGame = new JTextField();
				saveGame.setForeground(Color.BLACK);
				saveGame.setFont(new Font(font, Font.PLAIN, fontSize));
				saveGame.setBounds(width / 4, 3 * barWidth, width / 2, barWidth);
				add(saveGame);
				
				sGame = new JButton("Save");
				sGame.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) { 
						Sound.press.play();
						System.out.println(game.getMap().getMap());
						Main.client.sendData("savegame" + saveGame.getText() + "/" + game.getMap().getMap());
					}
				});
				if(!Main.serverRunning)
					sGame.setEnabled(false);
				sGame.setFont(new Font(font, Font.PLAIN, fontSize));
				sGame.setBounds(width / 4, 5 * barWidth, width / 2, barWidth);
				add(sGame);
				addBackButton();
			}});
		save.setFont(new Font(font, Font.PLAIN, fontSize));
		save.setBounds(width / 4, 3 * barWidth, width / 2, barWidth);
		add(save);
		
		settings = new JButton("Settings");
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				Sound.press.play();
				removeAll();
				repaint();
				effects = new JToggleButton("Sound effects " + (Main.sound ? "ON" : "OFF"));
				effects.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Main.sound = !Main.sound;
						Sound.press.play();
						effects.setText("Sound effects " + (Main.sound ? "ON" : "OFF"));
					}
				});
				effects.setSelected(Main.sound);
				effects.setFont(new Font(font, Font.PLAIN, fontSize));
				effects.setBounds(width / 4, 2 * barWidth, width / 2, barWidth);
				add(effects);
				
				music = new JToggleButton("Music " + (Main.music ? "ON" : "OFF"));
				music.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Sound.press.play();
						Main.music = !Main.music;
						music.setText("Music " + (Main.music ? "ON" : "OFF"));
						if(Main.music)
							Sound.theme.play();
						else
							Sound.theme.stop();
					}
				});
				music.setSelected(Main.music);
				music.setFont(new Font(font, Font.PLAIN, fontSize));
				music.setBounds(width / 4, 4 * barWidth, width / 2, barWidth);
				add(music);
				
				screen = new JToggleButton("Fullscreen " + (Main.fullScreen ? "ON" : "OFF"));
				screen.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Sound.press.play();
						Main.fullScreen = !Main.fullScreen;
						screen.setText("FullScreen " + (Main.fullScreen ? "ON" : "OFF"));
						game.changeSize();
					}
				});
				screen.setSelected(Main.fullScreen);
				screen.setFont(new Font(font, Font.PLAIN, fontSize));
				screen.setBounds(width / 4, 6 * barWidth, width / 2, barWidth);
				add(screen);
				addBackButton();
			}});
		settings.setFont(new Font(font, Font.PLAIN, fontSize));
		settings.setBounds(width / 4, 5 * barWidth, width / 2, barWidth);
		add(settings);
		
		exit = new JButton("Back to menu");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				if(Main.music) {
					Sound.theme.stop();
					Sound.menu.play();
				}
				game.stop();
				new MainFrame(frame);
			}
		});
		exit.setFont(new Font(font, Font.PLAIN, fontSize));
		exit.setBounds(width / 4, 7 * barWidth, width / 2, barWidth);
		add(exit);
		
	}
	/**
	 * Set button bounds according to the panel size.
	 */
	private void setButtonBounds() {
		resume.setFont(new Font(font, Font.PLAIN, fontSize));
		resume.setBounds(width / 4, 1 * barWidth, width / 2, barWidth);
		save.setFont(new Font(font, Font.PLAIN, fontSize));
		save.setBounds(width / 4, 3 * barWidth, width / 2, barWidth);
		settings.setFont(new Font(font, Font.PLAIN, fontSize));
		settings.setBounds(width / 4, 5 * barWidth, width / 2, barWidth);
		exit.setFont(new Font(font, Font.PLAIN, fontSize));
		exit.setBounds(width / 4, 7 * barWidth, width / 2, barWidth);
		effects.setFont(new Font(font, Font.PLAIN, fontSize));
		effects.setBounds(width / 4, 2 * barWidth, width / 2, barWidth);
		music.setFont(new Font(font, Font.PLAIN, fontSize));
		music.setBounds(width / 4, 4 * barWidth, width / 2, barWidth);
		screen.setFont(new Font(font, Font.PLAIN, fontSize));
		screen.setBounds(width / 4, 6 * barWidth, width / 2, barWidth);
		back.setFont(new Font(font, Font.PLAIN, fontSize));
		back.setBounds(width / 4, 8 * barWidth, width / 2, barWidth);
	}
	/**
	 * Set panel size according to the game size.
	 */
	private void setSize() {
		width = (int) (game.getScale() * game.getGameWidth());
		height = (int) (game.getScale() * game.getGameHeight()); 
		setPreferredSize(new Dimension(width, height));
		setBounds(width / 4, height / 4, width, height);
		
		if(!Main.fullScreen)
			setLocation(width / 4, height / 4);
		else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double h = screenSize.getHeight();
			double w = screenSize.getWidth();
			setLocation((int)(w / 2), (int)(h / 2));
		}
		fontSize = (int)(width * 16 / (624 / 2));
		barWidth = (int) (game.getScale() * game.getGameHeight() / 10);
	}
	/**
	 * Change size according to the game size.
	 */
	public void changeSize() {
		setSize();
		setButtonBounds();
		revalidate();
		repaint();
		frame.changeSize();
	}
	/**
	 * Method to add a back button.
	 */
	private void addBackButton() {
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				removeAll();
				repaint();
				add(resume);
				add(save);
				add(settings);
				add(exit);
			}
		});
		back.setFont(new Font(font, Font.PLAIN, fontSize));
		back.setBounds(width / 4, 8 * barWidth, width / 2, barWidth);
		add(back);
	}	
}
