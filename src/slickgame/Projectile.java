package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Projectile {
	
	private Image sprite;
	private Rectangle hitbox;
	private int spriteWidth;
	private int spriteHeight;
	
	static int BASE_SPRITEWIDTH = 16;
	static int BASE_SPRITEHEIGHT = 16;
	
	private float x;
	private float y;
	private float xIncrement;
	private float yIncrement;
	private double directionAngle; //in degrees, angle starts at the rightward vector and increases clockwise
	private float speed;
	private int damage;
	private float range;
	
	private int projectileID;
	private boolean removeMe;
	private float distTraveled;
	private long timeSpawned = System.currentTimeMillis();
	
	private boolean isSpectral = false;
	private boolean isPiercing = false;
	private ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();
	private boolean isHitting = false; //proxy before removal after hitting enemy
	private Enemy basisEnemy;
	private Enemy targetEnemy;
	private boolean didNotHitEnemy = false; //proxy before removal after expiry (not hitting enemy)
	private boolean isExpired = false; //proxy before removal after end-of-range expiry
	private ArrayList<Boss> hitBosses = new ArrayList<Boss>();
	private Boss targetBoss;
	
	private boolean isSlowed;
	private float slowFactor;
	private long timeSinceLastSlow;
	private static final long SLOW_DURATION = 3000;
	private float polarRadius = 0; //helper variable for polar coordinates
	private int rotationOrientation = 0; //helper for tiny planet
	private boolean isRotationSet; //helper for tiny planet
	private int frameCounter = 0;
	
	static final int SPELL = 0;
	static final int BULLET = 1;
	static final int ETHER = 2; //bullet variation: only deals damage to moving protagonist, immune to certain item actions
	static final int VAMP = 3; //bullet variation: deals damage directly to protagonist HP, heals enemy on hit
	
	//MODIFICATIONS FROM ITEMS
	private float damageMultiplier = 1;
	private float[] damageMultiplierList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; //16
		//lump of coal, proptosis, soy milk, bloodlust 1, bloodlust 2, polyphemus, adrenaline, crown of light, dead eye, cricket's head/magic mushroom, abaddon, succubus, bloodlust 3, eye of belial, apple, berserker
	
	public Projectile(int spriteWidth, int spriteHeight, float x, float y, double directionAngle, float speed, int damage, float range, int id) throws SlickException {
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.x = x;
		this.y = y;
		this.directionAngle = directionAngle;
		this.speed = speed;
		this.damage = damage;
		this.range = range;
		distTraveled = 0;
		removeMe = false;
		projectileID = id;
		if (projectileID == SPELL) {
			sprite = new Image("images/projectiles.png").getSubImage(123, 297, 17, 17);
			if(Protagonist.hasSpectral) {
				isSpectral = true;
			}
			if(Protagonist.hasPiercing) {
				isPiercing = true;
			}
		}
		if (projectileID == BULLET) {
			sprite = new Image("images/projectiles.png").getSubImage(188, 157, 11, 11);
		}
		if (projectileID == ETHER) {
			sprite = new Image("images/projectiles.png").getSubImage(71, 67, 9, 9);
		}
		if (projectileID == VAMP) {
			sprite = new Image("images/projectiles.png").getSubImage(71, 13, 9, 9);
		}
		hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
	}
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		sprite.draw((int) x,(int) y, spriteWidth, spriteHeight);
	}
	public void update(GameContainer container, StateBasedGame game, int delta, Crawling crawling) {
		//scales sprite size with damageMultiplier
		if(projectileID == SPELL) {
			spriteWidth = Math.min(54, (int)(BASE_SPRITEWIDTH*(1+damageMultiplier)/2));
			spriteHeight = Math.min(54, (int)(BASE_SPRITEHEIGHT*(1+damageMultiplier)/2));
		}
		
		//detects player/enemy collision and is removed (player and enemy classes handle taking damage)
		hitbox = new Rectangle((int)x, (int)y, (int)spriteWidth, (int)spriteHeight);
		if(didNotHitEnemy || (isHitting && !isPiercing)) {
			removeMe = true;
		}
		if(isHitting) {
			isHitting = false;
		}
		if(didNotHitEnemy) {
			didNotHitEnemy = false;
		}
		if(projectileID == BULLET) {
			if (hitbox.intersects(Protagonist.hitbox)) {
				if (!Protagonist.isInvincible() && !Protagonist.invulnerable) {
					Protagonist.isHit = true;
					Protagonist.lastDamageTaken = (int)((float)(this.damage)*damageMultiplier);
					Protagonist.timeSinceLastDamage = System.currentTimeMillis();
				}
				removeMe = true;
			}
		}
		if(projectileID == ETHER) {
			if (hitbox.intersects(Protagonist.hitbox) && Protagonist.isMoving) {
				if (!Protagonist.isInvincible() && !Protagonist.invulnerable) {
					Protagonist.isHit = true;
					Protagonist.lastDamageTaken = (int)((float)(this.damage)*damageMultiplier);
					Protagonist.timeSinceLastDamage = System.currentTimeMillis();
				}
				removeMe = true;
			}
		}
		if(projectileID == VAMP) {
			if (hitbox.intersects(Protagonist.hitbox)) {
				if (!Protagonist.isInvincible() && !Protagonist.invulnerable) {
					Protagonist.isHit = true;
					Protagonist.isVamped = true;
					Protagonist.lastDamageTaken = (int)((float)(this.damage)*damageMultiplier);
					Protagonist.timeSinceLastDamage = System.currentTimeMillis();
					if(basisEnemy!=null && !basisEnemy.getDead() && !basisEnemy.toBeRemoved()) {
						basisEnemy.setHP(basisEnemy.getHP()+Protagonist.lastDamageTaken);
					}
				}
				removeMe = true;
			}
		}
		if(projectileID == SPELL) {
			if(Crawling.enemies.size()>0) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					if (hitbox.intersects(Crawling.enemies.get(i).getHitbox()) && !hitEnemies.contains(Crawling.enemies.get(i)) && !removeMe) {
						isHitting = true;
						targetEnemy = Crawling.enemies.get(i);
						hitEnemies.add(Crawling.enemies.get(i));
						Crawling.enemies.get(i).takeDamage((int)((float)(this.damage)*damageMultiplier));
						break;
					}
				}
			}
			if(Crawling.bosses.size()>0) {
				for(int i=0; i<Crawling.bosses.size(); i++) {
					if (hitbox.intersects(Crawling.bosses.get(i).getHitbox()) && !hitBosses.contains(Crawling.bosses.get(i))) {
						isHitting = true;
						targetBoss = Crawling.bosses.get(i);
						hitBosses.add(Crawling.bosses.get(i));
						Crawling.bosses.get(i).takeDamage((int)((float)(this.damage)*damageMultiplier));
						break;
					}
				}
			}
		}
		//detects terrain collision and is removed
		if (Crawling.currentRoom.isTerrainCollide(x, y, Room.PROJECTILE_COLLIDE, spriteWidth, spriteHeight)) {
			if(!isSpectral) {
				didNotHitEnemy = true;
				isExpired = true;
			}
		}
		//detects if range has expired and is removed
		if (distTraveled > range) {
			didNotHitEnemy = true;
			isExpired = true;
		}
		
		//detects if lifetime has expired and is removed
		if (System.currentTimeMillis()-timeSpawned>10000) {
			didNotHitEnemy = true;
			isExpired = true;
		}
		//moves projectile with slowmode
		if(isSlowed) {
			timeSinceLastSlow = System.currentTimeMillis();
			isSlowed = false;
		}
		Input input = container.getInput();
		float walkspeed = speed;
		if (input.isKeyDown(Input.KEY_SPACE) && Protagonist.slowmodeCharge > 0) {
			walkspeed /= Protagonist.slowmodeFactor;
		}
		if (System.currentTimeMillis()-timeSinceLastSlow<SLOW_DURATION) {
			walkspeed -= speed*slowFactor;
		}
		xIncrement = (float)(walkspeed*Math.cos(Math.toRadians(directionAngle)));
		yIncrement = (float)(walkspeed*Math.sin(Math.toRadians(directionAngle)));
		x += xIncrement;
		y += yIncrement;
		distTraveled += walkspeed;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public void setDirectionAngle(float directionAngle) {
		this.directionAngle = directionAngle;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public float getDamageMultiplier() {
		return damageMultiplier;
	}
	public float[] getDamageMultiplierList() {
		return damageMultiplierList;
	}
	public void setDamageMultiplier(int index, float multiplier) {
		damageMultiplierList[index] = multiplier;
		float temp = 1;
		for(int i=0; i<damageMultiplierList.length; i++) {
			temp *= damageMultiplierList[i];
		}
		damageMultiplier = temp;
	}
	public float getDistTraveled() {
		return distTraveled;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getXIncrement() {
		return xIncrement;
	}
	public float getYIncrement() {
		return yIncrement;
	}
	public int getSpriteWidth() {
		return spriteWidth;
	}
	public int getSpriteHeight() {
		return spriteHeight;
	}
	public double getShotDirection() {
		return directionAngle;
	}
	public float getPolarRadius() {
		return polarRadius;
	}
	public void setPolarRadius(float polarRadius) {
		this.polarRadius = polarRadius;
	}
	public int getRotationOrientation() {
		return rotationOrientation;
	}
	public void setRotationOrientation(int rot) {
		this.rotationOrientation = rot;
		isRotationSet = true;
	}
	public boolean isRotationSet() {
		return isRotationSet;
	}
	public int getFrameCounter() {
		return frameCounter;
	}
	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}
	public void iterateFrameCounter() {
		frameCounter++;
		frameCounter%=360;
	}
	public Rectangle getHitbox() {
		return hitbox;
	}
	public int getProjectileID() {
		return projectileID;
	}
	public void setProjectileID(int id) {
		projectileID = id;
	}
	public ArrayList<Enemy> getHitEnemies() {
		return hitEnemies;
	}
	public ArrayList<Boss> getHitBosses() {
		return hitBosses;
	}
	public boolean getHitting() {
		return isHitting;
	}
	public boolean getDidNotHit() {
		return didNotHitEnemy;
	}
	public boolean getExpired() {
		return isExpired;
	}
	public Enemy getBasisEnemy() {
		return basisEnemy;
	}
	public void setBasisEnemy(Enemy e) {
		basisEnemy = e;
	}
	public Enemy getTargetEnemy() {
		return targetEnemy;
	}
	public Boss getTargetBoss() {
		return targetBoss;
	}
	public boolean toBeRemoved() {
		return removeMe;
	}
	public void setRemove() {
		removeMe = true;
	}
	public void makeSpectral() {
		isSpectral = true;
	}
	public void makePiercing() {
		isPiercing = true;
	}
	public void setSlow(float factor) {
		isSlowed = true;
		slowFactor = factor;
	}
}
