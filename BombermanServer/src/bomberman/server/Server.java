package bomberman.server;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Timer;

import bomberman.database.DatabaseConnection;
import bomberman.multiplayer.Game;
import bomberman.multiplayer.Player;

/**
 * Class to represent the server. This server is used to interact and respond to clients by sending commands according to received messages.
 * Establishes connection between client and database. It runs on a thread.
 * @author andre
 *
 */
public class Server implements Runnable {
	private boolean open, countdown = false, gameRunning = false;
	private static int time = 60;
	
	private ServerSocket s_server;
	private ArrayList <Socket> clients = new ArrayList<>();
	private HashMap <Socket, HashMap <String, Integer>> gamers = new HashMap <Socket, HashMap <String, Integer>>();
	private Server server;
	private Game game;
	private int readyPlayers = 0;
	private HashMap <Socket, String> usersOn = new HashMap <Socket, String>();
	private int chatTime = 60;
	
	private Thread run, handleClients, send, sendToAll, client;
	
	private Timer timer;
	
	/**
	 * Creates server.
	 * @param port Creates server at the given port.
	 */
	public Server(int port) {
		try {
			this.server = this;
			s_server = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Error opening server.");
		}
		
		run = new Thread(this, "Server");
		run.start();
		
		ActionListener taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	sendToAll("remaining" + (--time));
		    	System.out.println("Time remaining: " + time);
		    	String gamersList = "";
		    	for (Socket socket: gamers.keySet()) {
		    	    Map.Entry <String, Integer> gamer = gamers.get(socket).entrySet().iterator().next();
		    	    gamersList += gamer.getKey() + "," + gamer.getValue() + "/";
		    	}
		    	sendToAll("gamerslist/" + gamersList);
		    	if(gamers.size() == 0) {
		    		timer.stop();
		    		time = 60;
		    		System.out.println("Multiplayer game shutdown.");
		    	}
		    	if(time == 0) {
		    		timer.stop();
		    		time = 60;
		    		for (Socket socket: gamers.keySet()) {
			    	    Map.Entry <String, Integer> gamer = gamers.get(socket).entrySet().iterator().next();
			    	    if(gamer.getValue() == 0)
			    	    	gamers.remove(socket);
			    	}
		    		if(gamers.size() > 1) {
			    		game = new Game(server, gamers);
			    		gameRunning = true;
			    		long init = System.currentTimeMillis();
			    		while(System.currentTimeMillis() - init > 3000);
			    		server.sendToAll("maze/" + game.getMaze().getTiles());
		    		}
		    		if(gamers.size() == 1) {
		    			timer.start();
		    		}
		    	}
		    }
		};
		
		timer = new Timer(1000, taskPerformer);
		timer.setRepeats(true);
	}
	/**
	 * Method that keeps handling clients when the server is running.
	 */
	
	public void run() {
		open = true;
		System.out.println("Server started.");
		handleClients();
	}

	/**
	 * Method to handle clients and create a thread for each one of them, so the server can handle received infor from each client.
	 */
	public void handleClients() {
		handleClients = new Thread("HandleClients") {
			public void run() {
				while(open) {
					try {
						final Socket s_client = s_server.accept();
						client = new Thread(String.valueOf(s_client.getPort())) {
							public void run(){
								System.out.println("Connection established with client " + s_client.getPort());
								DatabaseConnection database = new DatabaseConnection();
								clients.add(s_client);
								receiving(s_client, database);
							}
						{}};
						client.start();
					} catch (IOException e) {
						try{ 
							System.out.println("Closing server...");
							s_server.close();
						} catch(IOException ex){ 
							System.out.println("Error closing server.");
						}
						System.out.println("Server closed.");
					}
				}
			}
		};
		
		handleClients.start();
	}	
	
	/**
	 * Method to check received info from clients. According to the received info, the server responds with a predifined command.
	 * @param client Client to check received info.
	 * @param database The corresponding database connection.
	 */
	@SuppressWarnings("serial")
	public void receiving(Socket client, DatabaseConnection database) {
		System.out.println("Waiting for info from client " + client.getPort() +".");
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Error getting buffers.");
		}
				
		while(open){
			try{
				String s = in.readLine();
				System.out.println("Received info from client " + client.getPort() + ":");
				System.out.println(s);
				
				if(s.contains("check") && s.contains("user") && s.contains("pass")) {
					boolean b = database.checkLogin(s.substring(s.indexOf("user") + 4, s.indexOf("pass")), 
										s.substring(s.indexOf("pass") + 4, s.length()));
					for(Socket sock: usersOn.keySet()) {
						if(usersOn.get(sock).equals(s.substring(s.indexOf("user") + 4, s.indexOf("pass"))))
							b = false;
					}
					System.out.println("Checking loging for username " +
							s.substring(s.indexOf("user") + 4, s.indexOf("pass")) + "...");
					System.out.println(b ? "Login valid." : "Login not valid.");
					send("login" + String.valueOf(b), client);
					if(b) 
						usersOn.put(client, s.substring(s.indexOf("user") + 4, s.indexOf("pass")));
				}
				
				if(s.contains("register") && s.contains("user") && s.contains("pass")) {
					boolean b = database.register(s.substring(s.indexOf("user") + 4, s.indexOf("pass")), 
										s.substring(s.indexOf("pass") + 4, s.length()));
					System.out.println("Registering username " +
							s.substring(s.indexOf("user") + 4, s.indexOf("pass")) + "...");
					System.out.println(b ? "Regist valid." : "Regist not valid.");
					send("register" + String.valueOf(b), client);
				}
				
				if(s.contains("getsavedgames")) {
					System.out.println("Getting list of saved games...");
					String list = database.getSavedGames();
					send(list, client);
					System.out.println("List of saved games sent successfully.");
				}
				
				if(s.contains("load")) {
					System.out.println("Loading saved game...");
					String save = s.substring(s.indexOf("load") + 4, s.length());
					String map = database.getGame(save);
					send(map,client);
					System.out.println("Saved game sent successfully.");
				}
				
				if(s.contains("savegame")) {
					System.out.println("Saving game...");
					String[] saveGame = s.split("/");
					if(Integer.valueOf(saveGame[3]) > database.getScore()) 
						database.setScore(Integer.valueOf(saveGame[3]));
					String sv = saveGame[0].replaceAll("savegame", "");
					String map = s.replaceAll(saveGame[0] + "/", "");
					database.saveGame(sv, map);
					System.out.println("Game saved successfully.");
				}
				
				if(s.contains("getrankings")) {
					System.out.println("Getting ranking...");
					send(database.getRankings(), client);
					System.out.println("Rankings sent successfully.");
				}
				
				if(s.contains("logout")) {
					Socket sk = null;
					for(Socket sock: usersOn.keySet()) {
						if(usersOn.get(sock).equals(s.replace("logout", "")))
							sk = sock;
					}
					usersOn.remove(sk);
					System.out.println("Username " + s.replace("logout", "") + " logged out");
				}
				
				if(s.contains("joinserver")) {
					System.out.println("Asking to join server...");
					if(!gameRunning) {
						send("added", client);
						gamers.put(client, new HashMap <String, Integer>(){{
							put(s.replaceAll("joinserver", ""), 0);
						}});
						System.out.println("Added client successfully");
						if(!countdown) {
							System.out.println("Started countdown");
							timer.start();
						}
					}
					else {
						send("occupied", client);
					}
				}
				if(s.contains("exitserver")) {
					System.out.println("Client " + client.getPort() + " exit from multiplayer");
					gamers.remove(client);
					System.out.println("Client " + client.getPort() + " removed from gamers list");
				}
				
				if(s.contains("setcharacter")) {
					gamers.get(client).entrySet().iterator().next().setValue(Integer.valueOf(s.replace("setcharacter", "")));
				}
				
				if(s.contains("sendplayers")) {
					readyPlayers++;
					if(readyPlayers == game.getPlayers().size()) {
						readyPlayers = 0;
						String info = "";
						for(Player player : game.getPlayers().values()) {
							info = info + "addplayer" + player.getUsername() + "-" +
											"color" + player.getColor() + "-" +
											"coordinates" + player.getX() + "," + player.getY() + "/";
						}
						sendToAll(info);
					}
				}
				
				if(s.contains("action")) {
					if(!s.contains("drop")) 
						game.movePlayer(client, s.replace("action", ""));	
					else
						game.getPlayers().get(client).dropBomb(client);
				}
					
				if(s.contains("arrive")) {
					System.out.println("Server player coords: " +
							game.getPlayers().get(client).getX() + ", " +
							game.getPlayers().get(client).getY());
					
					System.out.println("Client player coords: " +
							Integer.valueOf(s.replace("arrive", "").split(",")[0]) + ", " +
							Integer.valueOf(s.replace("arrive", "").split(",")[1]));
					
					if(game.getPlayers().get(client).getX() == Integer.valueOf(s.replace("arrive", "").split(",")[0]) &&
					game.getPlayers().get(client).getY() == Integer.valueOf(s.replace("arrive", "").split(",")[1])) {
						System.out.println("Sending can move");
						send("canmov", client);
					}
					else {
						System.out.println("Sendingto place player");
						sendToAll("place" + game.getPlayers().get(client).getUsername() + "/" + 
								game.getPlayers().get(client).getX() + "," + 
								game.getPlayers().get(client).getY());
					}
				}
				if(s.contains("> ")) {
					sendToAll(s);
				}
				
			} catch(Exception e){ 
				try{ 
					e.printStackTrace();
					gameRunning = false;
					usersOn.remove(client);
					gamers.remove(client);
					client.shutdownOutput();
					client.close();
					in.close();
					out.close();
					clients.remove(client);
					System.out.println("Error receiving info from client " + client.getPort() + ".");
					System.out.println("Connection with client " + client.getPort()+" terminated.");
					return;
				} catch(Exception exception1){ 
					System.out.println("Error closing connection with client " + client.getPort() + ".");
				}
			}			
		}		
	}
	
	/**
	 * Method to start chat.
	 */
	public void chatStart() {
		ActionListener taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	sendToAll("chattime" + --chatTime);
		    	if(chatTime == 0) {
		    		chatTime = 60;
		    		sendToAll("stopchat");
		    		for(Socket socket: gamers.keySet())
		    			gamers.remove(socket);
		    	}
		    }
		};
		
		timer = new Timer(1000, taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}
	
	/**
	 * Method to send String to a client.
	 * @param toSend String to send.
	 * @param client Client to send String.
	 */
	public void send(String toSend, Socket client) {
		try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            System.out.println(toSend);
            out.println(toSend);
        } catch(Exception e) {
            System.out.println("Socket is closed.");
        }
	}	
	
	/**
	 * Send String to all gamers. THis method is used in multiplayer mode for broadcasting.
	 * @param toSend String to send.
	 */
	public void sendToAll(String toSend) {
		for (Socket s : gamers.keySet())
			send(toSend, s);
	}
	/**
	 * Close server.
	 */
	public void closeConnection(){
		open = false;
		try{ 
			s_server.close();
		} catch(IOException e){ 
			System.out.println("Error closing server.");
		}
		
		for(int i = 0; i < clients.size();i++){
			try{ 
				clients.get(i).close();
			} catch(Exception exception){ 
				exception.printStackTrace(); 
			}
		}
		clients.clear();
	}
	/**
	 * Start game.
	 * @param gameRunning start game.
	 */
	public void setGamerRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}
}