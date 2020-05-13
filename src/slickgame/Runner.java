package slickgame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.io.File;
/**
@author Alvin and Brian
Comments:   1. Remember to make tiledMaps into gzib (compressed) type
*/
/**
Planned features:
 - add and implement boss, create boss room
 - add and implement items, create item room
 - create more enemy variations and more room types
 */
public class Runner extends StateBasedGame {

	public static final int MAINMENU = 0;
	public static final int LOADING = 1;
	public static final int CRAWLING = 2;
	public static final int PAUSESCREEN = 3;
	public static final int LEVELTRANSITION = 4;
	public static final int GAMEOVER = 5;
	public static final int WINSCREEN = 6;
	
	public static final int WIDTH = 960;
	public static final int HEIGHT = 576;
	public static final int FPS = 60;
	
	public Runner(String name) throws SlickException {
		super("runner");
		System.setProperty("org.lwjgl.librarypath", new File("java_game_lib/slick/lwjgl64.dll").getAbsolutePath());
		this.addState(new MainMenu(MAINMENU));
		this.addState(new Loading(LOADING));
		this.addState(new Crawling(CRAWLING));
		this.addState(new PauseScreen(PAUSESCREEN));
		this.addState(new LevelTransition(LEVELTRANSITION));
		this.addState(new GameOver(GAMEOVER));
		this.addState(new WinScreen(WINSCREEN));
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException
	{
			getState(MAINMENU).init(container, this);
			getState(LOADING).init(container, this);
			getState(CRAWLING).init(container, this);
			getState(PAUSESCREEN).init(container, this);
			getState(LEVELTRANSITION).init(container, this);
			getState(GAMEOVER).init(container, this);
			getState(WINSCREEN).init(container, this);
	}
	
	public static void main(String[] arguments) throws LWJGLException
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new Runner("The Binding of Lil Pump"));
			DisplayMode[] modes = Display.getAvailableDisplayModes();

	        for (int i=0;i<modes.length;i++) {
	            DisplayMode current = modes[i];
	            System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
	                                current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
	        }
	        System.out.println(app.getAspectRatio());
			app.setShowFPS(false);
			app.setDisplayMode(960, 576, false);
			app.setTargetFrameRate(FPS);
			app.start();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
