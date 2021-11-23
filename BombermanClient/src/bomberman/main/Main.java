package bomberman.main;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import bomberman.sockets.Client;


/**
 * Class where the main method is at. This is the starting point.
 * @author andre
 *
 */
public class Main {

	public static Client client;
	public static boolean serverRunning = false;
	public static boolean sound = true, music = true, fullScreen = false;
	
	/**
	 * Loads game settings from file and starts new frame.
	 * @param args Main arguments.
	 */
	public static void main(String[] args) {
		
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("settings.sv"));
			Main.sound = Boolean.valueOf(file.readLine());
			Main.music = Boolean.valueOf(file.readLine());
			Main.fullScreen = Boolean.valueOf(file.readLine());
			file.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error loading settings.");
		}
		new MainFrame();
	}
}
