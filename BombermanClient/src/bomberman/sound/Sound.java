package bomberman.sound;

import java.io.*;
import java.util.ArrayList;

import javax.sound.sampled.*;

import bomberman.game.logic.Game;
import bomberman.main.Main;
   
/**
 * Class to represent sounds played along the game. They are instantiated statically inside this class so it has to be loaded once.
 * @author andre
 *
 */
public class Sound {
	
    private String file;
    private boolean loop = false;
    private AudioInputStream stream;
	private AudioFormat format;
	private Clip clip = null;
    
	public static Sound theme = new Sound("res/sound/theme.wav", true);
	public static Sound menu = new Sound("res/sound/menu.wav", true);
	public static Sound press = new Sound("res/sound/press.wav", false);
    public static Sound explosion = new Sound("res/sound/bomb/BOM_11_S.wav", false);  
    public static Sound move = new Sound("res/sound/player/PLAYER_WALK.wav", false);
    public static Sound timeOut = new Sound("res/sound/TIME_UP.wav", false);
    public static Sound dropBomb = new Sound("res/sound/player/BOM_SET.wav", false);
    public static Sound power = new Sound("res/sound/player/ITEM_GET.wav", false);
    public static Sound dead = new Sound("res/sound/player/PLAYER_OUT.wav", false);
    /**
     * Create sound from given file path.
     * @param file Where the sound file is placed.
     * @param loop If the sound is going to be played in loop.
     */
   public Sound(String file, boolean loop) {
	   this.loop = loop;
	   this.file = file;
   }
   /**
    * Method to play sound. Only plays if sound/music is activated.
    */
   public void play() {
	   if(!loop) {
		   if(Main.sound)
			   sound();
	   }
	   else {
		   	if(Main.music)
		   		sound();
	   }
   }
   /**
    * Play sound.
    */
   private void sound() {
	   try {
		   if(clip != null) {
			   if(clip.available() == 0)
				   clip.stop();
		   }
			stream = AudioSystem.getAudioInputStream(new File(file));
			format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		   clip.start();
		  if(loop) 
			   clip.loop(50);
   }
   /**
    * Stop playing sound.
    */
   public void stop() {
	   clip.stop();
   }
}
