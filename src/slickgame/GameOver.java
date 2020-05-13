package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class GameOver extends BasicGameState {
	private int id;
	private Image screen;
	
	public GameOver(int init) {
		id = init;
	}
	public int getID() {
		return id;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		screen = new Image("images/screens/GameOver.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		screen.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		if(input.isKeyDown(Input.KEY_SPACE)) {
			MainMenu.starter = 0;
			Crawling.currentLevel = 0;
			Item.init(container, game);
			Protagonist.reset();
			Crawling.protag = new Protagonist();
			MainMenu.enter(game);
		}
	}
}