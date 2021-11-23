package bomberman.menu.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import bomberman.game.ui.TextShadow;
import bomberman.main.MainFrame;
import bomberman.sound.Sound;

/**
 * THis class represents the first panel, where user has to press enter to go to login panel.
 * @author andre
 *
 */
public class EnterPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private JPanel panel;
	private int fontSize;
	private Timer timer;
	private TextShadow enter;
	private boolean show = true;
	/**
	 * 
	 * @param frame Creates panel in the given frame.
	 */
	public EnterPanel(JFrame frame) {
		this.frame = frame;
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		this.panel = this;
		setFocusable(true);
        requestFocusInWindow();
        fontSize = (int)(frame.getWidth() * 80 / 1260);
        frame.addKeyListener(this);
		
		enter = new TextShadow("Press <Enter>", fontSize);
		enter.setBounds(frame.getWidth() / 10, frame.getHeight() / 4, 45 * frame.getWidth() / 50, 45 * frame.getHeight() / 50);
		enter.place(this);
		
		ActionListener taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	show = !show;
				if(show)
					enter.setContent("Press <Enter>");
				else
					enter.setContent("");
		    }
		};
		
		timer = new Timer(600, taskPerformer);
		timer.setRepeats(true);
		timer.start();
		Sound.menu.play();
	}
	
	/**
	 * Check if it was pressed enter key. If yes, creates a login panel.
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			Sound.press.play();
			timer.stop();
			removeAll();
			((MainFrame)frame).setBackground("res/images/bgmenu.png");
			LoginPanel login = new LoginPanel(frame);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
