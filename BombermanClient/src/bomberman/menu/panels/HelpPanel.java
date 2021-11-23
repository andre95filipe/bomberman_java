package bomberman.menu.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import bomberman.main.MainFrame;
import bomberman.sound.Sound;

/**
 * Class to represent panel where the help instructions are presented.
 * @author andre
 *
 */
public class HelpPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int startX, startY, spacingY;
	private int fontSize;
	
	/**
	 * 
	 * @param frame Creates panel in the given frame.
	 */
	public HelpPanel(JFrame frame) {
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 3;
		spacingY = frame.getHeight() / 40;
		
		fontSize = (int)(frame.getWidth() * 30 / 1260);

		JTextPane helpText = new JTextPane();
		JTextPane shadow = new JTextPane();

		helpText.setText("Hello Gamer! Welcome to BOMBERMAN, the game "
				+ "where you can use the strategy to annihilate your enemy. "
				+ "In this game you must drop bombs in strategic places, "
				+ "that may explode to kill the enemy. You can experience "
				+ "the game in Singleplayer mode, in which you play against"
				+ " bots to traine your skills. Otherwise, you can play against"
				+ " your friends in the Multiplayer mode, and prove that you "
				+ "are the best between them. For this game you may use the "
				+ "commands 'W','S','A','D', or the arrows to move your hero. "
				+ "To drop the bomb use the commnad 'Space' or 'CTRL'. Good "
				+ "luck, and enjoy the game!");
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_JUSTIFIED);
		StyleConstants.setFontFamily(attribs, "Eight-Bit Madness");
		StyleConstants.setFontSize(attribs, fontSize);
		StyleConstants.setForeground(attribs, Color.WHITE);
		helpText.setParagraphAttributes(attribs, true);
		helpText.setBounds(startX, startY, frame.getWidth() / 2, 2 * frame.getHeight() / 5);
		helpText.setOpaque(false);
		setVisible(true);

		StyleConstants.setForeground(attribs, Color.BLACK);
		shadow.setParagraphAttributes(attribs, true);
		shadow.setText(helpText.getText());
		shadow.setBounds(startX + 2, startY + 2, frame.getWidth() / 2, 2 * frame.getHeight() / 5);
		shadow.setOpaque(false);
		
		add(helpText);
		add(shadow);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				frame.getContentPane().removeAll();
				frame.getContentPane().repaint();
				((MainFrame)frame).addToBackground(new MenuPanel(frame));
			}
		});
		back.setFont(new Font("Eight-Bit Madness", Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
		back.setBounds(startX, startY + helpText.getHeight() + spacingY, frame.getWidth() / 4, frame.getHeight() / 17);
		add(back);
		
		setVisible(true);
		
	}
}