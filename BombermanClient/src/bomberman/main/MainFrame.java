package bomberman.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bomberman.menu.panels.EnterPanel;
import bomberman.menu.panels.MenuPanel;
import bomberman.menu.panels.SettingsPanel;

/**
 * Class to represent the main frame for all the menus.
 * @author andre
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width, height;
	private JLabel background;
	
	/**
	 * Constructor for when a game ends and returns to the menu panel.
	 * @param lastFrame Frame to dispose. In this case, it will be the Game frame.
	 */
	public MainFrame(JFrame lastFrame) {
		lastFrame.dispose();
		initialize("res/images/bgmenu.png");
		addToBackground(new MenuPanel(this));
		setVisible(true);
	}
	/**
	 * Contructor for new main frame.
	 */
	public MainFrame() {
		initialize("res/images/bginit.png");
		addToBackground(new EnterPanel(this));
		repaint();
		setVisible(true);
	}
	/**
	 * Main frame initializations according to the settings loadded from file in the Main class.
	 * @param path Path to the background to show.
	 */
	private void initialize(String path) {
		setTitle("Bomberman V1.0");
		
		ImageIcon icon = new ImageIcon("res/images/Bomberman-icon.png");
		setIconImage(icon.getImage());

		getContentPane().setLayout(null);
		
		setSize();
		
		setResizable(false);

		background = new JLabel();
		background.setBounds(this.getBounds());
		background.setLocation(0, 0);
		setBackground(path);
		
		addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	try {
            		Main.client.close();
            	} catch(Exception x) {

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
					}
	            	System.exit(0);
	            }
            	}
        });
	}
	/**
	 * Set frame size.
	 */
	public void setSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = screenSize.getWidth();
		double h = screenSize.getHeight();
		setUndecorated(Main.fullScreen);
		if(Main.fullScreen) {
			width = (int)(w);
			height = (int) (h);
			setBounds(0, 0, width, height);
		}
		else {
			width = (int) (3 * w / 4);
			height = (int) (3 * h / 4);
			setBounds((int)(w - width) / 2, (int)(h - height) / 2, width, height);
		}
		setVisible(true);
	}
	/**
	 * Change frame size.
	 * @param panel Current displayed panel to change size.
	 */
	public void changeSize(JPanel panel) {
		setSize();
		background.setVisible(false);
		panel.setVisible(false);
		background.setBounds(this.getBounds());
		setBackground("res/images/bgmenu.png");
		background.setLocation(0, 0);
		panel.setBounds(this.getBounds());
		panel.setLocation(0, 0);
		((SettingsPanel)panel).changeBounds();
		panel.setVisible(true);
		background.setVisible(true);
		setVisible(true);
		repaint();
	}
	
	/**
	 * 
	 * @param path Path to the background to be displayed in the frame.
	 */
	public void setBackground(String path) {
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		background.setIcon(imageIcon);
	}
	/**
	 * 
	 * @param panel Panel to add to background.
	 */
	public void addToBackground(JPanel panel) {
		background.removeAll();
		background.add(panel);
		add(background);
		background.setVisible(true);
	}
}
