package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class LevelTransition extends BasicGameState {
	private int id;
	private static Image screen;
	private static int progress;
	
	public LevelTransition(int init) {
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
		screen = new Image("images/screens/LevelTransition1.png");
		progress = 0;
		game.enterState(Runner.LEVELTRANSITION);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		screen.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_1)) {
			Protagonist.MAXHP += 15;
			progress++;
		} else if (input.isKeyPressed(Input.KEY_2)) {
			Protagonist.fireRateDelay -= (long)(Protagonist.fireRateDelay*0.23);
			progress++;
		} else if (input.isKeyPressed(Input.KEY_3)) {
			Protagonist.shotSpeed += 1.2f;
			progress++;
		} else if (input.isKeyPressed(Input.KEY_4)) {
			Protagonist.speed += 0.05f;
			progress++;
		} else if (input.isKeyPressed(Input.KEY_5)) {
			Protagonist.damage += 5;
			progress++;
		} else if (input.isKeyPressed(Input.KEY_6)) {
			Protagonist.range += 96f;
			progress++;
		}
		if(progress==0) {
			screen = new Image("images/screens/LevelTransition1.png");
		}
//		if(progress==1) {
//			screen = new Image("images/screens/LevelTransition2.png");
//		}
		if(progress==1) {
			Loading.enter(game);
		}
	}
}