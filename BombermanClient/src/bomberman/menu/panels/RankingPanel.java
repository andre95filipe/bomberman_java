package bomberman.menu.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import bomberman.main.Main;
import bomberman.main.MainFrame;


/**
 * Class to represent the panel where the rankings are displayed.
 * @author andre
 *
 */
public class RankingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JPanel panel;
	private JTable table;
	private DefaultTableModel model;
	private int startX, startY, spacingY;
	private int fontSize;
	private String font = "Eight-Bit Madness";
	/**
	 * 
	 * @param frame Create panel in the given frame.
	 */
	public RankingPanel(JFrame frame) {
		this.panel = this;

		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 3;
		spacingY = frame.getHeight() / 40;
		
		fontSize = (int)(frame.getWidth() * 30 / 1260);
		
		model = new DefaultTableModel();
	    JTable table = new JTable(model){
	        private static final long serialVersionUID = 1L;
	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
	    model.addColumn("Position");
	    model.addColumn("Username");
	    model.addColumn("Score");
	    table.getTableHeader().setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
	    table.setFont(new Font(font, Font.PLAIN, (int)(frame.getWidth() * 30 / 1260)));
	    table.setRowHeight(spacingY * 2);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(startX, startY, frame.getWidth() / 2, 2 * frame.getHeight() / 5);
		add(scrollPane);
		scrollPane.setVisible(true);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.getContentPane().repaint();
				((MainFrame)frame).addToBackground(new MenuPanel(frame));
			}
		});
		back.setFont(new Font(font, Font.PLAIN, fontSize));
		back.setBounds(startX, startY + scrollPane.getHeight() + 2 * spacingY, frame.getWidth() / 4, frame.getHeight() / 17);
		add(back);
		
		Main.client.sendData("getrankings");
		Main.client.receiving(frame, this);
	}
	
	/**
	 * 
	 * @return Table where the ranking are displayed.
	 */
	public DefaultTableModel getModel() {
		return this.model;
	}
}
