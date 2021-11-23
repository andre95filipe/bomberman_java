package bomberman.menu.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import bomberman.main.Main;
import bomberman.main.MainFrame;

/**
 * Class to represent panel to continue a saved game. It is presented with a list of saved games.
 * @author andre
 *
 */
public class ContinuePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JList <String> list = new JList <String> ();
	private boolean init = true;
	private int startX, startY, spacingY;
	private int fontSize;
	private String font = "Eight-Bit Madness";
	
	/**
	 * 
	 * @param frame Creates panel on the given frame.
	 */
	public ContinuePanel(JFrame frame) {
		panel = this;

		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 3;
		spacingY = frame.getHeight() / 40;
		
		fontSize = (int)(frame.getWidth() * 30 / 1260);
		
		list.setBounds(startX, startY, frame.getWidth() / 2, 2 * frame.getHeight() / 7);
		list.setFont(new Font(font, Font.PLAIN, fontSize));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(list.getBounds());
		add(scrollPane);
		scrollPane.setVisible(true);
		
		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.client.sendData("load" + list.getSelectedValue());
				Main.client.receiving(frame, panel);
			}
		});
		
		load.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 40 / 1260)));
		load.setBounds(startX, startY + list.getHeight() + spacingY, frame.getWidth() / 4, frame.getHeight() / 17);
		add(load);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.getContentPane().repaint();
				((MainFrame)frame).addToBackground(new MenuPanel(frame));
			}
		});
		back.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 40 / 1260)));
		back.setBounds(startX, startY + list.getHeight() + 2 * spacingY + load.getHeight(), frame.getWidth() / 4, frame.getHeight() / 17);
		add(back);
		
		Thread thread = new Thread() {
			public void run(){
				Main.client.sendData("getsavedgames");
				Main.client.receiving(frame, panel);
			}
		{}};
		thread.start();
	}
	
	 /**
	  * 
	  * @return List where saved games are displayed.
	  */
	public JList<String> getList() {
		return list;
	}
	/**
	 * 
	 * @return If this panel has just been open.
	 */
	public boolean isInit() {
		return init;
	}
	/**
	 * 
	 * @param init Set initialize panel.
	 */
	public void setInit(boolean init) {
		this.init = init;
	}
}
