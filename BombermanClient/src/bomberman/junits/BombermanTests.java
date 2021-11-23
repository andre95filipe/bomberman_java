package bomberman.junits;

import static org.junit.Assert.*;

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;

import bomberman.game.logic.Game;
import bomberman.game.ui.GameFrame;
import bomberman.game.ui.GamePanel;
import bomberman.game.ui.StatusBar;
import bomberman.sockets.Client;

public class BombermanTests {

	@Test
	public void checkValidLogin() {
		String username = "username";
		String password = "password";
		Client client = new Client("Client1", "10.227.156.4",  6666, new JFrame());
		client.sendData("checkloginuser" + username + "pass" + password);
		client.receiving(new JFrame(), new JPanel());
		assertEquals("logintrue", client.getMessage());
	}
	
	@Test
	public void checkInvalidLogin() {
		String username = "sadgasfgsfgd";
		String password = "dfgsdfgdsfgsdfgsd";
		Client client = new Client("Client2", "10.227.156.4",  6666, new JFrame());
		client.sendData("checkloginuser" + username + "pass" + password);
		client.receiving(new JFrame(), new JPanel());
		assertTrue(client.getMessage().contains("false") && client.getMessage().contains("login"));
	}
	
	@Test
	public void getSavedGames() {
		String username = "test";
		String password = "test";
		Client client = new Client("Client1", "10.227.156.4",  6666, new JFrame());
		client.sendData("checkloginuser" + username + "pass" + password);
		client.receiving(new JFrame(), new JPanel());
		
		client.sendData("getsavedgames");
		client.receiving(new JFrame(), new JPanel());
		assertEquals("test/test1/", client.getMessage());
	}
	
	@Test
	public void getRankings() {
		String username = "test";
		String password = "test";
		Client client = new Client("Client1", "10.227.156.4",  6666, new JFrame());
		client.sendData("checkloginuser" + username + "pass" + password);
		client.receiving(new JFrame(), new JPanel());
		
		client.sendData("getrankings");
		client.receiving(new JFrame(), new JPanel());
		assertEquals("pedro,600/americo,300/test,100/andre,0/asdfadfxgcasdf,0/asdfasfddfxgcasdf,0/new,0/newusername,0/qwe,0/user,0/username,0/", client.getMessage());
	}
	
	@Test
	public void invalidRegister() {
		String username = "test";
		String password = "test";
		Client client = new Client("Client1", "10.227.156.4",  6666, new JFrame());
		client.sendData("registeruser" + username + "pass" + password);
		client.receiving(new JFrame(), new JPanel());
		assertEquals("registerfalse", client.getMessage());
	}
	
	@Test
	public void newSingleplayerGame() {
		int level = 1;
		GameFrame frame = new GameFrame(1, false, new JFrame());
		frame.setVisible(false);
		frame.getPane().setVisible(false);
		assertEquals(16, frame.getPane().getGame().getPlayer().getX());
		assertEquals(16, frame.getPane().getGame().getPlayer().getY());
	}
}
