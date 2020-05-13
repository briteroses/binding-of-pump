package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.tiled.*;

import java.util.ArrayList;
import java.awt.Rectangle;

public class Room {
	private TiledMap room;
	private String filename;
	private int pixelWidth;
	private int pixelHeight;
	private int[] doorwayFormat;
	private ArrayList<Rectangle> blocks = new ArrayList<Rectangle>();
	ArrayList<Pickup> pickups = new ArrayList<Pickup>();
	ArrayList<Item> items = new ArrayList<Item>();
	
	static final int PROTAG_COLLIDE = 0;
	static final int PROJECTILE_COLLIDE = 1;
	static final int ENEMY_COLLIDE = 2;
	
	private boolean revealMe = false;
	
	public Room(String name) throws SlickException {
		room = new TiledMap(name);
		filename = name;
		//Initialize blocks
		int layer = 0;
		for (int i = 0; i < room.getWidth(); i++)
		{
			for (int j = 0; j < room.getHeight(); j++)
			{
				int tileID = room.getTileId(i, j, layer);
				String blockedValue = room.getTileProperty(tileID, "blocked", "false");
				if (blockedValue.equals("true")) {
					blocks.add(new Rectangle((int)i*room.getTileWidth(),(int)j*room.getTileHeight(), room.getTileWidth(), room.getTileHeight()));
				}
			}
		}
		//TO BE WRITTEN: ITEMGEN
		//Initialize doorwayFormat
//		doorwayFormat = new int[4];
//		for (int i=0; i<4; i++) {
//			doorwayFormat[i] = Integer.parseInt(name.substring(i+6, i+7));
//		}
	}
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		room.render(0, 0);
		for (int i=0; i<pickups.size(); i++) {
			pickups.get(i).render(container, game, g);
		}
		for (int i=0; i<items.size(); i++) {
			items.get(i).render(container, game, g);
		}
	}
	public void update(GameContainer container, StateBasedGame game, int delta, Crawling crawling) {
		for (int i=0; i<pickups.size(); i++) {
			pickups.get(i).update(container, game, delta, crawling);
		}
		for (int i=0; i<items.size(); i++) {
			items.get(i).update(container, game, delta, crawling);
		}
		pickups.removeIf(p -> p.toBeRemoved());
		items.removeIf(i -> i.toBeRemoved());
	}
	public boolean isTerrainCollide(float x, float y, int detectorID) {
		Rectangle hitbox = new Rectangle(0,0,0,0);
		if (detectorID == PROTAG_COLLIDE) {
			hitbox = new Rectangle((int)(x + Protagonist.SPRITEWIDTH/6), (int)(y + Protagonist.SPRITEHEIGHT/3), (int)2*Protagonist.SPRITEWIDTH/3, (int)2*Protagonist.SPRITEHEIGHT/3);
		}
		for (Rectangle pix: blocks) {
			if (Protagonist.isFlying) {
				if (!(x >= room.getTileWidth() && x + Protagonist.SPRITEWIDTH <= (room.getWidth()-1)*room.getTileWidth() && y >= room.getTileHeight() && y + Protagonist.SPRITEHEIGHT <= (room.getHeight()-1)*room.getTileHeight()) && hitbox.intersects(pix)) {
					return true;
				}
			}
			else {
				if (hitbox.intersects(pix)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isTerrainCollide(float x, float y, int detectorID, int spriteWidth, int spriteHeight) {
		Rectangle hitbox = new Rectangle(0,0,0,0);
		if (detectorID == PROJECTILE_COLLIDE) {
			hitbox = new Rectangle((int)x, (int)y, (int)spriteWidth, (int)spriteHeight);
		}
		if (detectorID == ENEMY_COLLIDE) {
			hitbox = new Rectangle((int)x, (int)y, (int)spriteWidth, (int)spriteHeight);
		}
		for (Rectangle pix: blocks) {
			if (hitbox.intersects(pix)) {
				return true;
			}
		}
		return false;
	}
	public int isDoorway(float x, float y) {
		pixelWidth = room.getWidth()*room.getTileWidth();
		pixelHeight = room.getHeight()*room.getTileHeight();
		if (y < -1) {
			return 0;
		}
		else if (x > pixelWidth + 1 - Protagonist.SPRITEWIDTH) {
			return 1;
		}
		else if (y > pixelHeight + 1 - Protagonist.SPRITEHEIGHT) {
			return 2;
		}
		else if (x < -1) {
			return 3;
		}
		else {
			return -1;
		}
	}
	public boolean isRevealed() {
		return revealMe;
	}
	public void reveal() {
		revealMe = true;
	}
	public boolean isCleared() {
		//equals t for start.tmx maps; equals n for dead maps
		return filename.substring(filename.length()-5,filename.length()-4).equals("t")  || filename.substring(filename.length()-5, filename.length()-4).equals("n");
	}
	public void addPickup(Pickup p) {
		pickups.add(p);
	}
	public void addItem(Item i) {
		items.add(i);
	}
	public TiledMap getTiledMap() {
		return room;
	}
	public String getFileName() {
		return filename;
	}
	public int getPixelWidth() {
		return pixelWidth;
	}
	public int getPixelHeight() {
		return pixelHeight;
	}
}

