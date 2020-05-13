package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.awt.Rectangle;

public class Pickup {
	private String type;
	private Image sprite;
	private float x;
	private float y;
	private int spriteWidth;
	private int spriteHeight;
	private Rectangle hitbox;
	
	private boolean removeMe;
	
	static final String HEART = "heart";
	private static final int HEART_HEAL = 15;
	static final String SHIELD = "shield";
	private static final int SHIELD_HEAL = 15;
	static final String GOLDHEART = "goldheart";
	private static final int GOLDHEART_HEAL = 60;
	static final String HP_POTION = "potion1";
	static final String FIRERATE_POTION = "potion2";
	static final String SHOTSPEED_POTION = "potion3";
	static final String SPEED_POTION = "potion4";
	static final String DAMAGE_POTION = "potion5";
	static final String RANGE_POTION = "potion6";
	
	
	public Pickup (String type, float x, float y) throws SlickException {
		this.type = type;
		this.x = x;
		this.y = y;
		removeMe = false;
		sprite = null;
		if(type==HEART) {
			sprite = new Image("images/pickups/heart.png");
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==SHIELD) {
			sprite = new Image("images/pickups/shield.png");
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==GOLDHEART) {
			sprite = new Image("images/pickups/goldHeart.png");
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==HP_POTION) {
			sprite = new Image("images/pickups/potions.png").getSubImage(1, 1, 15, 30);
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==FIRERATE_POTION) {
			sprite = new Image("images/pickups/potions.png").getSubImage(1, 125, 15, 30);
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==SHOTSPEED_POTION) {
			sprite = new Image("images/pickups/potions.png").getSubImage(1, 218, 15, 30);
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==SPEED_POTION) {
			sprite = new Image("images/pickups/potions.png").getSubImage(1, 32, 15, 30);
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==DAMAGE_POTION) {
			sprite = new Image("images/pickups/potions.png").getSubImage(1, 63, 15, 30);
			spriteWidth = 32;
			spriteHeight = 32;
		}
		if(type==RANGE_POTION) {
			sprite = new Image("images/pickups/potions.png").getSubImage(1, 187, 15, 30);
			spriteWidth = 32;
			spriteHeight = 32;
		}
	}
	public void render (GameContainer container, StateBasedGame game, Graphics g) {
		sprite.draw((int)x,(int)y,spriteWidth,spriteHeight);
	}
	public void update (GameContainer container, StateBasedGame game, int delta, Crawling crawling) {
		hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
		Rectangle protagHitbox = new Rectangle((int)(Protagonist.x + Protagonist.SPRITEWIDTH/6), (int)(Protagonist.y + Protagonist.SPRITEHEIGHT/6), (int)2*Protagonist.SPRITEWIDTH/3, (int)5*Protagonist.SPRITEHEIGHT/6);	
		if(hitbox.intersects(protagHitbox)) {
			if(type==HEART) {
				if(Protagonist.HP + HEART_HEAL <= Protagonist.MAXHP) {
					Protagonist.HP += HEART_HEAL;
				}
				else {
					Protagonist.HP = Protagonist.MAXHP;
				}
				removeMe = true;
			}
			if(type==SHIELD) {
				Protagonist.shield+=SHIELD_HEAL;
				removeMe = true;
			}
			if(type==GOLDHEART) {
				if(Protagonist.HP + GOLDHEART_HEAL <= Protagonist.MAXHP) {
					Protagonist.HP += GOLDHEART_HEAL;
				}
				else {
					Protagonist.HP = Protagonist.MAXHP;
				}
				removeMe = true;
			}
			if(type==HP_POTION) {
				Protagonist.MAXHP += 15;
				Protagonist.HP += 15;
				removeMe = true;
			}
			if(type==FIRERATE_POTION) {
				Protagonist.fireRateDelay -= (long)(Protagonist.fireRateDelay*0.23);
				removeMe = true;
			}
			if(type==SHOTSPEED_POTION) {
				Protagonist.shotSpeed += 1.2f;
				removeMe = true;
			}
			if(type==SPEED_POTION) {
				Protagonist.speed += 0.05f;
				removeMe = true;
			}
			if(type==DAMAGE_POTION) {
				Protagonist.damage += 5;
				removeMe = true;
			}
			if(type==RANGE_POTION) {
				Protagonist.range += 96f;
				removeMe = true;
			}
		}
	}
	public boolean toBeRemoved() {
		return removeMe;
	}
}
