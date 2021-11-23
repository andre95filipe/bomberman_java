package bomberman.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;

import bomberman.game.logic.Game;
import bomberman.main.Main;

public class EndFrame extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	private int barWidth;
	private JButton restart, exit;
	private GameFrame frame;
	private JLabel score;
	private Game game;

	public EndFrame(GameFrame frame, Game game) {
		this.frame = frame;
		this.game = game;
		width = (int) (game.getScale() * game.getGameWidth() / 2);
		height = (int) (game.getScale() * game.getGameHeight() / 2); 
		
		setSize();
			
		setFocusable(true);
		setLayout(null);
		setBackground(new Color(0x60, 0x60, 0x60, 0xFF));
		setResizable(false);
		
		putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		pack();
		setTitle("Bomberman");
		
		barWidth = height / 10;
		
		score = new JLabel("Score " + game.getScore());
		restart = new JButton("Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Game game = new Game(frame, frame.getStatusBar(), 1);		
				game.start();
				}
		});
		restart.setFont(new Font("Eight-Bit Madness", Font.PLAIN, 22));
		restart.setBounds(width / 4, 1 * barWidth, width / 2, barWidth);
		add(restart);
		
		
		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		exit.setFont(new Font("Eight-Bit Madness", Font.PLAIN, 22));
		exit.setBounds(width / 4, 7 * barWidth, width / 2, barWidth);
		add(exit);
		frame.add(this);
	}
	
	private void setSize() {
		setPreferredSize(new Dimension(width, height));
		if(!Main.fullScreen)
			setLocation((int)(game.getWidth() * game.getScale() / 4), (int)(game.getHeight() * game.getScale() / 4));
		else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double h = screenSize.getHeight();
			double w = screenSize.getWidth();
			setLocation((int)((w - width) / 2), (int)((h - height) / 2));
		}
	}
	
	public void changeSize() {
		setSize();
		revalidate();
		repaint();
		frame.changeSize();
	}
		
}
