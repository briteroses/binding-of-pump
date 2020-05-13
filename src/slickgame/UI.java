package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class UI {
	
	private static boolean hasTreasureMap = false;
	static boolean tabToggle = false;
	
	public static void render(GameContainer container, StateBasedGame game, Graphics g, Crawling crawling) {
		renderUI(container, game, g, crawling);
		renderMinimap(container, g);
	}
	public static void renderUI(GameContainer container, StateBasedGame game, Graphics g, Crawling crawling) {
		g.resetFont();
		g.setColor(Color.white);
		if (Crawling.roomsCleared >= Crawling.floor.getFloorSize() || Crawling.luciferDefeated) {
			g.drawString("FLOOR COMPLETE", 420, 192);
			g.drawString("PRESS ENTER TO GO DEEPER", 380, 224);
		}
		g.setColor(new Color(255,44,44,180)); //max HP bar
		for(int i=0; i < Protagonist.MAXHP; i++) {
			g.fillRect(44+2*i, 14, 2, 11);
		}
		g.setColor(Color.green); //HP bar
		for(int i=0; i < Protagonist.HP; i++) {
			g.fillRect(44+2*i, 14, 2, 11);
		}
		g.setColor(Color.cyan);
		for(int i=0; i < Protagonist.shield; i++) {
			g.fillRect(44+2*i, 29, 2, 11);
		}
		g.setColor(Color.white); //max slowmode bar
		for(float i=0; i < Protagonist.maxSlowmodeCharge; i++) {
			g.fillRect(44+i*0.5f, 44, 0.5f, 11);
		}
		g.setColor(Color.magenta); //slowmode bar
		for(float i=0; i < Protagonist.slowmodeCharge; i++) {
			g.fillRect(44+i*0.5f, 44, 0.5f, 11);
		}
		g.setColor(Color.white);
		g.drawString("HP: ", 15, 10);
		g.drawString("SP: ", 15, 25);
		g.drawString("LP: ", 15, 40);
		g.drawString("Floor " + Crawling.currentLevel, 15, 55);
		renderMinimap(container, g);
		g.setColor(new Color(255,255,255,90));
		for(int i=0; i<Math.min(6, Protagonist.obtainedItems.size()); i++) {
			g.fillRect(15+69*i, 497, 64, 64);
		}
		for(int i=Protagonist.obtainedItems.size()-6; i<Protagonist.obtainedItems.size(); i++) {
			if(i>=0) {
				Protagonist.obtainedItems.get(i).getSprite().draw(15+69*(5-(i-(Protagonist.obtainedItems.size()-6))), 497, 64, 64);
			}
		}
	}
	public static void renderMinimap(GameContainer container, Graphics g) {
		double transparent = 1;
		if(tabToggle) {
			transparent = 0;
		}
		int[] bounds = Crawling.floor.getBounds();
		g.setColor(new Color(0,0,0,(int)transparent*120));
		g.fillRect(800, 10, 150, 120);
		//uncleared normal rooms
		g.setColor(new Color(255,44,44,(int)transparent*255)); //red
		for(int i=0; i<=bounds[1]-bounds[0]; i++) {
			for(int j=0; j<=bounds[3]-bounds[2]; j++) {
				if(Crawling.floor.getRoom(i+bounds[0], j+bounds[2]) != null && (Crawling.floor.getRoom(i+bounds[0], j+bounds[2]).isRevealed() || hasTreasureMap)) {
					g.fillRect(815+j*(int)120/(bounds[3]-bounds[2]+1), 25+i*(int)90/(bounds[1]-bounds[0]+1), (int)120/(bounds[3]-bounds[2]+1), (int)90/(bounds[1]-bounds[0]+1));
				}
			}
		}
		//cleared rooms
		g.setColor(new Color(102,204,255,(int)transparent*255)); //blue
		for(int i=0; i<=bounds[1]-bounds[0]; i++) {
			for(int j=0; j<=bounds[3]-bounds[2]; j++) {
				if(Crawling.floor.getRoom(i+bounds[0], j+bounds[2]) != null && Crawling.floor.getRoom(i+bounds[0], j+bounds[2]).isCleared() && (Crawling.floor.getRoom(i+bounds[0], j+bounds[2]).isRevealed() || hasTreasureMap)) {
					g.fillRect(815+j*(int)120/(bounds[3]-bounds[2]+1), 25+i*(int)90/(bounds[1]-bounds[0]+1), (int)120/(bounds[3]-bounds[2]+1), (int)90/(bounds[1]-bounds[0]+1));
				}
			}
		}
		//starter room
		g.fillRect(815+(Crawling.floor.getStarterLocation()[1]-bounds[2])*120/(bounds[3]-bounds[2]+1), 25+(Crawling.floor.getStarterLocation()[0]-bounds[0])*(int)90/(bounds[1]-bounds[0]+1), (int)120/(bounds[3]-bounds[2]+1), (int)90/(bounds[1]-bounds[0]+1));
		//item rooms
		g.setColor(new Color(240,240,96,(int)transparent*255)); //yellow
		for(int i=0; i<=bounds[1]-bounds[0]; i++) {
			for(int j=0; j<=bounds[3]-bounds[2]; j++) {
				if(Crawling.floor.getLayout()[i+bounds[0]][j+bounds[2]]==2 && (Crawling.floor.getRoom(i+bounds[0], j+bounds[2]).isRevealed() || hasTreasureMap)) {
					g.fillRect(815+j*(int)120/(bounds[3]-bounds[2]+1), 25+i*(int)90/(bounds[1]-bounds[0]+1), (int)120/(bounds[3]-bounds[2]+1), (int)90/(bounds[1]-bounds[0]+1));
				}
			}
		}
		//boss room
		if(Crawling.currentLevel % 2 == 0 || Crawling.currentLevel==7) {
			g.setColor(new Color(255, 50, 255, (int)transparent*255)); //purple
			if(Crawling.floor.getRoom(Crawling.floor.getBossLocation()[0], Crawling.floor.getBossLocation()[1]).isRevealed() || hasTreasureMap) {
				g.fillRect(815+(Crawling.floor.getBossLocation()[1]-bounds[2])*(int)120/(bounds[3]-bounds[2]+1), 25+(Crawling.floor.getBossLocation()[0]-bounds[0])*(int)90/(bounds[1]-bounds[0]+1), (int)120/(bounds[3]-bounds[2]+1), (int)90/(bounds[1]-bounds[0]+1));
			}
		}
		//current room
		g.setColor(new Color(255,255,255,(int)transparent*255));
		g.fillRect(815+(Crawling.yCurrentLocation-bounds[2])*120/(bounds[3]-bounds[2]+1), 25+(Crawling.xCurrentLocation-bounds[0])*(int)90/(bounds[1]-bounds[0]+1), (int)120/(bounds[3]-bounds[2]+1), (int)90/(bounds[1]-bounds[0]+1));
	}
	public static void setTreasureMap() {
		hasTreasureMap = true;
	}
}
