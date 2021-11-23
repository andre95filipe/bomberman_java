package bomberman.menu.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import bomberman.game.ui.TextShadow;
import bomberman.main.Main;
import bomberman.main.MainFrame;
import bomberman.sound.Sound;

/**
 * Class to represent the settings panel, where the users can change the settings.
 * @author andre
 *
 */
public class SettingsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JPanel panel;
	private int startX, startY, spacingY, spacingX;
	private int fontSize;
	private String font = "Eight-Bit Madness";
	private TextShadow sfx, mfx, fs;
	private JToggleButton sound, music, fullScreen;
	private JButton back;
	/**
	 * 
	 * @param frame Create panel in the given frame.
	 */
	public SettingsPanel(JFrame frame) {
		this.frame = frame;
		this.panel = this;
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		fontSize = (int)(frame.getWidth() * 70 / 1260);
		
		sfx = new TextShadow("Sound effects", fontSize);
		sfx.place(this);
		
		sound = new JToggleButton((Main.sound ? "ON" : "OFF"));
		sound.setSelected(Main.sound);
		sound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Main.sound = !Main.sound;
				Sound.press.play();
				sound.setText((Main.sound ? "ON" : "OFF"));
			}
		});
		add(sound);
	
		mfx = new TextShadow("Music", fontSize);
		add(mfx);
		mfx.place(this);
		
		music = new JToggleButton((Main.music ? "ON" : "OFF"));
		music.setSelected(Main.music);
		music.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Sound.press.play();
				Main.music = !Main.music;
				music.setText((Main.music ? "ON" : "OFF"));
				if(Main.music) 
					Sound.menu.play();
				else
					Sound.menu.stop();
			}
		});
		add(music);
		
		fs = new TextShadow("Full Screen", fontSize);
		fs.place(this);
		
		fullScreen = new JToggleButton((Main.fullScreen ? "ON" : "OFF"));
		fullScreen.setSelected(Main.fullScreen);
		fullScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Sound.press.play();
				Main.fullScreen = !Main.fullScreen;
				frame.dispose();
				((MainFrame)frame).changeSize(panel);
				fullScreen.setText((Main.fullScreen ? "ON" : "OFF"));
			}
		});
		add(fullScreen);
		
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sound.press.play();
				frame.getContentPane().removeAll();
				frame.getContentPane().repaint();
				((MainFrame)frame).addToBackground(new MenuPanel(frame));
			}
		});
		add(back);
		changeBounds();
	}
	
	/**
	 * CHange button bounds according to the panel size.
	 */
	public void changeBounds() {
		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 3;
		spacingY = frame.getHeight() / 8;
		spacingX = frame.getWidth() / 2;
		
		fontSize = (int)(frame.getWidth() * 70 / 1260);
		
		sfx.setBounds(startX, startY, frame.getWidth() / 2, frame.getHeight() / 15);
		sfx.setFontSize(fontSize);
		sound.setBounds(startX + spacingX, startY, frame.getWidth() / 4, frame.getHeight() / 17);
		sound.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
		mfx.setBounds(startX, startY + spacingY, frame.getWidth() / 2, frame.getHeight() / 15);
		mfx.setFontSize(fontSize);
		music.setBounds(startX + spacingX, startY + spacingY, frame.getWidth() / 4, frame.getHeight() / 17);
		music.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
		fs.setBounds(startX, startY + 2 * spacingY, frame.getWidth() / 2, frame.getHeight() / 15);
		fs.setFontSize(fontSize);
		fullScreen.setBounds(startX + spacingX, startY + 2 * spacingY, frame.getWidth() / 4, frame.getHeight() / 17);
		fullScreen.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
		back.setBounds(startX, startY + 4 * spacingY, frame.getWidth() / 4, frame.getHeight() / 17);
		back.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
	}
}
