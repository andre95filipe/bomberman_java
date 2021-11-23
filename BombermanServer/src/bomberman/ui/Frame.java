package bomberman.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bomberman.console.MessageConsole;
import bomberman.server.Server;


/**
 * Class that represents frame that will display the server console and the start/stop button.
 * @author andre
 *
 */
public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates frame.
	 */
	public Frame() {
		setTitle("Bomberman Server v1.0");
		initialize();
	}
	/**
	 * Initializes frame.
	 */
	private void initialize() {
		setLayout(null);
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Eight-Bit Madness", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(textArea);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w = screenSize.getWidth();
		double h = screenSize.getHeight();
        setBounds((int)(w / 4),(int)(h / 4),(int)(w / 2),(int)(h / 2));
        
        scrollPane.setBounds(0, 0, getWidth(), 3 * getHeight() / 4);
        setVisible(true);
		setResizable(false);
        getContentPane().add(scrollPane);
		MessageConsole mc = new MessageConsole(textArea);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(1000);
		
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				if(!Main.running) {
					start.setText("Stop");
					Main.server = new Server(6666);
					Main.running = true;
				}
				else {
					start.setText("Start");
					Main.server.closeConnection();
					Main.running = false;
				}
			}
		});
		start.setEnabled(!Main.running);
		start.setBounds(getWidth() / 4, 3 * getHeight() / 4, getWidth() / 2, 2 * (getHeight() / 4) / 3);
		start.setFont(new Font("Eight-Bit Madness", Font.PLAIN, 18));
		getContentPane().add(start);
	}
	
}
