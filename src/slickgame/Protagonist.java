package slickgame;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Protagonist {
	
	private Animation sprite;
	private static final int[] DURATION = {300, 300};
	static float x = 448f, y = 256f;
	static int SPRITEWIDTH = 42;
	static int SPRITEHEIGHT = 51;
	static Rectangle hitbox;
	
	
	Image[] movementDown = {new Image("images/isaac.png").getSubImage(130, 21, 28, 34), 
			 new Image("images/isaac.png").getSubImage(33, 21, 28, 34)};
	Image[] movementUp = {new Image("images/isaac.png").getSubImage(130, 213, 28, 34), 
			 new Image("images/isaac.png").getSubImage(33, 213, 28, 34)};
	Image[] movementRight = {new Image("images/isaac.png").getSubImage(130, 149, 28, 34), 
			 new Image("images/isaac.png").getSubImage(33, 149, 28, 34)};
	Image[] movementLeft = {new Image("images/isaac.png").getSubImage(130, 87, 28, 34), 
			 new Image("images/isaac.png").getSubImage(33, 87, 28, 34)};
	
	Image[] hitFrameDown = {new Image("images/isaac.png").getSubImage(130, 21, 28, 34),  
			new Image("images/isaac.png").getSubImage(0, 0, 28, 34)};
	Image[] hitFrameUp = {new Image("images/isaac.png").getSubImage(130, 213, 28, 34),  
			new Image("images/isaac.png").getSubImage(0, 0, 28, 34)};
	Image[] hitFrameRight = {new Image("images/isaac.png").getSubImage(130, 149, 28, 34),  
			new Image("images/isaac.png").getSubImage(0, 0, 28, 34)};
	Image[] hitFrameLeft = {new Image("images/isaac.png").getSubImage(130, 87, 28, 34),  
			new Image("images/isaac.png").getSubImage(0, 0, 28, 34)};

	Animation down = new Animation(movementDown, DURATION, false);
	Animation up = new Animation(movementUp, DURATION, false);
	Animation right = new Animation(movementRight, DURATION, false);
	Animation left = new Animation(movementLeft, DURATION, false);
	Animation hitDown = new Animation(hitFrameDown, new int[] {250,250}, true);
	Animation hitUp = new Animation(hitFrameUp, new int[] {250,250}, true);
	Animation hitRight = new Animation(hitFrameRight, new int[] {250,250}, true);
	Animation hitLeft = new Animation(hitFrameLeft, new int[] {250,250}, true);
	
	static int HP;
	static int MAXHP;
	private static final int BASEHP = 60;
	static int shield;
	static long fireRateDelay;
	private static final long BASE_FRD = 350;
	static float shotSpeed;
	private static final float BASE_SHOTSPEED = 6f;
	static float speed;
	private static final float BASESPEED = 0.33f;
	static int damage;
	static final int BASEDAMAGE = 10;
	static float range;
	private static final float BASERANGE = 450f;
	static float RUNSHOOT_BONUS = 1f;
	
	static ArrayList<Item> obtainedItems = new ArrayList<Item>();
	static boolean hasWeapon = false;
	static boolean weaponCharging = false;
	static boolean weaponCharged = false;
	static long timeSinceWeaponCharge = 0;
	
	static boolean isMoving = false;
	static boolean isAttacking = false;
	
	static long timeSinceLastSpell = 0;
	static long timeSinceLastDamage = 0;
	static long IFRAMES = 1200;
	static boolean isFiring = false;
	static boolean isHit = false; //one-frame indicator for damage
	static boolean isVamped = false;
	static boolean tookDamageThisRoom = false;
	static int lastDamageTaken;
	static boolean invulnerable;
	
	static float slowmodeCharge;
	static float maxSlowmodeCharge;
	private static final float BASE_SLOWMODE = 240f;
	static double slowmodeFactor = 1.8;
	
	static int shotNumber = 0;
	static int shotDirection;
	static float fireRateMultiplier = 1;
	private static float[] fireRateMultiplierList = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; //11
		//soy milk, 20/20, epiphora, polyphemus, lean shotgun, paschal candle, orb of light, norepi, leany veins, berserker
	private static float speedMultiplier = 1;
	private static float[] speedMultiplierList = {1, 1}; //adrenaline, abaddon
	private static float shotSpeedMultiplier = 1;
	private static float[] shotSpeedMultiplierList = {1}; //ghostly
	public static boolean isFlying = false;
	public static boolean hasSpectral = false;
	public static boolean hasPiercing = false;
	public static int hasMitre = 0;

	public Protagonist() throws SlickException {
		sprite = down;
		MAXHP = BASEHP;
		HP = MAXHP;
		shield = 20;
		fireRateDelay = BASE_FRD;
		shotSpeed = BASE_SHOTSPEED;
		speed = BASESPEED;
		damage = BASEDAMAGE;
		range = BASERANGE;
		maxSlowmodeCharge = BASE_SLOWMODE;
		slowmodeCharge = maxSlowmodeCharge;
//		obtainedItems.add(new Item());
//		Item.itemPool.removeIf(i -> i==obtainedItems.get(0).getID());
	}
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(weaponCharged) {
			sprite.draw((int) x,(int) y, SPRITEWIDTH, SPRITEHEIGHT, Color.red);
		} else if (isFlying) {
			sprite.draw((int) x,(int) y, SPRITEWIDTH, SPRITEHEIGHT, Color.lightGray);
		} else {
			sprite.draw((int) x,(int) y, SPRITEWIDTH, SPRITEHEIGHT);
		}
//		g.setColor(Color.magenta);
//		g.fillRect((int)(Protagonist.x + Protagonist.SPRITEWIDTH/3), (int)(Protagonist.y + Protagonist.SPRITEHEIGHT/3), (int)1*Protagonist.SPRITEWIDTH/3, (int)1*Protagonist.SPRITEHEIGHT/3);
		g.setColor(Color.white);
	}
	public void update(GameContainer container, StateBasedGame game, int delta, Crawling crawling) throws SlickException {
		
		Input input = container.getInput();
		
		//check if protagonist is moving or shooting
		isMoving = false;
		isAttacking = false;
		if(input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_A)) {
			isMoving = true;
		}
		if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_LEFT)) {
			isAttacking = true;
		}
		
		//protagonist items take effect
		for (int i=0; i<obtainedItems.size(); i++) {
			obtainedItems.get(i).effect(container, game, delta, crawling);
		}
		
		this.movement(container, game, delta);
		this.attack(shotNumber, container, delta);
		
		if(HP>MAXHP) {
			HP = MAXHP;
		}
		if(HP<0) {
			HP=0;
		}
		if(shield<0) {
			shield=0;
		}
		
		//protagonist takes contact damage
		if(isHit) {
			tookDamageThisRoom = true;
			isHit = false;
			if(shield > 0 && !isVamped) {
				shield -= lastDamageTaken;
			} else if(HP==0 && isVamped) {
				shield -= lastDamageTaken;
			} else {
				HP -= lastDamageTaken;
			}
		}
		if(isVamped) {
			isVamped = false;
		}
		hitbox = new Rectangle((int)(Protagonist.x + Protagonist.SPRITEWIDTH/3), (int)(Protagonist.y + Protagonist.SPRITEHEIGHT/3), (int)1*Protagonist.SPRITEWIDTH/3, (int)1*Protagonist.SPRITEHEIGHT/3);	
		if (!isInvincible() && !invulnerable) {
			if(Crawling.enemies.size()>0) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					if (hitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
						isHit = true;
						lastDamageTaken = Crawling.enemies.get(i).getContactDamage();
						timeSinceLastDamage = System.currentTimeMillis();
						break;
					}
				}
			}
			if(Crawling.bosses.size()>0) {
				for(int i=0; i<Crawling.bosses.size(); i++) {
					if (hitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
						isHit = true;
						lastDamageTaken = Crawling.bosses.get(i).getContactDamage();
						timeSinceLastDamage = System.currentTimeMillis();
						break;
					}
				}
			}
		}
		
		//protagonist dies
		if (HP+shield <= 0) {
			game.enterState(Runner.GAMEOVER);
		}
	}
	public void movement(GameContainer container, StateBasedGame game, int delta) {
		Input input = container.getInput();
		float walkspeed = Math.min(delta*speed*speedMultiplier, delta*0.7f);
		if (input.isKeyDown(Input.KEY_SPACE) && slowmodeCharge > 0) {
			walkspeed /= slowmodeFactor;
			slowmodeCharge--;
		}
		if (!input.isKeyDown(Input.KEY_SPACE) && slowmodeCharge < maxSlowmodeCharge) {
			slowmodeCharge+=0.6f;
		}
		if (input.isKeyDown(Input.KEY_W))
		{
			if(!input.isKeyDown(Input.KEY_S)) {
				sprite.update(delta);
			}
		    // The lower the delta the slower the sprite will animate.
		    if (!Crawling.currentRoom.isTerrainCollide(x,y-walkspeed,Room.PROTAG_COLLIDE)) {
		    	if(input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_D)) {
		    		y -= walkspeed*0.71f;
		    	}
		    	else {
		    		y -= walkspeed;
		    	}
		    }
		}
		if (input.isKeyDown(Input.KEY_S))
		{
			if(!input.isKeyDown(Input.KEY_W)) {
				sprite.update(delta);
			}
		    if (!Crawling.currentRoom.isTerrainCollide(x,y+walkspeed,Room.PROTAG_COLLIDE)) {
		    	if(input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_D)) {
		    		y += walkspeed*0.71f;
		    	}
		    	else {
		    		y += walkspeed;
		    	}
		    }  
		}
		if (input.isKeyDown(Input.KEY_A))
		{
			if(!input.isKeyDown(Input.KEY_D)) {
				sprite.update(delta);
			}
		    if (!Crawling.currentRoom.isTerrainCollide(x-walkspeed,y,Room.PROTAG_COLLIDE)) {
		    	if(input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_S)) {
		    		x -= walkspeed*0.71f;
		    	}
		    	else {
		    		x -= walkspeed;
		    	}
		    }
		}
		if (input.isKeyDown(Input.KEY_D))
		{
			if(!input.isKeyDown(Input.KEY_A)) {
				sprite.update(delta);
			}
		    if (!Crawling.currentRoom.isTerrainCollide(x+walkspeed,y,Room.PROTAG_COLLIDE)) {
		    	if(input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_S)) {
		    		x += walkspeed*0.71f;
		    	}
		    	else {
		    		x += walkspeed;
		    	}
		    }
		}
	}
	public void attack(int shotNumber, GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		sprite = down;
		if (isInvincible())
		{
			sprite = hitDown;
		}
		if(isFiring) {
			isFiring=false;
		}
		if (!hasWeapon) {
			if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_LEFT)) {
				shotDirection = 0;
				if(input.isKeyDown(Input.KEY_UP)) {
					sprite = up;
					if (isInvincible())
					{
						sprite = hitUp;
					}
					if(input.isKeyDown(Input.KEY_A)) {
						shotDirection = 255;
					}
					else if(input.isKeyDown(Input.KEY_D)) {
						shotDirection = 285;
					}
					else {
						shotDirection = 270;
					}
				}
				if(input.isKeyDown(Input.KEY_RIGHT)) {
					sprite = right;
					if (isInvincible())
					{
						sprite = hitRight;
					}
					if(input.isKeyDown(Input.KEY_W)) {
						shotDirection = 345;
					}
					else if(input.isKeyDown(Input.KEY_S)) {
						shotDirection = 15;
					}
					else {
						shotDirection = 0;
					}
				}
				if(input.isKeyDown(Input.KEY_DOWN)) {
					sprite = down;
					if (isInvincible())
					{
						sprite = hitDown;
					}
					if(input.isKeyDown(Input.KEY_D)) {
						shotDirection = 75;
					}
					else if(input.isKeyDown(Input.KEY_A)) {
						shotDirection = 105;
					}
					else {
						shotDirection = 90;
					}
				}
				if(input.isKeyDown(Input.KEY_LEFT)) {
					sprite = left;
					if (isInvincible())
					{
						sprite = hitLeft;
					}
					if(input.isKeyDown(Input.KEY_S)) {
						shotDirection = 165;
					}
					else if(input.isKeyDown(Input.KEY_W)) {
						shotDirection = 195;
					}
					else {
						shotDirection = 180;
					}
				}
				if(System.currentTimeMillis()-timeSinceLastSpell > Math.max((int)(((float)fireRateDelay)*fireRateMultiplier), (int)(((float)fireRateDelay)*fireRateMultiplier)*shotNumber) || timeSinceLastSpell==0) {
					Protagonist.shoot(x+SPRITEWIDTH/4, y+SPRITEHEIGHT/3, shotDirection, container, delta);
				}
			}
		}
	}
	public static void shoot(float x, float y, int shotDirection, GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		float projShotSpeed = shotSpeed*shotSpeedMultiplier;
		RUNSHOOT_BONUS = delta*speed*speedMultiplier;
		if((input.isKeyDown(Input.KEY_W) && shotDirection == 270) || (input.isKeyDown(Input.KEY_D) && shotDirection == 0) || (input.isKeyDown(Input.KEY_S) && shotDirection == 90) || (input.isKeyDown(Input.KEY_A) && shotDirection == 180)) {
			projShotSpeed += speed*RUNSHOOT_BONUS;
		}
		switch(shotNumber) {
			case 0: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 1: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 2: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+3, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-3, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 3: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+6, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-6, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 4: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+8, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+3, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-3, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-8, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 5: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+10, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+5, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-5, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-10, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 6: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+12, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+7, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+2, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-2, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-7, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-12, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 7: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+14, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+9, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+4, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-4, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-9, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-14, projShotSpeed, damage, range, Projectile.SPELL));
				break;
			case 9: Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+18, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+14, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+10, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection+5, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-5, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-10, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-14, projShotSpeed, damage, range, Projectile.SPELL));
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, x, y, shotDirection-18, projShotSpeed, damage, range, Projectile.SPELL));
				break;
		}
		isFiring=true;
		timeSinceLastSpell = System.currentTimeMillis();
	}
	public static boolean isInvincible() {
		if (System.currentTimeMillis()-timeSinceLastDamage<IFRAMES) {
			return true;
		}
		return false;
	}
	public static void makeInvincible() {
		
	}
	public static void setFireRateMultiplier(int index, float multiplier) {
		fireRateMultiplierList[index] = multiplier;
		float temp = 1;
		for(int i=0; i<fireRateMultiplierList.length; i++) {
			temp *= fireRateMultiplierList[i];
		}
		fireRateMultiplier = temp;
	}
	public static void setSpeedMultiplier(int index, float multiplier) {
		speedMultiplierList[index] = multiplier;
		float temp = 1;
		for(int i=0; i<speedMultiplierList.length; i++) {
			temp *= speedMultiplierList[i];
		}
		speedMultiplier = temp;
	}
	public static void setShotSpeedMultiplier(int index, float multiplier) {
		shotSpeedMultiplierList[index] = multiplier;
		float temp = 1;
		for(int i=0; i<shotSpeedMultiplierList.length; i++) {
			temp *= shotSpeedMultiplierList[i];
		}
		shotSpeedMultiplier = temp;
	}
	public static void reset() {
		x = 448f;
		y = 256f;
		
		obtainedItems = new ArrayList<Item>();
		hasWeapon = false;
		weaponCharging = false;
		weaponCharged = false;
		timeSinceWeaponCharge = 0;
		
		timeSinceLastSpell = 0;
		timeSinceLastDamage = 0;
		IFRAMES = 1200;
		isFiring = false;
		isHit = false; //one-frame indicator for damage
		tookDamageThisRoom = false;
		
		slowmodeFactor = 1.8;
		
		shotNumber = 0;
		fireRateMultiplier = 1;
		fireRateMultiplierList = new float[]{1, 1, 1, 1, 1, 1}; //soy milk, 20/20, epiphora, polyphemus, lean shotgun, paschal candle
		speedMultiplier = 1;
		speedMultiplierList = new float[]{1, 1}; //adrenaline, abaddon
		shotSpeedMultiplier = 1;
		shotSpeedMultiplierList = new float[]{1}; //ghostly
		isFlying = false;
		hasSpectral = false;
	}
}
