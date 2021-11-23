package bomberman.menu.panels;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bomberman.game.ui.GameFrame;
import bomberman.game.ui.TextShadow;
import bomberman.main.Main;
import bomberman.main.MainFrame;
import bomberman.sound.Sound;

/**
 * Class to represent the main menu.
 * @author andre
 *
 */
public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JPanel panel;
	private int startX, startY, divX, divY, spacingY, fontSize;
	private String  font = "Eight-Bit Madness";
	
	/**
	 * 
	 * @param frame Create panel in the given frame.
	 */
	public MenuPanel(JFrame frame) {
		this.frame = frame;
		this.panel = this;
		initialize();
	}
	
	/**
	 * All panel initializations.
	 */
	private void initialize() {
		clear();
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		fontSize = (int)(frame.getWidth() * 30 / 1260);

		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 3;
		divX = frame.getWidth() / 4;
		divY = frame.getHeight() / 17;
		spacingY = frame.getHeight() / 40;
		
		JButton contGame = new JButton("Continue Game");
		contGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				clear();
				((MainFrame)frame).addToBackground(new ContinuePanel(frame));
			}
		});
		contGame.setEnabled(Main.serverRunning);
		contGame.setFont(new Font(font, Font.PLAIN, fontSize));
		contGame.setBounds(startX, startY, divX, divY);
		add(contGame);
		
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				if(Main.music)
					Sound.menu.stop();
				new GameFrame(1, Main.fullScreen, frame);
			}
		});
		newGame.setFont(new Font(font, Font.PLAIN, fontSize));
		newGame.setBounds(startX, startY + 1 * divY + 1 * spacingY, divX, divY);
		add(newGame);
		
		JButton multiplayer = new JButton("Multiplayer");
		multiplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				Main.client.sendData("joinserver" + Main.client.getUsername());
				Main.client.receiving(frame, panel);
			}
		});
		multiplayer.setEnabled(Main.serverRunning);
		multiplayer.setFont(new Font(font, Font.PLAIN, fontSize));
		multiplayer.setBounds(startX, startY + 2 * divY + 2 * spacingY, divX, divY);
		add(multiplayer);
		
		JButton rankings = new JButton("Rankings");
		rankings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				clear();
				((MainFrame)frame).addToBackground(new RankingPanel(frame));
			}
		});
		rankings.setEnabled(Main.serverRunning);
		rankings.setFont(new Font(font, Font.PLAIN, fontSize));
		rankings.setBounds(startX, startY + 3 * divY + 3 * spacingY, divX, divY);
		add(rankings);
		
		JButton settings = new JButton("Settings");
		settings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				clear();
				((MainFrame)frame).addToBackground(new SettingsPanel(frame));
			}
		});
		settings.setFont(new Font(font, Font.PLAIN, fontSize));
		settings.setBounds(startX, startY + 4 * divY + 4 * spacingY, divX, divY);
		add(settings);
		
		JButton help = new JButton("Help");
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				clear();
				((MainFrame)frame).addToBackground(new HelpPanel(frame));
			}
		});
		help.setFont(new Font(font, Font.PLAIN, fontSize));
		help.setBounds(startX, startY + 5 * divY + 5 * spacingY, divX, divY);
		add(help);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		exit.setFont(new Font(font, Font.PLAIN, fontSize));
		exit.setBounds(startX, startY + 6 * divY + 6 * spacingY, divX, divY);
		add(exit);
		
		if(Main.serverRunning) {
			Canvas c = new Canvas();
			FontMetrics fm = c.getFontMetrics(new Font(font, Font.PLAIN, fontSize));
			TextShadow welcome = new TextShadow("Welcome " + Main.client.getUsername(), fontSize);
			int w = fm.stringWidth("Welcome " + Main.client.getUsername());
			welcome.setBounds(getWidth() - (startX / 2) - w, spacingY / 3, w, fm.getHeight());
			welcome.place(this);
			
			JButton logout = new JButton("Logout");
			logout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Sound.press.play();
					clear();
					Main.client.sendData("logout" + Main.client.getUsername());
					((MainFrame)frame).addToBackground(new LoginPanel(frame));
				}
			});
			logout.setFont(new Font(font, Font.PLAIN, 3 * fontSize / 4));
			logout.setBounds(getWidth() - (startX / 2) - (3 * divX / 8), 2 * spacingY, 3 * divX / 8, 3 * divY / 4);
			add(logout);
		}
		((MainFrame)frame).addToBackground(this);
		frame.repaint();
		frame.setVisible(true);
		setVisible(true);
	}
	
	/**
	 * Clears panel.
	 */
	public void clear() {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
	}
}
