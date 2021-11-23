package bomberman.sockets;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bomberman.game.activeelements.Bomb;
import bomberman.game.activeelements.Explosion;
import bomberman.game.activeelements.Flames;
import bomberman.game.chat.Chat;
import bomberman.game.ui.GameFrame;
import bomberman.game.ui.GamePanel;
import bomberman.main.Main;
import bomberman.main.MainFrame;
import bomberman.menu.panels.ContinuePanel;
import bomberman.menu.panels.LoginPanel;
import bomberman.menu.panels.MenuPanel;
import bomberman.menu.panels.MultiplayerPanel;
import bomberman.menu.panels.RankingPanel;
import bomberman.sound.Sound;

/**
 * Class to represent the game client. This class is going to be used to interact with the server and change the game state according to received commands.
 * @author andre
 *
 */
public class Client {

	private Socket s_client;
	private int port;
	private String name, address;
	private InetAddress ip;
	private String received;
	private Thread send;
	private JFrame frame;
	private String username = "";
	private GameFrame gFrame;
	private BufferedReader in;
	
	/**
	 * Constructor to create a client.
	 * @param name Name given to the client
	 * @param address Client's address
	 * @param port Client's port
	 * @param frame Frame.
	 */
	public Client(String name, String address, int port, JFrame frame){
		try {
			this.frame = frame;
			this.name = name;
			this.address = address;
			this.port = port;
			s_client = new Socket(address, port);
			Main.serverRunning = true;
		} catch (IOException e) {
			int selectedOption = JOptionPane.showConfirmDialog(frame, "Error connecting to server.\nDo you want to continue?", "Server error", JOptionPane.YES_NO_OPTION); 
			if(selectedOption == JOptionPane.NO_OPTION) 
				System.exit(0);
			if (selectedOption == JOptionPane.YES_OPTION) {
				new MenuPanel(frame);
				Main.serverRunning = false;
			}
		}
		
		if(Main.serverRunning) {
			try {
				in = new BufferedReader(new InputStreamReader(s_client.getInputStream()));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Socket closed.");
			}	
		}
	}
	/**
	 * Send data to server.
	 * @param stringToSend String to send to server.
	 */
	public void sendData(String stringToSend) {
		send = new Thread("Sending") {
			public void run() {
				PrintWriter out = null;
				if(stringToSend.contains("checkuser"))
					username = stringToSend.substring(9, stringToSend.indexOf("pass"));
				try {
					out = new PrintWriter(s_client.getOutputStream(), true);
				} catch (IOException e) {
					System.out.println("Error sending data to server");;
				}
				out.println(stringToSend);
			}
		};
		send.start();
	}
	
