package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class PauseScreen extends BasicGameState {
	private int id;
	private static Image screen;
	
	public PauseScreen(int init) {
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
		screen = new Image("images/screens/Pause.png");
		game.enterState(Runner.PAUSESCREEN);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		screen.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			Protagonist.timeSinceLastSpell = System.currentTimeMillis();
			game.enterState(Runner.CRAWLING);
		}
	}
	
}