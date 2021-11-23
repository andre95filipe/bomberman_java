package bomberman.game.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bomberman.game.ui.TextShadow;
import bomberman.main.Main;
import bomberman.sound.Sound;


/**
 * Class to instantiate a new chat panel after multiplayer game ended.
 * @author thebomberman
 *
 */
public class Chat extends JPanel {
private static final long serialVersionUID = 1L;

	private String font = "Eight-Bit Madness";
	private JFrame frame;
	private JPanel panel;
	private JTextField message;
	private String text = "";
	private int startX, startY, spacingX, spacingY;
	private TextShadow title;
	private String username;
	private JTextArea textArea;
	private boolean on = true;

	
	/**
	 * Constructor creates a Chat panel.
	 * @param winner Writes who the winner of the game is.
	 * @param username The corresponding gamer username to be sent to the chat.
	 */
	public Chat(String winner, String username) {
		this.username = username;
		this.frame = new JFrame();
		this.panel = this;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = screenSize.getWidth();
		double h = screenSize.getHeight();
		frame.setBounds((int)(w - (3 * w / 4)) / 2, (int)(h - (3 * h / 4)) / 2, (int)(3 * w / 4), (int)(3 * h / 4));
		setBounds(frame.getBounds());
		
		ImageIcon icon = new ImageIcon("res/images/Bomberman-icon.png");
		frame.setIconImage(icon.getImage());
		
		text = "Winner is " + winner + "!              Remaining ";
		setFocusable(true);
		setLayout(null);
		setBackground(new Color(0x60, 0x60, 0x60, 0xFF));
		startX = getWidth() / 30;
		startY = getHeight() / 15;
		spacingX = getWidth() / 20;
		spacingY = getHeight() / 20;
		
		title = new TextShadow(text, (int)(frame.getWidth() * 60 / 1260));
		title.setBounds(startX, startY, getWidth() - 2 * startX, spacingY);
		title.place(this);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 40 / 1260)));
		textArea.setBounds(startX, startY + 2 * spacingY, getWidth() - 2 * startX, getHeight() / 2);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(textArea.getBounds());
		add(scroll);
		
		message = new JTextField();
		message.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 40 / 1260)));
		message.setBounds(startX, startY + 3 * spacingY + getHeight() / 2, getWidth() - 10 * startX - spacingX / 2, 2 * spacingY);
		add(message);
		message.setColumns(10);
		
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				Main.client.sendData("> " + username + ": " + message.getText() + "\n");
				message.setText("");
			}
		});
		send.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 40 / 1260)));
		send.setBounds(startX + message.getWidth() + spacingX / 2, message.getY(), 8 * startY - spacingX / 2, 2 * spacingY);
		add(send);	
		
		setVisible(true);
		frame.add(this);
		frame.repaint();
		frame.setVisible(true);
		
		Thread thread = new Thread() {
			public void run(){
				while(on) {
					Main.client.receiving(frame, panel);
				}
			}
		{}};
		thread.start();
	}
	
	
	/**
	 * 
	 * @return Get area where messages are going to be placed.
	 */
	public JTextArea getTextArea() {
		return this.textArea;
	}
	
	/**
	 * 
	 * @return Get Chat title to write remaining time.
	 */
	
	public TextShadow getTitle() {
		return this.title;
	}
	
	public String getText() {
		return this.text;
	}
}
