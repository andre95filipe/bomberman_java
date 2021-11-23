package bomberman.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Class to create text with shadow effect.
 * @author thebomberman
 *
 */
public class TextShadow extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private JLabel label, shadow;
	
	/**
	 * Create text with shadow
	 * @param text Text to add.
	 * @param size Size of the font.
	 */
	public TextShadow(String text, int size) {
		label = new JLabel();
		shadow = new JLabel();
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Eight-Bit Madness", Font.PLAIN, size));
		shadow.setForeground(Color.BLACK);
		shadow.setFont(label.getFont());
		setContent(text);
	}
	
	/**
	 * Set text bounds.
	 */
	@Override
	public void setBounds(int x, int y, int width, int height){
		this.label.setBounds(x, y, width, height);
		this.shadow.setBounds(x + 2, y + 2, width, height);
	}
	/**
	 * 
	 * @param panel Place text in this panel.
	 */
	public void place(JPanel panel) {
		panel.add(label);
		panel.add(shadow);
	}
	/**
	 * 
	 * @param text Set the text to present.
	 */
	public void setContent(String text) {
		label.setText(text);
		shadow.setText(text);
	}
	/**
	 * 
	 * @param size Set the corresponding font size.
	 */
	public void setFontSize(int size) {
		label.setFont(new Font("Eight-Bit Madness", Font.PLAIN, size));
		shadow.setFont(new Font("Eight-Bit Madness", Font.PLAIN, size));
	}
	
	/**
	 * 
	 * @param color Set text color.
	 */
	public void setColor(Color color) {
		label.setForeground(color);
	}
}
