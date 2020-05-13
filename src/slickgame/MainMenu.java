package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class MainMenu extends BasicGameState{
	private int id;
	private static Image screen;
	static int starter;

	public MainMenu(int init) {
		id = init;
	}
	public int getID() {
		return id;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		Item.init(container, game);
		screen = new Image("images/screens/IntroBackground.png");
		starter = 0;
	}
	public static void enter(StateBasedGame game) throws SlickException {
		screen = new Image("images/screens/IntroBackground.png");
		game.enterState(Runner.MAINMENU);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		screen.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//activate main game
		Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_SPACE)) {
			starter++;
		}
		if(starter==1) {
			screen = new Image("images/screens/StoryBackground.png");
		}
		if(starter==2) {
			Loading.enter(game);
		}
	}
	
}	
