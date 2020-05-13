package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class WinScreen extends BasicGameState {
	private int id;
	private Image screen;
	
	public WinScreen(int init) {
		id = init;
	}
	public int getID() {
		return id;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		screen = new Image("images/screens/WinScreenBackground.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		screen.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
}
