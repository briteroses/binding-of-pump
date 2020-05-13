package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Loading extends BasicGameState {
	private int id;
	private static Image screen;
	private static long timeSinceLastLoad;
	
	public Loading(int init) {
		id = init;
	}
	public int getID() {
		return id;
	}
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		//empty
	}
	public static void enter(StateBasedGame game) throws SlickException {
		screen = new Image("images/screens/Loading.png");
		timeSinceLastLoad = System.currentTimeMillis();
		game.enterState(Runner.LOADING);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		screen.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(System.currentTimeMillis()-timeSinceLastLoad>800) {
			Crawling.enter(game);
		}
	}
	
}
