package bomberman.menu.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import bomberman.game.logic.GameMultiplayer;
import bomberman.game.ui.TextShadow;
import bomberman.main.Main;
import bomberman.main.MainFrame;

/**
 * Class to represent multiplayer panel. Here the user joins the server and chooses a character to play with.
 * @author andre
 *
 */
public class MultiplayerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JPanel panel;
	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private TextShadow time;
	private int startX, startY, spacingY;
	private int fontSize;
	private String font = "Eight-Bit Madness";
	private int aux = 0;
	private boolean gameStart = false;
	private boolean exit = false;
	
	private ArrayList <JRadioButton> buttons = new ArrayList <JRadioButton>();

	/**
	 * 
	 * @param frame Create panel to the given frame.
	 */
	public MultiplayerPanel(JFrame frame) {
		this.frame = frame;
		this.panel = this;
		
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 3;
		spacingY = frame.getHeight() / 40;
		
		fontSize = (int)(frame.getWidth() * 50 / 1260);
		
		TextShadow info = new TextShadow("Select your character and press ready!", fontSize);
		info.setBounds(startX, startY, 2 * getWidth() / 3, getHeight() / 15);
	    info.place(this);
	    
	    int picSize = getWidth() / 20;
	    
	    ButtonGroup group = new ButtonGroup();
	    
	    for(int i = 1; i <= 4; i++) {
	    	aux = i;
	    	add(new JLabel() {
				private static final long serialVersionUID = 1L;
			{
				setBounds(startX + (aux - 1) * 2 * (picSize + spacingY), startY + 3 * spacingY, picSize, picSize);
				new ImageIcon(new ImageIcon("res/textures/player1.png").getImage().getScaledInstance(picSize, picSize, Image.SCALE_SMOOTH));
				setIcon(
						new ImageIcon(new ImageIcon("res/textures/player" + aux +".png").getImage().getScaledInstance(picSize, picSize, Image.SCALE_SMOOTH))
						);
				JLabel lbl = this;
				buttons.add(new JRadioButton(aux + ".") {
					private static final long serialVersionUID = 1L;
				{
				setBounds(lbl.getX() + picSize / 2 - 46 / 2, lbl.getY() + picSize, 46, 23);
				setContentAreaFilled(false);
				}});
	    	}});
	    	panel.add(buttons.get(i - 1));
	    	group.add(buttons.get(i - 1));
	    }
	    
	    JToggleButton ready = new JToggleButton("Ready");
		ready.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i;
				for(i = 0; i < 4; i++) {
					if(buttons.get(i).isSelected())
						break;
				}
				if(ready.isSelected())
					Main.client.sendData("setcharacter" + (i + 1));
				else
					Main.client.sendData("setcharacter0");
			}
		});
		ready.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
		ready.setBounds(startX, startY + picSize + 5 * spacingY, frame.getWidth() / 8, frame.getHeight() / 17);
		add(ready);
		
		time = new TextShadow("", (int)(frame.getWidth() * 40 / 1260));
		time.setBounds(ready.getX() + ready.getWidth() + 2 * spacingY, ready.getY(), frame.getWidth() / 2, frame.getHeight() / 17);
		time.place(this);

	    model = new DefaultTableModel();  
	    table = new JTable(model);
	    table.getTableHeader().setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 24 / 1260)));
	    table.setFont(table.getTableHeader().getFont());
	    model.addColumn("Username");
	    model.addColumn("Status");
	    model.addColumn("Character");
	    table.setRowHeight(getHeight() / (5 * 5));
		table.setBounds(startX, ready.getY() + ready.getHeight() + spacingY, getWidth() / 2, getHeight() / 5);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(table.getBounds());
		add(scrollPane);

		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit = true;
				gameStart = true;
				frame.getContentPane().removeAll();
				frame.getContentPane().repaint();
				((MainFrame)frame).addToBackground(new MenuPanel(frame));
			}
		});
		back.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
		back.setBounds(startX, getHeight() - getHeight() / 17 - 3 * spacingY, getWidth() / 4, getHeight() / 17);
		add(back);
		
		((MainFrame)frame).addToBackground(this);
		frame.repaint();
		frame.setVisible(true);
		setVisible(true);
		
		Thread thread = new Thread() {
			public void run(){
				while(!gameStart)
					Main.client.receiving(frame, panel);
			}
		{}};
		
		thread.start();
	}
	
	/**
	 * 
	 * @return Table where the users in the server are displayed.
	 */
	public DefaultTableModel getTable() {
		return this.model;
	}
	
	/**
	 * 
	 * @param time Change text time.
	 */
	
	public void setTime(String time) {
		this.time.setContent("Game starts in " + time);
	}
	
	/**
	 * 
	 * @param start Start game.
	 */
	public void setStart(boolean start) {
		this.gameStart = start;
	}
	
	/**
	 * 
	 * @return Get radio buttons information to get which characters the user has chosen.
	 */
	public ArrayList <JRadioButton> getButtons(){
		return this.buttons;
	}
	
}
