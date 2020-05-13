package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.util.ArrayList;

public class Crawling extends BasicGameState {
	private int id;
	
	static int currentLevel = 0;
	static Floor floor;
	static int xCurrentLocation;
	static int yCurrentLocation;
	static Room currentRoom;
	
	static Protagonist protag;
	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	static ArrayList<Projectile> spells = new ArrayList<Projectile>();
	static ArrayList<Projectile> bullets = new ArrayList<Projectile>();
	static ArrayList<Boss> bosses = new ArrayList<Boss>();
	static ArrayList<Pickup> tempPickups = new ArrayList<Pickup>();
	static ArrayList<Projectile> garbageSpells = new ArrayList<Projectile>();
	
	static int killCount;
	static int killGoal;
	static int roomsCleared;
	private static long failsafe;
	
	private int levelTransitionTrigger; //helper variable for level transition mechanism

	static boolean clearRoomToggle = false; //one-frame indicator for room clears
	static boolean roomSwitching = false; //one-frame indicator for room changes
	static boolean floorCleared = false;
	
	static boolean luciferDefeated = false;
	
	public Crawling(int init) {
		id = init;
	}
	public int getID() {
		return id;
	}
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		protag = new Protagonist();
	}
	public static void enter(StateBasedGame game) throws SlickException {
		currentLevel++;
		floor = new Floor(currentLevel);
		roomsCleared = 0;
		xCurrentLocation = floor.getStarterLocation()[0];
		yCurrentLocation = floor.getStarterLocation()[1];
		currentRoom = floor.getRoom(xCurrentLocation, yCurrentLocation);
		setupRoom(currentRoom);
		spells.clear();
		bullets.clear();
		game.enterState(Runner.CRAWLING);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
	{
		//render room, player, enemies, projectiles
		floor.getRoom(xCurrentLocation, yCurrentLocation).render(container, game, g);
		for (int i=0; i<spells.size(); i++){
			spells.get(i).render(container, game, g);
		}
		for (int i=0; i<bullets.size(); i++){
			bullets.get(i).render(container, game, g);
		}
		for (int i=0; i<enemies.size(); i++) {
			enemies.get(i).render(container, game, g);
		}
		for (int i=0; i<bosses.size(); i++) {
			bosses.get(i).render(container, game, g);
		}
		protag.render(container, game, g);
		Item.renderFamiliars(container, game, g);
		Item.renderHalos(g);
		//render UI
		UI.render(container, game, g, this);
	}
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
	{
		//debugging
		
		//update room, enemies, and projectiles
		currentRoom.update(container, game, delta, this);
		for (int i=0; i<enemies.size(); i++) {
			enemies.get(i).update(container, game, delta, this);
		}
		for (int i=0; i<bosses.size(); i++) {
			bosses.get(i).update(container, game, delta, this);
		}
		for (int i=0; i<spells.size(); i++){
			spells.get(i).update(container, game, delta, this);
		}
		for (int i=0; i<bullets.size(); i++){
			bullets.get(i).update(container, game, delta, this);
		}
		
		//remove expired projectiles and enemies and add to kill count for room
		{int enemyCountBefore = enemies.size()+bosses.size();
		enemies.removeIf(e -> e.toBeRemoved());
		bosses.removeIf(b -> b.toBeRemoved());
		int enemyCountAfter = enemies.size()+bosses.size();
		killCount += enemyCountBefore-enemyCountAfter;}
		spells.removeIf(p -> p.toBeRemoved());
		spells.removeIf(p -> p==null);
		bullets.removeIf(p -> p.toBeRemoved());
		while(spells.size()>256) {
			spells.remove(0);
		}
		
		//toggle minimap
		Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_TAB)) {
			UI.tabToggle = !UI.tabToggle;
		}
		
		//update protagonist
		protag.update(container, game, delta, this);
		
		//clear room if all enemies killed
		if(clearRoomToggle) {
			clearRoomToggle  = false;
		}
		if(enemies.size()+bosses.size()==0 && !currentRoom.isCleared()) {
			clearRoom();
			clearRoomToggle = true;
		}
		//if softlocked: clear room after 90 seconds
		if(System.currentTimeMillis()-failsafe>90000 && !currentRoom.isCleared()) {
			clearRoom();
			clearRoomToggle = true;
		}
		
		//changes room when player moves through doorway
		if(roomSwitching) {
			roomSwitching = false;
		}
		garbageSpells.clear();
		this.changeRoom();
		
		//change to pause screen
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			PauseScreen.enter(game);
		}
		
		//complete floor and enter level transition
		if (roomsCleared >= floor.getFloorSize() || luciferDefeated) {
			if(levelTransitionTrigger==1) {
				levelTransitionTrigger = 2;
				floorCleared = false;
			}
			if(levelTransitionTrigger==0) {
				levelTransitionTrigger = 1;
				floorCleared = true;
			}
			if (levelTransitionTrigger==2 && input.isKeyDown(Input.KEY_ENTER)) {
				if(luciferDefeated) {
					levelTransitionTrigger = 0;
					game.enterState(Runner.WINSCREEN);
				}
				else {
					levelTransitionTrigger = 0;
					Protagonist.x = 448f;
					Protagonist.y = 256f;
					Protagonist.HP += (Protagonist.MAXHP-Protagonist.HP)/2;
					Loading.enter(game);
				}
			}
		}
	}
	public void changeRoom() throws SlickException {
		if (currentRoom.isDoorway(Protagonist.x, Protagonist.y) == 0) {
			xCurrentLocation--;
			Protagonist.y = currentRoom.getTiledMap().getTileHeight()*(currentRoom.getTiledMap().getHeight()-1)-Protagonist.SPRITEHEIGHT;
			changeRoomHelper();
		}
		if (currentRoom.isDoorway(Protagonist.x, Protagonist.y) == 1) {
			yCurrentLocation++;
			Protagonist.x = currentRoom.getTiledMap().getTileWidth();
			changeRoomHelper();
		}
		if (currentRoom.isDoorway(Protagonist.x, Protagonist.y) == 2) {
			xCurrentLocation++;
			Protagonist.y = currentRoom.getTiledMap().getTileHeight();
			changeRoomHelper();
		}
		if (currentRoom.isDoorway(Protagonist.x, Protagonist.y) == 3) {
			yCurrentLocation--;
			Protagonist.x = currentRoom.getTiledMap().getTileWidth()*(currentRoom.getTiledMap().getWidth()-1)-Protagonist.SPRITEWIDTH;
			changeRoomHelper();
		}
	}
	public void changeRoomHelper() throws SlickException {
		currentRoom = floor.getRoom(xCurrentLocation, yCurrentLocation);
		garbageSpells.clear();
		garbageSpells.addAll(spells);
		spells.clear();
		bullets.clear();
		roomSwitching = true;
		setupRoom(currentRoom);
	}
	public static void setupRoom(Room room) throws SlickException {
		enemies.clear();
		bosses.clear();
		int layer = 0;
		for (int i = 0; i < room.getTiledMap().getWidth(); i++)
		{
			for (int j = 0; j < room.getTiledMap().getHeight(); j++)
			{
				int tileID = room.getTiledMap().getTileId(i, j, layer);
				String type = room.getTiledMap().getTileProperty(tileID, "enemygen", "");
				if (!type.equals("")) {
					enemies.add(new Enemy(type, i*room.getTiledMap().getTileWidth(), j*room.getTiledMap().getTileHeight()));
				}
			}
		}
		for (int i = 0; i < room.getTiledMap().getWidth(); i++)
		{
			for (int j = 0; j < room.getTiledMap().getHeight(); j++)
			{
				int tileID = room.getTiledMap().getTileId(i, j, layer);
				String type = room.getTiledMap().getTileProperty(tileID, "bossgen", "");
				if (!type.equals("")) {
					if (currentLevel==7 && xCurrentLocation==floor.getBossLocation()[0] && yCurrentLocation==floor.getBossLocation()[1]) {
						bosses.add(new Boss("lucifer", i*room.getTiledMap().getTileWidth()-64, j*room.getTiledMap().getTileHeight()-64));
					} else {
						bosses.add(new Boss(Integer.toString((int)(4*Math.random())), i*room.getTiledMap().getTileWidth()-64, j*room.getTiledMap().getTileHeight()-64));
					}
				}
			}
		}
//		for (int i = 0; i < room.getTiledMap().getWidth(); i++)
//		{
//			for (int j = 0; j < room.getTiledMap().getHeight(); j++)
//			{
//				int tileID = room.getTiledMap().getTileId(i, j, layer);
//				String type = room.getTiledMap().getTileProperty(tileID, "item", "false");
//				if (type.equals("true")) {
//					currentRoom.addItem(new Item());
//				}
//			}
//		}
		for(Pickup p : tempPickups) {
			currentRoom.addPickup(p);
		}
		tempPickups.clear();
		killCount = 0;
		killGoal = enemies.size()+bosses.size();
		for (int i=0; i<360; i+=90) {
			if(xCurrentLocation+(int)Math.cos(Math.toRadians(i)) >= 0 && xCurrentLocation+(int)Math.cos(Math.toRadians(i)) < Floor.CAPACITY && yCurrentLocation+(int)Math.sin(Math.toRadians(i)) >= 0 && yCurrentLocation+(int)Math.sin(Math.toRadians(i)) < Floor.CAPACITY && floor.getRoom(xCurrentLocation+(int)Math.cos(Math.toRadians(i)), yCurrentLocation+(int)Math.sin(Math.toRadians(i))) != null) {
				floor.getRoom(xCurrentLocation+(int)Math.cos(Math.toRadians(i)), yCurrentLocation+(int)Math.sin(Math.toRadians(i))).reveal();
			}
		}
		Protagonist.tookDamageThisRoom = false;
		//final boss mode: shrink player
		if(currentLevel==7 && xCurrentLocation==floor.getBossLocation()[0] && yCurrentLocation==floor.getBossLocation()[1]) {
			Protagonist.SPRITEWIDTH /= 2;
			Protagonist.SPRITEHEIGHT /= 2;
			Projectile.BASE_SPRITEWIDTH /= 2;
			Projectile.BASE_SPRITEHEIGHT /= 2;
		}
		//if softlocked: clear room after 90 seconds
		failsafe = System.currentTimeMillis();
	}
	public static void clearRoom() throws SlickException {
		Room temp = new Room(currentRoom.getFileName().substring(0, currentRoom.getFileName().length()-4)+"n.tmx");
		currentRoom = floor.setRoom(temp, xCurrentLocation, yCurrentLocation);
		setupRoom(currentRoom);
		roomsCleared++;
		//randomly spawns a heart, drinks, or shield
		int centerX = currentRoom.getTiledMap().getWidth()/2;
		int centerY = currentRoom.getTiledMap().getHeight()/2;
		boolean heartNotPlaced = true;
		int heartX = 0;
		int heartY = 0;
		int distFromCenter = 0;
		while (heartNotPlaced) {
			for (int i=0; i<currentRoom.getTiledMap().getWidth(); i++) {
				for (int j=0; j<currentRoom.getTiledMap().getHeight(); j++) {
					if (Math.abs(i-centerX)+Math.abs(j-centerY)==distFromCenter && !currentRoom.getTiledMap().getTileProperty(currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
						heartNotPlaced = false;
						heartX = i;
						heartY = j;
					}
				}
			}
			distFromCenter++;
		}
		if(floor.getLayout()[xCurrentLocation][yCurrentLocation]<=1) {
			double roll = Math.random();
			if (roll<0.3) {
				currentRoom.addPickup(new Pickup(Pickup.HEART, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));
			}
			else if (roll<0.3133) {
				currentRoom.addPickup(new Pickup(Pickup.HP_POTION, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));			
			}
			else if (roll<0.3266) {
				currentRoom.addPickup(new Pickup(Pickup.FIRERATE_POTION, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));			
			}
			else if (roll<0.34) {
				currentRoom.addPickup(new Pickup(Pickup.SHOTSPEED_POTION, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));			
			}
			else if (roll<0.3533) {
				currentRoom.addPickup(new Pickup(Pickup.SPEED_POTION, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));			
			}
			else if (roll<0.3666) {
				currentRoom.addPickup(new Pickup(Pickup.DAMAGE_POTION, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));			
			}
			else if (roll<0.38) {
				currentRoom.addPickup(new Pickup(Pickup.RANGE_POTION, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));			
			}
			else if (roll<0.45+0.12*Protagonist.hasMitre) {
				currentRoom.addPickup(new Pickup(Pickup.SHIELD, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));
			}			
		}
		//spawns item after item room kill
		if(currentLevel!=7 && !(xCurrentLocation==floor.getBossLocation()[0] && yCurrentLocation==floor.getBossLocation()[1]) && floor.getLayout()[xCurrentLocation][yCurrentLocation]==2) {
			centerX = currentRoom.getTiledMap().getWidth()/2;
			centerY = currentRoom.getTiledMap().getHeight()/2;
			boolean itemNotPlaced = true;
			int itemX = 0;
			int itemY = 0;
			distFromCenter = 0;
			while (itemNotPlaced) {
				for (int i=0; i<currentRoom.getTiledMap().getWidth(); i++) {
					for (int j=0; j<currentRoom.getTiledMap().getHeight(); j++) {
						if (Math.abs(i-centerX)+Math.abs(j-centerY)==distFromCenter && !currentRoom.getTiledMap().getTileProperty(currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
							itemNotPlaced = false;
							itemX = i;
							itemY = j;
						}
					}
				}
				distFromCenter++;
			}
			currentRoom.addItem(new Item(itemX*currentRoom.getTiledMap().getTileWidth(), itemY*currentRoom.getTiledMap().getTileHeight()));
		}
		//spawns full heal or item after boss room kill
		if(currentLevel!=7 && xCurrentLocation == floor.getBossLocation()[0] && yCurrentLocation == floor.getBossLocation()[1]) {
			centerX = currentRoom.getTiledMap().getWidth()/2;
			centerY = currentRoom.getTiledMap().getHeight()/2;
			heartNotPlaced = true;
			heartX = 0;
			heartY = 0;
			distFromCenter = 0;
			while (heartNotPlaced) {
				for (int i=0; i<currentRoom.getTiledMap().getWidth(); i++) {
					for (int j=0; j<currentRoom.getTiledMap().getHeight(); j++) {
						if (Math.abs(i-centerX)+Math.abs(j-centerY)==distFromCenter && !currentRoom.getTiledMap().getTileProperty(currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
							heartNotPlaced = false;
							heartX = i;
							heartY = j;
						}
					}
				}
				distFromCenter++;
			}
			if(currentLevel % 2 == 1) {
				currentRoom.addPickup(new Pickup(Pickup.GOLDHEART, heartX*currentRoom.getTiledMap().getTileWidth()+16, heartY*currentRoom.getTiledMap().getTileHeight()+16));
			}
			if(currentLevel % 2 == 0) {
				currentRoom.addItem(new Item(heartX*currentRoom.getTiledMap().getTileWidth(), heartY*currentRoom.getTiledMap().getTileHeight()));
			}
		}
		if(currentLevel==7 && !(xCurrentLocation==floor.getBossLocation()[0] && yCurrentLocation==floor.getBossLocation()[1]) && floor.getLayout()[xCurrentLocation][yCurrentLocation]==2) {
			centerX = currentRoom.getTiledMap().getWidth()/2;
			centerY = currentRoom.getTiledMap().getHeight()/2;
			heartNotPlaced = true;
			heartX = 0;
			heartY = 0;
			distFromCenter = 0;
			while (heartNotPlaced) {
				for (int i=0; i<currentRoom.getTiledMap().getWidth(); i++) {
					for (int j=0; j<currentRoom.getTiledMap().getHeight(); j++) {
						if (Math.abs(i-centerX)+Math.abs(j-centerY)==distFromCenter && !currentRoom.getTiledMap().getTileProperty(currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
							heartNotPlaced = false;
							heartX = i;
							heartY = j;
						}
					}
				}
				distFromCenter++;
			}
			currentRoom.addItem(new Item(heartX*currentRoom.getTiledMap().getTileWidth(), heartY*currentRoom.getTiledMap().getTileHeight()));
		}
		if(currentLevel==7 && (xCurrentLocation==floor.getBossLocation()[0] && yCurrentLocation==floor.getBossLocation()[1])) {
			luciferDefeated=true;
			Protagonist.SPRITEWIDTH *= 2;
			Protagonist.SPRITEHEIGHT *= 2;
			Projectile.BASE_SPRITEWIDTH *= 2;
			Projectile.BASE_SPRITEHEIGHT *= 2;
		}
	}
}