	/**
	 * Method to check receiving information from server. This only runs once.
	 * Action are made to the game according to server commands and where the game is at, by checking the current frame and panel.
	 * @param frame Corresponding frame.
	 * @param panel Corresponding.
	 */
	public void receiving(JFrame frame, JPanel panel) {
		try {
			String s = received = in.readLine();
			if(panel instanceof LoginPanel) {
				if(s.contains("true")) {
					if(s.contains("login")) 
						new MenuPanel(frame);
					else {
						((LoginPanel)panel).getErrorLabel().setColor(Color.WHITE);
						((LoginPanel)panel).setTextBorder(Color.BLACK);
						((LoginPanel)panel).getErrorLabel().setContent("Register successfull!");
					}
				}
				else {
					((LoginPanel)panel).getErrorLabel().setColor(Color.RED);
					((LoginPanel)panel).setTextBorder(Color.RED);
					((LoginPanel)panel).getErrorLabel().setContent("Username or password invalid!");
				}
			}
			if(panel instanceof ContinuePanel) {
				if(((ContinuePanel) panel).isInit()) {
					((ContinuePanel) panel).setInit(false);
					String[] parts = s.split("/");
					((ContinuePanel)panel).getList().setListData(parts);
				}
				else {
					if(Main.music)
						Sound.menu.stop();
					new GameFrame(received, frame);
				}
			}
			if(panel instanceof RankingPanel) {
				String[] parts = s.split("/");
				for(int i = 0; i < parts.length; i++) {
					((RankingPanel)panel).getModel().addRow( 
					new Object[] {String.valueOf(i +1 ), 
								parts[i].split(",")[0], 
								parts[i].split(",")[1]});
				}
			}
			if(panel instanceof MenuPanel) {
				if(s.contains("occupied")) {
					JOptionPane.showMessageDialog(frame, "Server is occupied.\nTry again later.");
				}
				if(s.contains("added")) {
					new MultiplayerPanel(frame);
				}
			}
			if(panel instanceof MultiplayerPanel) {
				if(s.contains("remaining")) {
					((MultiplayerPanel)panel).setTime(s.replaceAll("remaining", ""));
				}
				if(s.contains("maze")) {
					((MultiplayerPanel)panel).setStart(true);
					new GameFrame(Main.fullScreen, frame, s.replace("maze/", ""), username);
				}
				if(s.contains("gamerslist")) {
					String[] parts = (s.replace("gamerslist/", "")).split("/");
					((MultiplayerPanel)panel).getTable().setRowCount(0);
					for(int i = 0; i < 4; i++)
						((MultiplayerPanel)panel).getButtons().get(i).setEnabled(true);
					for(int i = 0; i < parts.length; i++) {
						String[] info = parts[i].split(",");
						((MultiplayerPanel)panel).getTable().addRow(new Object[] {
								info[0], 
								new String(Integer.valueOf(info[1]) == 0 ? "Not ready" : "Ready"),
								new String(Integer.valueOf(info[1]) == 0 ? "?" : info[1])});
						if(Integer.valueOf(info[1]) > 0)
							((MultiplayerPanel)panel).getButtons().get(Integer.valueOf(info[1]) - 1).setEnabled(false);
					}
				}
			}
			if(panel instanceof GamePanel) {
				if(s.contains("addplayer")) {
					String[] players = s.split("/");
					for(int i = 0; i < players.length; i++) {
						String[] player = players[i].split("-");
						((GamePanel)panel).getMultiGame().addPlayer(player[0].replace("addplayer", ""),
																Integer.valueOf(player[1].replace("color", "")),
																Integer.valueOf(player[2].replace("coordinates", "").split(",")[0]),
																Integer.valueOf(player[2].replace("coordinates", "").split(",")[1]));
					}
					((GamePanel)panel).getMultiGame().start();
					
				}
				if(s.contains("/move")) {
					String[] command = s.split("/");
					int dirX = Integer.valueOf(command[1].replaceAll("move", "").split(",")[0]);
					int dirY = Integer.valueOf(command[1].replaceAll("move", "").split(",")[1]);
					((GamePanel)panel).getMultiGame().movePlayer(command[0], dirX, dirY);
				}
				if(s.contains("actiondrop")) {
					((GamePanel)panel).getMultiGame().getBombs().add(
							new Bomb(((GamePanel)panel).getMultiGame().getMap(),
									((GamePanel)panel).getMultiGame(),
									Integer.valueOf(s.replace("actiondrop", "").split(",")[0]),
									Integer.valueOf(s.replace("actiondrop", "").split(",")[1])));
				}
				if(s.contains("addflames")) {
					((GamePanel)panel).getMultiGame().bombExplode(Integer.valueOf(s.replace("addflames", "").split(",")[0]),
																	Integer.valueOf(s.replace("addflames", "").split(",")[1]),
																	s.replace("addflames", ""));
				}
				if(s.contains("removeflames")) {
					((GamePanel)panel).getMultiGame().removeFlames(Integer.valueOf(s.replace("removeflames", "").split(",")[0]),
																	Integer.valueOf(s.replace("removeflames", "").split(",")[1]));
				}
				if(s.contains("killplayer")) {
					((GamePanel)panel).getMultiGame().killPlayer(s.replace("killplayer", ""));
				}
				if(s.contains("gameover")) {
					((GamePanel)panel).getMultiGame().stop();
					frame.dispose();
					new Chat(s.replace("gameover", ""), username);
				}
				if(s.contains("canmov"))
					((GamePanel)panel).getMultiGame().getPlayers().get(username).setMoving(false);
				if(s.contains("place")) 
					((GamePanel)panel).getMultiGame().getPlayers().get(s.split("/")[0].replace("place","")).setCoord(
							Integer.valueOf(s.split("/")[1].split(",")[0]),
							Integer.valueOf(s.split("/")[1].split(",")[1]));
			}
			if(panel instanceof Chat) {
				if(s.contains("> ")) {
					((Chat)panel).getTextArea().append(s + "\n");
				}
				if(s.contains("chattime")) {
					((Chat)panel).getTitle().setContent(((Chat)panel).getText() + " " + s.replace("chattime", ""));
				}
				if(s.contains("stopchat")) {
					if(Main.music) {
						Sound.theme.stop();
						Sound.menu.play();
					}
					new MainFrame(frame);
				}
			}
		} catch(Exception exception){ 
			exception.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error socket connenction.");
			System.exit(0);
		}	
	}
	
	/**
	 * Method to close client.
	 */
	
	public void close() {
		try {
			s_client.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error closing socket connenction.");
		}
	}
	
	/**
	 * 
	 * @return Message received from server.
	 */
	
	public String getMessage() {
		return received;
	}

	/**
	 * 
	 * @return Client port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * 
	 * @return CLient name.
	 */

	public String getName() {
		return name;
	}

	
	/**
	 * 
	 * @return Client address.
	 */
	public String getAddress() {
		return address;
	}
	
	
	/**
	 * 
	 * @return CLient username.
	 */
	public String getUsername() {
		return this.username;
	}
}
