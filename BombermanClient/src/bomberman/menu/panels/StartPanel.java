package bomberman.menu.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import bomberman.main.MainFrame;

public class StartPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	private JFrame frame;
	private int x, y, velocity = 8, textWidth, textSelect = 0, fontSize;
	private String font = "Eight-Bit Madness";
	private FontMetrics fm;
	private ArrayList <String> text = new ArrayList <String>() {
		private static final long serialVersionUID = 1L;
	{
		add("LPRO - Bomberman");
		add("Developed by");
		add("Americo Duarte");
		add("Andre Silva");
		add("Pedro Ferreira");
	}};

	public StartPanel(JFrame frame){
		this.frame = frame;
		
		x = frame.getWidth();
		y = frame.getHeight() / 2;
		fontSize = (int)(frame.getWidth() * 150 / 1260);
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setBackground(Color.BLACK);
		setVisible(true);
		textWidth = 1000;
		timer = new Timer(1, this);
		timer.start();
	}
	
	public void paint(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font(font, Font.PLAIN, fontSize));
		fm = g.getFontMetrics(new Font(font, Font.PLAIN, fontSize));
		textWidth = fm.stringWidth(text.get(textSelect));
		g.drawString(text.get(textSelect), x, y);
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(x + textWidth <= frame.getWidth() && x >= 0) 
			velocity = (int) Math.ceil(getWidth() / 1366.0);
		if(x <= 0)
			velocity = (int) Math.ceil(getWidth() * 8 / 1366.0);
		if(x + textWidth < 0) {
			if(textSelect == 4) {
				timer.stop();
				frame.getContentPane().removeAll();
				frame.getContentPane().repaint();
				frame.setVisible(true);
				((MainFrame) frame).addToBackground(new EnterPanel(frame));
				frame.repaint();
			}
			else {
				textSelect++;
				x = frame.getWidth();
				y = frame.getHeight() / 2;
			}
		}
		x -= velocity;
		repaint();
	}
	
}
