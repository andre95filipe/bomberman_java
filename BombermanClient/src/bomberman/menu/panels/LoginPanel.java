package bomberman.menu.panels;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bomberman.game.ui.TextShadow;
import bomberman.main.Main;
import bomberman.main.MainFrame;
import bomberman.sockets.Client;
import bomberman.sound.Sound;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Color;

/**
 * THis class represents login panel, where user logs in.
 * @author andre
 *
 */
public class LoginPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField username;
	private JPasswordField password;
	private TextShadow errorLabel;
	private JPanel panel;
	private JFrame frame;
	
	private int startX, startY, divX, divY, spacing, fontSize, fieldSize;
	private String  font = "Eight-Bit Madness";
	
	/**
	 * 
	 * @param frame Create panel in the given frame.
	 */
	public LoginPanel(JFrame frame)  {
		panel = this;
		this.frame = frame;
		frame.getContentPane().removeAll();
		
		setBounds(frame.getBounds());
		setLocation(0, 0);
		setLayout(null);
		setOpaque(false);
		
		fontSize = (int)(frame.getWidth() * 40 / 1260);
		fieldSize = 5 * fontSize / 8;
		
		startX = frame.getWidth() / 30;
		startY = frame.getHeight() / 2;
		divX = frame.getWidth() / 3;
		divY = frame.getHeight() / 20;
		spacing = frame.getHeight() / 40;
		
		TextShadow userLabel = new TextShadow("Username", fontSize);
		userLabel.setBounds(startX, startY, divX, divY);
		userLabel.place(panel);
		
		username = new JTextField();
		username.setFont(new Font(font, Font.PLAIN, fieldSize));
		username.setBounds(startX, startY + 1 * divY, divX, divY);
		add(username);
		username.setColumns(10);
		
		TextShadow passLabel = new TextShadow("Password", fontSize);
		passLabel.setBounds(startX, startY + 2 * divY + spacing, divX, divY);
		passLabel.place(panel); 
		
		password = new JPasswordField();
		password.setFont(new Font(font, Font.PLAIN, fieldSize));
		password.setBounds(startX, startY + 3 * divY + spacing, divX, divY);
		add(password);
		
		JButton signIn = new JButton("Sign In");
		signIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				boolean usr = true;
				for(int i = 0; i < username.getText().length(); i++) {
					if(!((username.getText().charAt(i) >= 'A' && username.getText().charAt(i) <= 'Z') ||
						(username.getText().charAt(i) >= 'a' && username.getText().charAt(i) <= 'z') ||
						(username.getText().charAt(i) >= '0' && username.getText().charAt(i) <= '9'))) {
						usr = false;
						break;
					}
				}
				if(usr) {
					Main.client.sendData("checkuser" + username.getText() + "pass" + String.valueOf(password.getPassword()));
					Main.client.receiving(frame, panel);
				}
				else {
					errorLabel.setColor(Color.RED);
					setTextBorder(Color.RED);
					errorLabel.setContent("Username or password invalid!");
				}
			}
		});
		signIn.setFont(new Font(font, Font.PLAIN, fieldSize));
		signIn.setBounds(startX, startY + 5 * divY, (divX - spacing) / 2, 3 * divY / 2);
		add(signIn); 
		
		JButton register = new JButton("Register");
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.press.play();
				boolean usr = true;
				for(int i = 0; i < username.getText().length(); i++) {
					if(!((username.getText().charAt(i) >= 'A' && username.getText().charAt(i) <= 'Z') ||
						(username.getText().charAt(i) >= 'a' && username.getText().charAt(i) <= 'z') ||
						(username.getText().charAt(i) >= '0' && username.getText().charAt(i) <= '9'))) {
						usr = false;
						break;
					}
				}
				if(usr) {
					Main.client.sendData("registeruser" + username.getText() + "pass" + String.valueOf(password.getPassword()));
					Main.client.receiving(frame, panel);
				}
				else {
					errorLabel.setColor(Color.RED);
					setTextBorder(Color.RED);
					errorLabel.setContent("Username or password invalid!");
				}
			}
		});
		register.setBounds(startX + (divX + spacing) / 2, startY + 5 * divY, (divX - spacing) / 2, 3 * divY / 2);
		register.setFont(new Font(font, Font.PLAIN, fieldSize));
		add(register);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		exit.setFont(new Font(font, Font.PLAIN, fieldSize));
		exit.setBounds(startX, startY + 5 * divY + (3 * divY / 2) + spacing, (divX - spacing) / 2, 3 * divY / 2);
		add(exit);
		
		errorLabel = new TextShadow("", fontSize);
		errorLabel.setColor(Color.RED);
		errorLabel.setFont(new Font(font, Font.PLAIN, fieldSize));
		errorLabel.setBounds(startX + divX + spacing, startY + 1 * divY, 3 * divX, divY);
		errorLabel.place(panel);
		((MainFrame) frame).addToBackground(this);
		
		if(Main.client == null)
			connectServer();
	}
	
	/**
	 * 
	 * @return Error label. Used to how error messages to the user.
	 */
	public TextShadow getErrorLabel() {
		return errorLabel;
	}
	
	/**
	 * 
	 * @param color Change text border color to add error effect (red) or not.
	 */
	public void setTextBorder(Color color) {
		username.setBorder(BorderFactory.createLineBorder(color));
		password.setBorder(BorderFactory.createLineBorder(color));
	}
	
	/**
	 * Creates connection to server by loading a file with the ip and port of the corresponding server.
	 */
	public void connectServer() {
		BufferedReader file = null;
		String address = "";
		int port = 0;
		try {
			file = new BufferedReader(new FileReader("serverconfig.sv"));
			address = file.readLine();
			port = Integer.parseInt(file.readLine());
			file.close();
		} catch (IOException e) {
			System.out.println("Error loading server configurations");
		}
		
		Main.client = new Client("Client", address, port, frame);
	}
}