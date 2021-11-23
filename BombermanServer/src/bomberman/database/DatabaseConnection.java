package bomberman.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to represent connection to database. 
 * @author andre
 *
 */
public class DatabaseConnection {
	
	private Connection connection;
	
	private String username, password, saveTable;
	private boolean loginValid = false;
	private final String driver = "com.mysql.cj.jdbc.Driver",
						 url = "jdbc:mysql://localhost:3306/mydb?useTimezone=true&serverTimezone=UTC",
						 usr = "root",
						 psw = "root";
	/**
	 * Create database connection by setting the driver used for the connection.
	 */
	public DatabaseConnection() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, usr, psw);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error creating connection to database");
		}
	}
	
	/**
	 * Method to check if login is valid.
	 * @param user Given username.
	 * @param pass Given password.
	 * @return Login valid.
	 */
	
	public boolean checkLogin(String user, String pass) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error creating statement");
		}
		ResultSet results;
		
		try {
			results = statement.executeQuery("SELECT * FROM USERS WHERE username = '" + user + "' && password='" + pass + "';");
		} catch (SQLException e) {
			System.out.println("Error query username and password");
			return false;
		}
		
		try {
			while(results.next()) {
			 	username = results.getString("username");
			 	password = results.getString("password");
			 	saveTable = results.getString("saved_games");
			}
			results.close();
			statement.close();
		} catch (SQLException e) {
			System.out.println("Error getting info from database");
			System.exit(0);
		}
		
		try {
			
			if(username.equals(user) && password.equals(pass)) { 
				loginValid = true;
				return true;
				}
			else 
				return false;
		} catch(NullPointerException e) {
			return false;
		}
	}
	/**
	 * Register a username.
	 * @param user Given username.
	 * @param pass GIven password.
	 * @return If register is valid or not.
	 */
	public boolean register(String user, String pass) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("INSERT INTO USERS(username, password, saved_games, score) VALUES('" + user + "' , '" + pass + "', '" + user + "_sg', '0');");
			statement.execute();
			createSaveTable(user + "_sg");
			this.username = user;
			this.password = pass;
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	/**
	 * method to create a saved games table.
	 * @param table Name of the table.
	 */
	private void createSaveTable(String table) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("CREATE TABLE " + table + "(name VARCHAR(255), map VARCHAR(5000), PRIMARY KEY (name));");
			statement.execute();
		} catch (SQLException e) {
			System.out.println("Error creating saved games table");
		}
	}
	/**
	 * 
	 * @return Saved games.
	 */
	public String getSavedGames() {
		Statement statement = null;
		String savedGames = "";
		ResultSet results;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		results = null;
		try {
			results = statement.executeQuery("SELECT * FROM " + saveTable + ";");
		} catch (SQLException e) {
			return "";
		}
		
		try {
			while(results.next()) {
			 	savedGames = savedGames + results.getString("name") + "/";
			}
			results.close();
			statement.close();
		} catch (NullPointerException | SQLException e) {
			return "";
		}

		return savedGames;
	}
	/**
	 * Method to save game.
	 * @param name Name of the save.
	 * @param map Formated String with the state of the game.
	 * @return If game was successfully saved.
	 */
	public boolean saveGame(String name, String map) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("INSERT INTO " + saveTable + "(name, map) VALUES('" + name + "' , '" + map + "');");
			statement.execute();
		} catch (SQLException e) {
			try {
				statement = connection.prepareStatement("UPDATE " + saveTable + " SET map='" + map + "' WHERE name='" + name + "';");
				statement.execute();
			} catch (SQLException e1) {
				System.out.println("Error saving game");
				return false;
			}
			return true;
		}
		return true;
	}
	/**
	 * Load a saved game by it's name.
	 * @param game Name of the saved game.
	 * @return Formated String with the state of the corresponding game.
	 */
	public String getGame(String game) {
		Statement statement = null;
		ResultSet results;
		String line = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Error creating query for getting saved game");	
			return "";
		}
		results = null;
		try {
			results = statement.executeQuery("SELECT map FROM " + saveTable + " WHERE name='" + game + "'");
		} catch (SQLException e) {
			System.out.println("No saved games");
			return "";
		}
		
		try {
			while(results.next()) {
			 	line = results.getString("map");
			}
			results.close();
			statement.close();
		} catch (NullPointerException | SQLException e) {
			System.out.println("");
			return ""; 
		}
		return line;
	}
	/**
	 * 
	 * @return Highest score from the player.
	 */
	public int getScore() {
		Statement statement = null;
		ResultSet results;
		String score = "";
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		results = null;
		try {
			results = statement.executeQuery("SELECT score FROM users WHERE username='" + username + "'");
		} catch (SQLException e) {
			System.out.println("Error geting user score");
			return Integer.valueOf(score);
		}
		
		try {
			while(results.next()) {
			 	score = results.getString("score");
			}
			results.close();
			statement.close();
		} catch (NullPointerException | SQLException e) {
			return 0; 
		}
		return Integer.valueOf(score);
	}
	
	/**
	 * Replaces the highest score from player.
	 * @param score Score to place
	 * @return If score was successfully replaced.
	 */
	public boolean setScore(int score) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("UPDATE users SET score='" + score + "' WHERE username='" + username + "';");
			statement.execute();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @return If login is valid
	 */
	public boolean isLoginValid() {
		return this.loginValid;
	}
	
	/**
	 * CLose database connection.
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error closing database connection");
		}
	}
	/**
	 * 
	 * @return Formated String with rankings ordered by the highest score.
	 */
	public String getRankings() {
		Statement statement = null;
		String table = "";
		ResultSet results;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		results = null;
		try {
			results = statement.executeQuery("SELECT * from users ORDER BY score DESC;");
		} catch (SQLException e) {
			System.out.println("Error executing query");
			return "";
		}
		try {
			while(results.next()) 
				table = table + 
				results.getString("username") + "," + 
				results.getString("score") + "/";
			results.close();
			statement.close();
		} catch (NullPointerException | SQLException e) {
			System.out.println("Error reading results");
			return "";
		}
		return table;
	}
}
