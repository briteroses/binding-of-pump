package slickgame;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.*;
/** Item list:
 * 0 - Lump of Coal - spells increase in damage and size at further range
 * 1 - Proptosis - spells start with massive damage and decrease in damage and size at further range
 * 2 - Triple Shot
 * 3 - Dead Dove - spells pass through walls
 * 4 - Sagittarius - spells pass through enemies
 * 5 - Soy Milk - spells have extremely high fire rate, extremely low damage
 * 6 - Double Shot
 * 7 - Quad Shot
 * 8 - God's Flesh - randomly raises three stats, lowers three stats
 * 9 - Explosivo - small chance of shooting tear which explodes into ring of tears upon expiry
 * 10 - Vampirism - small chance to heal when enemy killed
 * 11 - Revenge Eraser - removes all projectiles in room when protagonist takes damage
 * 12 - Revenge Trap - deals small damage to all enemies in room when protagonist takes damage
 * 13 - Close Kodak - orbital with small radius that deals contact damage
 * 14 - Far Kodak - orbital with far radius that deals contact damage
 * 15 - Buddy Smokepurpp - controllable familiar that deals contact damage
 * 16 - Bloodlust 1 - increases damage with killcount
 * 17 - Epiphora - increases fire rate with killcount
 * 18 - Bloodlust 2 - increases damage multiplier with rooms cleared
 * 19 - Gray Hourglass - doubles effectiveness of slowmode (twice as slow)
 * 20 - Blue Hourglass - doubles slowmode charge
 * 21 - Heart Pendant - enemies have small chance to drop heart when killed
 * 22 - Polyphemus - extremely large tears with low fire rate that pierce with lowered damage
 * 23 - Treasure Map - reveals map
 * 24 - Spider Shot - chance to slow enemy on hit (slows walk only, not shots)
 * 25 - Medusa Shot - chance to freeze enemy on hit
 * 26 - Adrenaline - multiplies damage and speed when HP is low
 * 27 - Lean Shotgun - replaces spells with a charged shotgun attack
 * 28 - Anti-Gravity - holding fire key holds all spells in place
 * 29 - Crown of Light - if HP is maxed, doubles damage for current room until damage is taken
 * 30 - Holy Mantle - negates the first damage taken in a room
 * 31 - Mascara - doubles damage but halves fire rate and shot speed
 * 32 - Ghostly - spectral and piercing tears with low shot speed
 * 33 - Leany Veins - releases 10-shot spell ring when damage is taken
 * 34 - Dunce Cap - shoot two shots at 45-deg angles
 * 35 - Dead Eye - rewards accuracy with damage multiplier
 * 36 - The Virus - protagonist deals contact damage
 * 37 - Superhot - all enemies and bullets freeze while protagonist is not performing action
 * 38 - Occult Eye - tears are controlled
 * 39 - Paschal Candle - rewards fire rate multiplier for consecutive rooms cleared without taking damage
 * 40 - Menorah - taking damage increases shot number until 7, starting over again at 0
 * 41 - Abaddon - grants huge damage and speed boost at the cost of most max HP
 * 42 - <3 - adds max HP and fully restores health
 * 43 - Aquarius - leaves trail of tears
 * 44 - The Belt - boosts speed
 * 45 -  of the Martyr - boosts damage
 * 46 - Blue Cap - max HP up, fire rate up, shot speed down
 * 47 - Lard - large max HP boost at the cost of speed down
 * 48 - Cat-o-nine Tails - damage and shot speed up
 * 49 - Cricket's Body - splash damage when tears hit enemy or expire range
 * 50 - Cricket's Head - grants damage multiplier
 * 51 - Growth Hormones - damage and speed boost
 * 52 - Halo - boosts all stats
 * 53 - Holy Grail - grants flying and boosts HP
 * 54 - Infamy - small chance to negate damage taken
 * 55 - Contact Lens - spells can cancel out with bullets
 * 56 - Bowtie - HP boost
 * 57 - Magic 8 Ball - shot speed boost
 * 58 - Magic Mushroom - some stats up and damage multiplier
 * 59 - Magic Scab - HP boost
 * 60 - Big Meat - Damage boost and small HP boost
 * 61 - The Contract - Sets max HP to very small value, destroys all enemies if player takes damage below max HP
 * 62 - Heels - range boost
 * 63 - Codeine Piss - doubles fire rate at the cost of range
 * 64 - Fat Shroom - HP, damage, range small boosts
 * 65 - Skinny Shroom - large fire rate boost with small damage down
 * 66 - Bandaid - HP boost
 * 67 - Ouija - grants spectral tears
 * 68 - Pentagram - large damage boost
 * 69 - Pisces - fire rate boost
 * 70 - Steroids - speed and range boost
 * 71 - Sad Onion - fire rate boost
 * 72 - The Screw - fire rate and shot speed boost
 * 73 - The Small Rock - damage and fire rate boost at the cost of some speed
 * 74 - Speed Ball - speed and shot speed boost
 * 75 - Spirit of the Night - grants flying and piercing
 * 76 - Steven - damage boost
 * 77 - Stigmata - HP and damage boost
 * 78 - Synthoil - damage and range boost
 * 79 - Continuum - spells loop through walls
 * 80 - Godhead - spells gain a damaging halo
 * 81 - Monstrance - protagonist gains a damaging halo
 * 82 - Censer - protagonist gains a slowing halo
 * 83 - Angelic Prism - orbiting prism that splits spells into four
 * 84 - Lead Pencil - creates spurt of spells for every 20 spells shot
 * 85 - Contagion - enemies release ring of spells when killed
 * 86 - Kidney Stone - creates concentrated spurt of spells at small chance for every shot fired
 * 87 - Little Horn - small chance to shoot instakilling spell
 * 88 - Succubus - familiar flies around room, projecting shadow that damages enemies and buffs player damage
 * 89 - Athame - protagonist activated shadow when hit that damages enemies and creates hearts when it kills enemies
 * 90 - Blood Rights - dramatically increases player invincibility frames
 * 91 - Immaculate Heart - chance to generate an orbiting spell with every spell fired
 * 92 - Tiny Planet - all spells are orbiting and spectral
 * 93 - Saturn - generates 7 orbiting spectral piercing spells when entering room
 * 94 - Orb of Light - if HP is maxed, grants large fire rate bonus until damage is taken in room
 * 95 - Norepi - multiplies fire rate when HP is low
 * 96 - Bloodlust 3 - taking damage boosts damage for the remaining floor
 * 97 - Celtic Cross - chance to trigger protective shield upon getting hit
 * 98 - Polaroid - triggers protective shield upon getting hit at low HP
 * 99 - The Pact - grants huge fire rate boost at the cost of most max HP
 * 100 - Wafer - all damage to player is halved
 * 101 - Mark - pay some HP for damage boost and shield
 * 102 - Relic - drops shield every 6 rooms completed
 * 103 - Crystal Ball - reveals map, small chance to heal shield when room completed
 * 104 - Mitre - increases drop chance of shield
 * 105 - Ceremonial Robes - pay some HP for damage boost and shield
 * 106 - Tanooki - player turns invulnerable when performing no action for an extended period of time
 * 107 - Black Lotus - player gains max HP and shield
 * 108 - Gimpy - chance to heal shield when hit
 * 109 - Rosary - gain large amount of shield
 * 110 - Blanket - gain shield, full HP heal, negated first hit in boss room
 * 111 - Parasite - spells split into two new spells when hitting enemy or on expiry
 * 112 - Empty Vessel - when HP is low, gain flying and a periodic protective shield
 * 113 - Stopwatch - freezes all enemies in room when player takes damage
 * 114 - Common Cold - chance to shoot poison spells
 * 115 - Serpent's Kiss - chance to shoot serpent poison spells and deals serpent poison to enemies in contact with player. When serpent poisoned enemies die, they have a small chance to drop shield.
 * 116 - Toxic Shock - all enemies are poisoned when player first enters room
 * 117 - Mirror - spells rebound like boomerangs
 * 118 - Midas Touch - player deals heavy contact damage, freezes enemies in contact
 * 119 - Broken Watch - slows all enemies in room when player takes damage
 * 120 - Eye of Belial - grants piercing spells, spells double in damage and rebound after piercing one enemy
 * 121 - Maw of the Void - pay max HP for ability to charge shadow aura via continuous fire. Shadow deals damage to enemies and has a small chance to drop shields after killing each enemy.
 * 122 - Lil Zel - fast-orbiting buddy that blocks shots and deals contact damage
 * 123 - Lil Lil Pump - orbiting buddy that deals small contact damage and shoots alongside player
 * 124 - Lil Purpp - orbiting buddy that targets and auto-fires at nearby enemies
 * 125 - Apple - small chance to shoot high-damage spell
 * 126 - Poison Brain - player gains poison aura while shooting
 * 127 - Squeezy - boosts fire rate, grants shield
 * 128 - Dolly - boosts range and fire rate, spawns three hearts or shields
 * 129 - Laced Xan - boosts speed, shrinks player sprite
 * 130 - Binky - boosts fire rate, grants shield, decreases range, shrinks player sprite
 * 131 - Yum Heart - drops heart every 6 rooms completed
 * 132 - Scapular - grants shield if player takes damage while at low HP
 * 133 - Raw Liver - dramatically boosts max HP
 * 134 - Stem Cells - boosts max HP, small heal
 * 135 - Placenta - boosts max HP, extremely low chance to heal HP every frame
 * 136 - Brittle Heart - massive boost to max HP, fully heals. When player dips 15 HP below max HP, max HP decreases permanently.
 * 137 - The Pin - range, shot speed, and speed boost
 * 138 - Mini Mushroom - range and speed boost, shrinks player sprite
 * 139 - Blood Bag - boosts max HP and speed, massive heal
 * 140 - Cancer - grants shield; if hit, grants partial defense against damage for the rest of the room
 * 141 - Berserker - if hit, grants increased damage and fire rate for a short time
 */
public class Item {
	private int itemID;
	private Image sprite;
	private float x;
	private float y;
	private int spriteWidth = 64;
	private int spriteHeight = 64;
	private Rectangle hitbox;
	
	private boolean onPickup = true; //triggers pickup effects for items with continuous effects
	
	static ArrayList<Integer> itemPool = new ArrayList<Integer>(); //contains IDs rather than items
	
	private boolean removeMe;
	
	//HELPER VARIABLES
	private int frameCounter = 0; //helper for continuous motions with time
	private float statModifier = 1;
	private boolean effectToggle = false;
	private long timeSinceLastTrigger = 0;
	private long timeSinceLastBullet = 0;
	private double angle;
	private ArrayList<Projectile> specialProjectiles = new ArrayList<Projectile>(); //helper for items that create their own spells with special effects
	private ArrayList<Projectile> interactionProjectiles = new ArrayList<Projectile>();
	private ArrayList<Circle> halos = new ArrayList<Circle>();
	
	static final int NUMBER_OF_ITEMS = 142;
	static final int[] weaponItems = {27, 34};
	static final int[] familiarItems = {13, 14, 15, 83, 88, 122, 123, 124};
	static final int[] haloItems = {80, 81, 82, 87};
	static final int[] shadowItems = {88, 89, 121};
	static final int[] protItems = {97, 98, 106, 112};
	static final int[] poisonItems = {114, 115, 116, 126};

	public Item (int id) throws SlickException {
		this.itemID = id;
		this.x = 448f;
		this.y = 256f;
		sprite = new Image("images/items/" + itemID + ".png");
	}
	public Item () throws SlickException {
		this.itemID = itemPool.get((int)(Math.random()*itemPool.size()));
		sprite = new Image("images/items/" + itemID + ".png");
		this.x = 448f;
		this.y = 256f;
	}
	public Item (int x, int y) throws SlickException {
		this.itemID = itemPool.get(((int)(itemPool.size()*Math.random())));
		sprite = new Image("images/items/" + itemID + ".png");
		this.x = x;
		this.y = y;
	}
	public static void init(GameContainer container, StateBasedGame game) {
		itemPool.clear();
		Protagonist.obtainedItems.clear();
		for (int i=0; i<NUMBER_OF_ITEMS; i++) {
			itemPool.add(i);
		}
	}
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		sprite.draw(x, y, spriteWidth, spriteHeight);
	}
	public void update(GameContainer container, StateBasedGame game, int delta, Crawling crawling) {
		hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
		Rectangle protagHitbox = new Rectangle((int)(Protagonist.x + Protagonist.SPRITEWIDTH/6), (int)(Protagonist.y + Protagonist.SPRITEHEIGHT/6), (int)2*Protagonist.SPRITEWIDTH/3, (int)5*Protagonist.SPRITEHEIGHT/6);	
		if(hitbox.intersects(protagHitbox)) {
			itemPool.removeIf(i -> i==itemID);
			Protagonist.obtainedItems.add(this);
			removeMe = true;
		}
	}
	public void effect(GameContainer container, StateBasedGame game, int delta, Crawling crawling) throws SlickException {
		if(itemID==0) { //lump of coal
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(0, (1+Crawling.spells.get(i).getDistTraveled()/650));
			}
		}
		if(itemID==1) { //proptosis
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(1, (2-Crawling.spells.get(i).getDistTraveled()/650));
			}
		}
		if(itemID==2) { //triple shot
			if(onPickup) {
				Protagonist.shotNumber += 3;
				onPickup=false;
			}
		}
		if(itemID==3) { //dead dove
			if(onPickup) {
				Protagonist.isFlying = true;
				Protagonist.hasSpectral = true;
				onPickup=false;
			}
		}
		if(itemID==4) { //sagittarius
			Protagonist.hasPiercing = true;
		}
		if(itemID==5) { //soy milk
			Protagonist.setFireRateMultiplier(0, 0.15f);
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(2, 0.2f);
			}
		}
		if(itemID==6) { //20/20
			if(onPickup) {
				Protagonist.setFireRateMultiplier(1, 0.6f);
				Protagonist.shotNumber += 2;
				onPickup=false;
			}
		}
		if(itemID==7) { //quad shot
			if(onPickup) {
				Protagonist.shotNumber += 4;
				onPickup=false;
			}
		}
		if(itemID==8) { //god's flesh
			if(onPickup) {
				ArrayList<Integer> values = new ArrayList<Integer>();
				values.add(1); values.add(1); values.add(-1); values.add(-1); values.add(-1); values.add(-1);
				Collections.shuffle(values);
				Protagonist.MAXHP += 15*values.remove(0);
				Protagonist.fireRateDelay -= (long)(Protagonist.fireRateDelay*0.23)*values.remove(0);
				Protagonist.shotSpeed += 1.2f*values.remove(0);
				Protagonist.speed += 0.05f*values.remove(0);
				Protagonist.damage += 5*values.remove(0);
				Protagonist.range += 96f*values.remove(0);				
				onPickup=false;
			}
		}
		if(itemID==9) { //explosivo
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(!specialProjectiles.contains(Crawling.spells.get(i)) && Crawling.spells.get(i).getExpired() && Math.random()<0.15) {
					float xSource = Crawling.spells.get(i).getX()-2*Crawling.spells.get(i).getXIncrement();
					float ySource = Crawling.spells.get(i).getY()-2*Crawling.spells.get(i).getYIncrement();
					for(int k=0; k<10; k++) {
						Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, xSource+Projectile.BASE_SPRITEWIDTH/2, ySource+Projectile.BASE_SPRITEHEIGHT/2, 36*k, Protagonist.shotSpeed, Protagonist.damage, Protagonist.range/2, Projectile.SPELL);
						Crawling.spells.add(ringProjectile);
						specialProjectiles.add(ringProjectile);
					}
				}
				if(!specialProjectiles.contains(Crawling.spells.get(i)) && Crawling.spells.get(i).getHitting() && Math.random()<0.111) {
					float xSource = Crawling.spells.get(i).getX();
					float ySource = Crawling.spells.get(i).getY();
					Enemy targetEnemy = Crawling.spells.get(i).getTargetEnemy();
					Boss targetBoss = Crawling.spells.get(i).getTargetBoss();
					for(int k=0; k<10; k++) {
						Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, xSource+Projectile.BASE_SPRITEWIDTH/2, ySource+Projectile.BASE_SPRITEHEIGHT/2, 36*k, Protagonist.shotSpeed, Protagonist.damage, Protagonist.range/2, Projectile.SPELL);
						ringProjectile.getHitEnemies().add(targetEnemy);
						ringProjectile.getHitBosses().add(targetBoss);
						Crawling.spells.add(ringProjectile);
						specialProjectiles.add(ringProjectile);
					}
				}
			}
		}
		if(itemID==10) { //vampirism
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Crawling.enemies.get(i).getDead() && Math.random()<0.06) {
					if(Protagonist.HP + 15 <= Protagonist.MAXHP) {
						Protagonist.HP += 15;
					}
					else {
						Protagonist.HP = Protagonist.MAXHP;
					}
				}
			}
		}
		if(itemID==11) { //revenge eraser
			if(Protagonist.isHit && Math.random()<1) {
				Crawling.bullets.clear();
			}
		}
		if(itemID==12) { //revenge trap
			if(Protagonist.isHit && Math.random()<0.3) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).takeDamage((int)(((float)(80*(Protagonist.MAXHP-Protagonist.HP)))/Protagonist.MAXHP));
				}
			}
		}
		if(itemID==13) { //close kodak
			//orbits
			spriteWidth = 32;
			spriteHeight = 40;
			int radius = 40;
			frameCounter++;
			frameCounter %= 360;
			x = (Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(frameCounter)))*radius-spriteWidth/2;
			y = (Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(frameCounter)))*radius-spriteHeight/2;
			//deals contact damage and blocks shots
			Rectangle familiarHitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(familiarHitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
					Crawling.enemies.get(i).takeDamage(3);
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
					Crawling.bosses.get(i).takeDamage(3);
				}
			}
			for(int i=0; i<Crawling.bullets.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bullets.get(i).getHitbox()) && Crawling.bullets.get(i).getProjectileID()==Projectile.BULLET) {
					Crawling.bullets.get(i).setRemove();
				}
			}
		}
		if(itemID==14) { //far kodak
			//orbits
			spriteWidth = 32;
			spriteHeight = 40;
			int radius = 120;
			frameCounter++;
			frameCounter%=360;
			x = (Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(frameCounter)))*radius-spriteWidth/2;
			y = (Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(frameCounter)))*radius-spriteHeight/2;
			//deals contact damage
			Rectangle familiarHitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(familiarHitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
					Crawling.enemies.get(i).takeDamage(1);
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
					Crawling.bosses.get(i).takeDamage(1);
				}
			}
		}
		if(itemID==15) { //buddy smokepurpp
			spriteWidth = 48;
			spriteHeight = 48;
			//moves with the cursor
			Input input = container.getInput();
			float walkspeed = 3.5f;
			if (input.isKeyDown(Input.KEY_UP))
			{
			    if (y-walkspeed>Crawling.currentRoom.getTiledMap().getTileHeight()) {
			    	if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT)) {
			    		y -= walkspeed*0.71f;
			    	}
			    	else {
			    		y -= walkspeed;
			    	}
			    }
			}
			if (input.isKeyDown(Input.KEY_DOWN))
			{
			    if (y+walkspeed+spriteHeight<Crawling.currentRoom.getPixelHeight() - Crawling.currentRoom.getTiledMap().getTileHeight()) {
			    	if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT)) {
			    		y += walkspeed*0.71f;
			    	}
			    	else {
			    		y += walkspeed;
			    	}
			    }  
			}
			if (input.isKeyDown(Input.KEY_LEFT))
			{
			    if (x-walkspeed>Crawling.currentRoom.getTiledMap().getTileWidth()) {
			    	if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_DOWN)) {
			    		x -= walkspeed*0.71f;
			    	}
			    	else {
			    		x -= walkspeed;
			    	}
			    }
			}
			if (input.isKeyDown(Input.KEY_RIGHT))
			{
			    if (x+walkspeed+spriteWidth<Crawling.currentRoom.getPixelWidth() - Crawling.currentRoom.getTiledMap().getTileWidth()) {
			    	if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_DOWN)) {
			    		x += walkspeed*0.71f;
			    	}
			    	else {
			    		x += walkspeed;
			    	}
			    }
			}
			//deals contact damage
			Rectangle familiarHitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(familiarHitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
					if(frameCounter % 2 == 0) {
						Crawling.enemies.get(i).takeDamage(1);
					}
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
					if(frameCounter % 2 == 0) {
						Crawling.bosses.get(i).takeDamage(1);
					}
				}
			}
			//snaps to player location after room change
			if(Crawling.roomSwitching) {
				x = Protagonist.x;
				y = Protagonist.y;
			}
		}
		if(itemID==16) { //bloodlust 1
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.killCount<5) {
					Crawling.spells.get(i).setDamageMultiplier(3, 1f+0.1f*Crawling.killCount);
				} else {
					Crawling.spells.get(i).setDamageMultiplier(3, 1.5f);
				}
			}
		}
		if(itemID==17) { //epiphora
			if(Crawling.killCount<5) {
				Protagonist.setFireRateMultiplier(2, 1-0.06f*Crawling.killCount);
			} else {
				Protagonist.setFireRateMultiplier(2, 0.7f);
			}
		}
		if(itemID==18) { //bloodlust 2
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.roomsCleared<12) {
					Crawling.spells.get(i).setDamageMultiplier(4, 1f+0.05f*Crawling.roomsCleared);
				} else {
					Crawling.spells.get(i).setDamageMultiplier(4, 1.6f);
				}
			}
		}
		if(itemID==19) { //gray hourglass
			if(onPickup) {
				Protagonist.slowmodeFactor = 3.2;
				onPickup=false;
			}
		}
		if(itemID==20) { //blue hourglass
			if(onPickup) {
				Protagonist.maxSlowmodeCharge = 480f;
				Protagonist.slowmodeCharge = 480f;
				onPickup=false;
			}
		}
		if(itemID==21) { //heart pendant
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Crawling.enemies.get(i).getDead() && Math.random()<0.03) {
					Crawling.tempPickups.add(new Pickup("heart", Crawling.enemies.get(i).getX(), Crawling.enemies.get(i).getY()));
				}
			}
		}
		if(itemID==22) { //polyphemus
			Protagonist.hasPiercing = true;
			Protagonist.setFireRateMultiplier(3, 2.1f);
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(5, 2.3f);
				if(Crawling.spells.get(i).getHitting()) {
					if(Crawling.spells.get(i).getTargetEnemy() != null && 0>Crawling.spells.get(i).getTargetEnemy().getHP()) {
						Crawling.spells.get(i).setDamage((int)(((float)Crawling.spells.get(i).getDamage())-((float)(Crawling.spells.get(i).getTargetEnemy().getHP()+Crawling.spells.get(i).getDamage()*Crawling.spells.get(i).getDamageMultiplier()))/Crawling.spells.get(i).getDamageMultiplier()));
					}
					else {
						Crawling.spells.get(i).setRemove();
					}
				}
					
			}
		}
		if(itemID==23) { //treasure map
			UI.setTreasureMap();
		}
		if(itemID==24) { //spider shot
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.spells.get(i).getHitting() && Crawling.spells.get(i).getTargetEnemy() != null && Math.random()<0.2) {
					Crawling.spells.get(i).getTargetEnemy().setSlow(0.6f);
				}
			}
		}
		if(itemID==25) { //medusa shot
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.spells.get(i).getHitting() && Crawling.spells.get(i).getTargetEnemy() != null && Math.random()<0.12) {
					Crawling.spells.get(i).getTargetEnemy().setSlow(1f);
				}
			}
		}
		if(itemID==26) { //adrenaline
			Protagonist.setSpeedMultiplier(0, 1+0.2f*Math.max(0, 1-Protagonist.HP/((float)(Protagonist.MAXHP))));
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(6, 1+Math.max(0, 1-Protagonist.HP/((float)(Protagonist.MAXHP))));
			}
		}
		if(itemID==27) { //lean shotgun
			Protagonist.hasWeapon = true;
			Protagonist.setFireRateMultiplier(4, 5f);
			Input input = container.getInput();
			Protagonist.weaponCharging = false;
			if(Protagonist.isAttacking) {
				Protagonist.weaponCharging = true;
			}
			if(Protagonist.weaponCharging==false) {
				Protagonist.timeSinceWeaponCharge = System.currentTimeMillis();
			}
			if(Protagonist.isFiring) {
				Protagonist.isFiring=false;
			}
			if(System.currentTimeMillis()-Protagonist.timeSinceWeaponCharge>(long)((float)Protagonist.fireRateDelay*Protagonist.fireRateMultiplier)) {
				int shotDirection = 0;
				if(input.isKeyDown(Input.KEY_UP)) {
					shotDirection = 270;
				}
				if(input.isKeyDown(Input.KEY_RIGHT)) {
					shotDirection = 0;
				}
				if(input.isKeyDown(Input.KEY_DOWN)) {
					shotDirection = 90;
				}
				if(input.isKeyDown(Input.KEY_LEFT)) {
					shotDirection = 180;
				}
				int numberOfShots = (int) (Protagonist.shotNumber+7+4*Math.random());
				for(int i=0; i<numberOfShots; i++){
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Protagonist.SPRITEWIDTH/4, Protagonist.y+Protagonist.SPRITEHEIGHT/3, shotDirection-12+(int)(24*Math.random()), (float)(Protagonist.shotSpeed*(0.75+0.5*Math.random())), (int)(Protagonist.damage*(0.4+0.7*Math.random())), Protagonist.range, Projectile.SPELL));
				}
				Protagonist.isFiring = true;
				Protagonist.timeSinceWeaponCharge = System.currentTimeMillis();
			}
		}
		if(itemID==28) { //antigravity
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.2;
				onPickup=false;
			}
			//item interaction patch: immaculate heart, saturn
			Item[] interactions = {null, null}; //immaculate heart, saturn
			interactionProjectiles.clear();
			for(int i=0; i<Protagonist.obtainedItems.size(); i++) {
				if(Protagonist.obtainedItems.get(i).getID()==91) {
					interactions[0] = Protagonist.obtainedItems.get(i);
				}
				if(Protagonist.obtainedItems.get(i).getID()==93) {
					interactions[1] = Protagonist.obtainedItems.get(i);
				}
			}
			for(int i=0; i<2; i++) {
				if(interactions[i]!=null) {
					interactionProjectiles.addAll(interactions[i].getSpecialProjectiles());
				}
			}
			//overridden by tiny planet
			boolean hasTinyPlanet = false;
			for(int i=0; i<Protagonist.obtainedItems.size(); i++) {
				if(Protagonist.obtainedItems.get(i).getID()==92) {
					hasTinyPlanet=true;
				}
			}
			if(!hasTinyPlanet) {
				for (int i=0; i<Crawling.spells.size(); i++) {
					if(!interactionProjectiles.contains(Crawling.spells.get(i))) {
						Crawling.spells.get(i).setSpeed(Protagonist.shotSpeed);
					}
				}
				if(Protagonist.isAttacking) {
					for (int i=0; i<Crawling.spells.size(); i++) {
						if(!interactionProjectiles.contains(Crawling.spells.get(i))) {
							Crawling.spells.get(i).setSpeed(0);
						}
					}
				}
			}
		}
		if(itemID==29) { //crown of light
			if(Protagonist.HP == Protagonist.MAXHP && !Protagonist.tookDamageThisRoom) {
				for (int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setDamageMultiplier(7, 2);
				}
			}
		}
		if(itemID==30) { //holy mantle
			if(Protagonist.isHit && !Protagonist.tookDamageThisRoom) {
				Protagonist.lastDamageTaken = 0;
			}
		}
		if(itemID==31) { //mascara
			if(onPickup) {
				Protagonist.fireRateDelay *= 2;
				Protagonist.damage *= 2;
				Protagonist.shotSpeed /= 1.6;				
				onPickup=false;
			}
		}
		if(itemID==32) { //ghostly
			if(onPickup) {
				Protagonist.setShotSpeedMultiplier(0, 0.6f);
				Protagonist.hasSpectral = true;
				Protagonist.hasPiercing = true;	
				onPickup=false;
			}
		}
		if(itemID==33) { //leany veins
			if(Protagonist.isHit) {
				for(int k=0; k<20; k++) {
					Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Projectile.BASE_SPRITEWIDTH/2, Protagonist.y+Projectile.BASE_SPRITEHEIGHT/2, 18*k, Protagonist.shotSpeed, Protagonist.damage, Protagonist.range, Projectile.SPELL);
					Crawling.spells.add(ringProjectile);
				}
			}
			if (Protagonist.tookDamageThisRoom) {
				Protagonist.setFireRateMultiplier(9, 0.75f);
			} else {
				Protagonist.setFireRateMultiplier(9, 1);
			}
		}
		if(itemID==34) { //dunce cap
			Protagonist.hasWeapon = true;
			Input input = container.getInput();
			int shotNumberMultiplier = 1;
			if (Protagonist.shotNumber>0) {
				shotNumberMultiplier = Protagonist.shotNumber;
			}
			if(System.currentTimeMillis()-Protagonist.timeSinceLastSpell > (int)(((float)Protagonist.fireRateDelay)*Protagonist.fireRateMultiplier)*shotNumberMultiplier) {
				if(input.isKeyDown(Input.KEY_UP)){
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 225, container, delta);
					Protagonist.timeSinceLastSpell = 0;
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 315, container, delta);
				}
				else if(input.isKeyDown(Input.KEY_RIGHT)){
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 315, container, delta);
					Protagonist.timeSinceLastSpell = 0;
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 45, container, delta);
				}
				else if(input.isKeyDown(Input.KEY_DOWN)){
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 45, container, delta);
					Protagonist.timeSinceLastSpell = 0;
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 135, container, delta);
				}
				else if(input.isKeyDown(Input.KEY_LEFT)){
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 135, container, delta);
					Protagonist.timeSinceLastSpell = 0;
					Protagonist.shoot(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/3, 225, container, delta);
				}
			}
		}
		if(itemID==35) { //deadeye
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.spells.get(i).getHitting()) {
					if(statModifier<=1.875f) {
						statModifier += 0.125f;
					}
					else {
						statModifier = 2f;
					}
				}
				if(Crawling.spells.get(i).getDidNotHit() && Crawling.spells.get(i).getHitEnemies().size()+Crawling.spells.get(i).getHitBosses().size()==0) {
					if(statModifier>=1.125f) {
						statModifier -= 0.125f;
					}
					else {
						statModifier = 1f;
					}
				}
				Crawling.spells.get(i).setDamageMultiplier(8, statModifier);
			}
		}
		if(itemID==36) { //the virus
			frameCounter++;
			frameCounter %= 2;
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Crawling.enemies.get(i).getHitbox().intersects(Protagonist.hitbox)) {
					if(frameCounter==0) {
						Crawling.enemies.get(i).takeDamage(1);
					}
					if(Crawling.enemies.get(i).getDead() && Math.random()<0.3) {
						Crawling.tempPickups.add(new Pickup("heart", Crawling.enemies.get(i).getX(), Crawling.enemies.get(i).getY()));
					}
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(Crawling.bosses.get(i).getHitbox().intersects(Protagonist.hitbox)) {
					if(frameCounter==0) {
						Crawling.bosses.get(i).takeDamage(1);
					}
				}
			}
		}
		if(itemID==37) { //superhot
			Protagonist.maxSlowmodeCharge = 0;
			Protagonist.slowmodeCharge = 0;
			if(!Protagonist.isMoving && !Protagonist.isAttacking) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).setSlow(1f);
				}
				for(int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setSlow(1f);
				}
				for(int i=0; i<Crawling.bullets.size(); i++) {
					Crawling.bullets.get(i).setSlow(1f);
				}
			}
			else {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).setSlow(0);
				}
				for(int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setSlow(0);
				}
				for(int i=0; i<Crawling.bullets.size(); i++) {
					Crawling.bullets.get(i).setSlow(0);
				}
			}
		}
		if(itemID==38) { //occult eye
			if(onPickup) {
				Protagonist.damage += 3;
				Protagonist.range += 128f;
				onPickup = false;
			}
			Input input = container.getInput();
			if(!Protagonist.isAttacking) {
				for(int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setSlow(1f);
				}
			}
			else {
				for(int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setSlow(0);
				}
				if(input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_RIGHT)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(315);
					}
				}
				else if(input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_DOWN)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(45);
					}
				}
				else if(input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_LEFT)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(135);
					}
				}
				else if(input.isKeyDown(Input.KEY_LEFT) && input.isKeyDown(Input.KEY_UP)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(225);
					}
				}
				else if(input.isKeyDown(Input.KEY_UP)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(270);
					}
				}
				else if(input.isKeyDown(Input.KEY_RIGHT)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(0);
					}
				}
				else if(input.isKeyDown(Input.KEY_DOWN)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(90);
					}
				}
				else if(input.isKeyDown(Input.KEY_LEFT)) {
					for(int i=0; i<Crawling.spells.size(); i++) {
						Crawling.spells.get(i).setDirectionAngle(180);
					}
				}
			}
		}
		if(itemID==39) { //paschal candle
			if(Crawling.clearRoomToggle && statModifier>0.5f) {
				statModifier -= 0.1f;
			}
			if(Protagonist.isHit) {
				statModifier = 1;
			}
			Protagonist.setFireRateMultiplier(5, statModifier);
		}
		if(itemID==40) { //menorah
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.2f;
				onPickup=false;
			}
			if(Protagonist.shotNumber==0) {
				Protagonist.shotNumber=1;
			}
			if(Protagonist.isHit) {
				Protagonist.shotNumber++;
				Protagonist.shotNumber %= 8;
				if(Protagonist.shotNumber==0) {
					for(int i=0; i<30; i++) {
						Projectile temp = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Projectile.BASE_SPRITEWIDTH/2, Protagonist.y+Projectile.BASE_SPRITEHEIGHT/2, 360*Math.random(), 1+ (float) (2.5*Math.random()), Protagonist.damage, Protagonist.range, Projectile.SPELL);
						temp.makePiercing();
						Crawling.spells.add(temp);
					}
				}
			}
		}
		if(itemID==41) { //abaddon
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(10, 2);
			}
			Protagonist.setSpeedMultiplier(1, 1.2f);
			if(onPickup) {
				Protagonist.MAXHP = 15;
				if(Protagonist.HP>15) {
					Protagonist.HP=15;
				}
				Protagonist.shield+=75;
				onPickup=false;
			}
		}
		if(itemID==42) { //<3
			if(onPickup) {
				Protagonist.MAXHP += 15;
				Protagonist.HP = Protagonist.MAXHP;				
				onPickup=false;
			}
		}
		if(itemID==43) { //aquarius
			frameCounter++;
			frameCounter %= 24;
			if(frameCounter==0) {
				Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Projectile.BASE_SPRITEWIDTH/2, Protagonist.y+Projectile.BASE_SPRITEHEIGHT/2, 0, 0, Protagonist.damage, Protagonist.range, Projectile.SPELL));
			}
		}
		if(itemID==44) { //the belt
			if(onPickup) {
				Protagonist.speed += 0.05f;
				onPickup=false;
			}
		}
		if(itemID==45) { // of the martyr
			if(onPickup) {
				Protagonist.damage += 7;
				onPickup=false;
			}
		}
		if(itemID==46) { //blue cap
			if(onPickup) {
				Protagonist.MAXHP += 15;
				Protagonist.HP += 15;
				Protagonist.fireRateDelay -= (long)(Protagonist.fireRateDelay*0.23);
				Protagonist.shotSpeed -= 1.6f;				
				onPickup=false;
			}
		}
		if(itemID==47) { //lard
			if(onPickup) {
				Protagonist.MAXHP += 25;
				Protagonist.HP += 10;
				Protagonist.speed /= 1.28f;				
				onPickup=false;
			}
		}
		if(itemID==48) { //cat-o-nine tails
			if(onPickup) {
				Protagonist.damage += 5;
				Protagonist.shotSpeed += 1.2f;			
				onPickup=false;
			}
		}
		if(itemID==49) { //cricket's body
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.2f;
				Protagonist.range -= 128f;
				onPickup = false;
			}
			int randomAngle = (int)(90*Math.random());
			for(int i=0; i<Crawling.spells.size(); i++) {
				if((Crawling.spells.get(i).getExpired() || Crawling.spells.get(i).getHitting()) && !specialProjectiles.contains(Crawling.spells.get(i))) {
					float xSource = Crawling.spells.get(i).getX()-3*Crawling.spells.get(i).getXIncrement();
					float ySource = Crawling.spells.get(i).getY()-3*Crawling.spells.get(i).getYIncrement();
					for(int k=0; k<4; k++) {
						Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, xSource+Projectile.BASE_SPRITEWIDTH/2, ySource+Projectile.BASE_SPRITEHEIGHT/2, 90*k+randomAngle, Protagonist.shotSpeed, Protagonist.damage/3, 64f, Projectile.SPELL);
						Crawling.spells.add(ringProjectile);
						specialProjectiles.add(ringProjectile);
					}
				}
			}
		}
		if(itemID==50) { //cricket's head
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(9, 1.5f);
			}
		}
		if(itemID==51) { //growth hormones
			if(onPickup) {
				Protagonist.damage += 5;
				Protagonist.speed += 0.05f;				
				onPickup=false;
			}
		}
		if(itemID==52) { //halo
			if(onPickup) {
				Protagonist.MAXHP += 10;
				Protagonist.HP += 10;
				Protagonist.fireRateDelay -= (long)(Protagonist.fireRateDelay*0.12);
				Protagonist.shotSpeed += 0.6f;
				Protagonist.speed += 0.03f;
				Protagonist.damage += 3;
				Protagonist.range += 64f;				
				onPickup=false;
			}
		}
		if(itemID==53) { //holy grail
			if(onPickup) {
				Protagonist.isFlying = true;
				Protagonist.MAXHP += 15;				
				onPickup=false;
			}
		}
		if(itemID==54) { //infamy
			if(Protagonist.isHit && Math.random()<0.2) {
				Protagonist.lastDamageTaken=0;
			}
		}
		if(itemID==55) { //contact lens
			for(int i=0; i<Crawling.spells.size(); i++) {
				for(int j=0; j<Crawling.bullets.size(); j++) {
					if(Crawling.spells.get(i).getHitbox().intersects(Crawling.bullets.get(j).getHitbox()) && Crawling.bullets.get(j).getProjectileID()==Projectile.BULLET) {
						Crawling.spells.get(i).setRemove();
						Crawling.bullets.get(j).setRemove();
					}
				}
			}
		}
		if(itemID==56) { //bowtie
			if(onPickup) {
				Protagonist.MAXHP += 20;
				Protagonist.HP += 20;				
				onPickup=false;
			}
		}
		if(itemID==57) { //magic 8 ball
			if(onPickup) {
				Protagonist.shotSpeed += 2f;
				Protagonist.shield += 15;				
				onPickup=false;
			}
		}
		if(itemID==58) { //magic mushroom
			if(onPickup) {
				Protagonist.MAXHP += 10;
				Protagonist.HP += 10;
				Protagonist.speed += 0.03f;
				Protagonist.range += 128f;
				onPickup=false;
			}
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(9, 1.5f);
			}
		}
		if(itemID==59) { //magic scab
			if(onPickup) {
				Protagonist.MAXHP += 10;
				Protagonist.HP += 30;
				Protagonist.shield += 15;				
				onPickup=false;
			}
		}
		if(itemID==60) { //big meat
			if(onPickup) {
				Protagonist.MAXHP += 5;
				Protagonist.damage += 7;				
				onPickup=false;
			}
		}
		if(itemID==61) { //the contract 
			if(onPickup) {
				Protagonist.shield+=30;
				onPickup=false;
			}
			if(Protagonist.MAXHP != 20) {
				Protagonist.MAXHP = 20;
			}
			if(Protagonist.tookDamageThisRoom && Protagonist.HP<20) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).setRemove();
				}
				Protagonist.tookDamageThisRoom=false;
			}
		}
		if(itemID==62) { //heels
			if(onPickup) {
				Protagonist.range += 128f;				
				onPickup=false;
			}
		}
		if(itemID==63) { //codeine piss
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.8f;
				Protagonist.range -= 256f;				
				onPickup=false;
			}
		}
		if(itemID==64) { //fat shroom
			if(onPickup) {
				Protagonist.MAXHP += 7;
				Protagonist.damage += 3;
				Protagonist.range += 64f;				
				onPickup=false;
			}
		}
		if(itemID==65) { //skinny shroom
			if(onPickup) {
				Protagonist.fireRateDelay -= (long)(Protagonist.fireRateDelay*0.3);
				Protagonist.damage -= 2;				
				onPickup=false;
			}
		}
		if(itemID==66) { //bandaid
			if(onPickup) {
				Protagonist.MAXHP += 20;
				Protagonist.shield += 5;				
				onPickup=false;
			}
		}
		if(itemID==67) { //ouija
			if(onPickup) {
				Protagonist.hasSpectral = true;				
				onPickup=false;
			}
		}
		if(itemID==68) { //pentagram
			if(onPickup) {
				Protagonist.damage += 7;
				onPickup=false;
			}
		}
		if(itemID==69) { //pisces
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.25f;
				Protagonist.shield += 15;
				onPickup=false;
			}
		}
		if(itemID==70) { //steroids
			if(onPickup) {
				Protagonist.speed += 0.08f;
				Protagonist.range += 160f;				
				onPickup=false;
			}
		}
		if(itemID==71) { //the sad onion
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.32f;				
				onPickup=false;
			}
		}
		if(itemID==72) { //the screw
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.25f;
				Protagonist.shotSpeed += 1.3f;				
				onPickup=false;
			}
		}
		if(itemID==73) { //the small rock
			if(onPickup) {
				Protagonist.damage += 5;
				Protagonist.fireRateDelay /= 1.15f;
				Protagonist.speed -= 0.06f;				
				onPickup=false;
			}
		}
		if(itemID==74) { //speed ball
			if(onPickup) {
				Protagonist.speed += 0.07f;
				Protagonist.shotSpeed += 1.5f;				
				onPickup=false;
			}
		}
		if(itemID==75) { //spirit of the night
			if(onPickup) {
				Protagonist.isFlying = true;
				Protagonist.hasPiercing = true;				
				onPickup=false;
			}
		}
		if(itemID==76) { //steven
			if(onPickup) {
				Protagonist.damage += 5;
				onPickup=false;
			}
		}
		if(itemID==77) { //stigmata
			if(onPickup) {
				Protagonist.MAXHP += 10;
				Protagonist.damage += 5;				
				onPickup=false;
			}

		}
		if(itemID==78) { //synthoil
			if(onPickup) {
				Protagonist.damage += 5;
				Protagonist.range += 96f;				
				onPickup=false;
			}
		}
		if(itemID==79) { //continuum
			if(onPickup) {
				Protagonist.range += 400f;
				Protagonist.hasSpectral = true;
				onPickup = false;
			}
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.spells.get(i).getX()<0) {
					Crawling.spells.get(i).setX(960);
				}
				if(Crawling.spells.get(i).getX()>960) {
					Crawling.spells.get(i).setX(0);
				}
				if(Crawling.spells.get(i).getY()<0) {
					Crawling.spells.get(i).setY(576);
				}
				if(Crawling.spells.get(i).getY()>576) {
					Crawling.spells.get(i).setY(0);
				}
			}
		}
		if(itemID==80) { //godhead
			if(onPickup) {
				Protagonist.fireRateDelay *= 1.7f;
				Protagonist.shotSpeed /= 1.25f;
				onPickup=false;
			}
			halos.clear();
			for(int i=0; i<Crawling.spells.size(); i++) {
				Projectile temp = Crawling.spells.get(i);
				halos.add(new Circle((float)temp.getHitbox().getCenterX(), (float)temp.getHitbox().getCenterY(), 2.5f*temp.getSpriteWidth()));
			}
			for(int i=0; i<halos.size(); i++) {
				for(int j=0; j<Crawling.enemies.size(); j++) {
					float deltaX = halos.get(i).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(i).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
					float deltaY = halos.get(i).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(i).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(i).getRadius()*halos.get(i).getRadius())){
						Crawling.enemies.get(j).takeDamage(Protagonist.damage/10);
					}
				}
				for(int k=0; k<Crawling.bosses.size(); k++) {
					float deltaX = halos.get(i).getCenterX() - Math.max(Crawling.bosses.get(k).getX(), Math.min(halos.get(i).getCenterX(), Crawling.bosses.get(k).getX() + Crawling.bosses.get(k).getSpriteWidth()));
					float deltaY = halos.get(i).getCenterY() - Math.max(Crawling.bosses.get(k).getY(), Math.min(halos.get(i).getCenterY(), Crawling.bosses.get(k).getY() + Crawling.bosses.get(k).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(i).getRadius()*halos.get(i).getRadius())){
						Crawling.bosses.get(k).takeDamage(Protagonist.damage/10);
					}
				}
			}
		}
		if(itemID==81) { //monstrance
			halos.clear();
			halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 144));
			for(int j=0; j<Crawling.enemies.size(); j++) {
				float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(0).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
				float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(0).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
				if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
					if(frameCounter % 3 == 0) {
						Crawling.enemies.get(j).takeDamage(1);
					}
				}
			}
			for(int k=0; k<Crawling.bosses.size(); k++) {
				float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.bosses.get(k).getX(), Math.min(halos.get(0).getCenterX(), Crawling.bosses.get(k).getX() + Crawling.bosses.get(k).getSpriteWidth()));
				float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.bosses.get(k).getY(), Math.min(halos.get(0).getCenterY(), Crawling.bosses.get(k).getY() + Crawling.bosses.get(k).getSpriteHeight()));
				if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
					if(frameCounter % 3 == 0) {
						Crawling.bosses.get(k).takeDamage(1);
					}
				}
			}
			frameCounter++;
			frameCounter %= 3;
		}
		if(itemID==82) { //censer
			halos.clear();
			halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 144));
			for(int j=0; j<Crawling.enemies.size(); j++) {
				float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(0).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
				float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(0).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
				if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
					Crawling.enemies.get(j).setSlow(0.4f);
				}
			}
		}
		if(itemID==83) { //angelic prism
			//orbits
			spriteWidth = 80;
			spriteHeight = 80;
			int radius = 128;
			frameCounter++;
			frameCounter%=540;
			x = (Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(frameCounter/1.5f)))*radius-spriteWidth/2;
			y = (Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(frameCounter/1.5f)))*radius-spriteHeight/2;
			//item interaction patch: parasite, explosivo, cricket's body
			Item[] interactions = {null, null, null}; //parasite, explosivo, cricket's body
			interactionProjectiles.clear();
			for(int i=0; i<Protagonist.obtainedItems.size(); i++) {
				if(Protagonist.obtainedItems.get(i).getID()==111) {
					interactions[0] = Protagonist.obtainedItems.get(i);
				}
				if(Protagonist.obtainedItems.get(i).getID()==9) {
					interactions[1] = Protagonist.obtainedItems.get(i);
				}
				if(Protagonist.obtainedItems.get(i).getID()==49) {
					interactions[2] = Protagonist.obtainedItems.get(i);
				}
			}
			for(int i=0; i<3; i++) {
				if(interactions[i]!=null) {
					interactionProjectiles.addAll(interactions[i].getSpecialProjectiles());
				}
			}
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			specialProjectiles.removeIf(p -> p==null);
			specialProjectiles.removeAll(Crawling.garbageSpells);
			//splits tears
			Rectangle familiarHitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(familiarHitbox.intersects(Crawling.spells.get(i).getHitbox()) && !specialProjectiles.contains(Crawling.spells.get(i)) && !interactionProjectiles.contains(Crawling.spells.get(i))) {
					float xSource = Crawling.spells.get(i).getX();
					float ySource = Crawling.spells.get(i).getY();
					double tempDir = Crawling.spells.get(i).getShotDirection();
					for(int k=0; k<4; k++) {
						Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, 2*Projectile.BASE_SPRITEHEIGHT, xSource+Projectile.BASE_SPRITEWIDTH/2, ySource+Projectile.BASE_SPRITEHEIGHT/2, tempDir+16*k-24, Protagonist.shotSpeed, (int) (Protagonist.damage*1.5f), Protagonist.range, Projectile.SPELL);
						Crawling.spells.add(ringProjectile);
						specialProjectiles.add(ringProjectile);
					}
					Crawling.spells.get(i).setRemove();
				}
			}
		}
		if(itemID==84) { //lead pencil
			if(Protagonist.isFiring) {
				frameCounter++;
				frameCounter %= 20;
			}
			if(frameCounter % 20 == 19) {
				timeSinceLastTrigger = System.currentTimeMillis();
				frameCounter = 0;
			}
			if(System.currentTimeMillis()-timeSinceLastTrigger<570) {
				Input input = container.getInput();
				int shotDirection = 0;
				if(input.isKeyDown(Input.KEY_UP)) {
					shotDirection = 270;
				}
				if(input.isKeyDown(Input.KEY_RIGHT)) {
					shotDirection = 0;
				}
				if(input.isKeyDown(Input.KEY_DOWN)) {
					shotDirection = 90;
				}
				if(input.isKeyDown(Input.KEY_LEFT)) {
					shotDirection = 180;
				}
				int numberOfShots = (int)(1+2*Math.random());
				for(int i=0; i<numberOfShots; i++){
					Crawling.spells.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Protagonist.SPRITEWIDTH/4, Protagonist.y+Protagonist.SPRITEHEIGHT/3, shotDirection-12+(int)(24*Math.random()), (float)(Protagonist.shotSpeed*(0.75+0.5*Math.random())), Protagonist.damage/10+(int)(2*Math.random()), Protagonist.range, Projectile.SPELL));
				}	
			}
		}
		if(itemID==85) { //contagion
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Crawling.enemies.get(i).getDead()) {
					for(int k=0; k<12; k++) {
						Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, (float)Crawling.enemies.get(i).getHitbox().getCenterX(), (float)Crawling.enemies.get(i).getHitbox().getCenterY(), 30*k, Protagonist.shotSpeed, Protagonist.damage, Protagonist.range/2, Projectile.SPELL);
						ringProjectile.getHitEnemies().add(Crawling.enemies.get(i));
						Crawling.spells.add(ringProjectile);
					}
				}
			}
		}
		if(itemID==86) { //kidney stone
			frameCounter++;
			frameCounter%=3;
			if(Protagonist.isFiring && Math.random()<0.03) {
				timeSinceLastTrigger = System.currentTimeMillis();
			}
			if(System.currentTimeMillis()-timeSinceLastTrigger<1500) {
				if(frameCounter % 3 == 0) {
					Input input = container.getInput();
					int shotDirection = 0;
					if(input.isKeyDown(Input.KEY_UP)) {
						shotDirection = 270;
					}
					if(input.isKeyDown(Input.KEY_RIGHT)) {
						shotDirection = 0;
					}
					if(input.isKeyDown(Input.KEY_DOWN)) {
						shotDirection = 90;
					}
					if(input.isKeyDown(Input.KEY_LEFT)) {
						shotDirection = 180;
					}
					Crawling.spells.add(new Projectile((int)(2*Projectile.BASE_SPRITEWIDTH), (int)(2*Projectile.BASE_SPRITEHEIGHT), Protagonist.x+Protagonist.SPRITEWIDTH/4, Protagonist.y+Protagonist.SPRITEHEIGHT/3, shotDirection-2+4*Math.random(), Protagonist.shotSpeed, (int)(Protagonist.damage*1.5f), Protagonist.range, Projectile.SPELL));
				}
			}
		}
		if(itemID==87) { //little horn
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			specialProjectiles.removeIf(p -> p==null);
			specialProjectiles.removeAll(Crawling.garbageSpells);
			if(Protagonist.isFiring && Crawling.spells.size()>0 && Math.random()<0.042) {
				Crawling.spells.get(Crawling.spells.size()-1).makePiercing();
				Crawling.spells.get(Crawling.spells.size()-1).setDamage(Protagonist.damage*5);
				specialProjectiles.add(Crawling.spells.get(Crawling.spells.size()-1));
			}
			halos.clear();
			for(int i=0; i<specialProjectiles.size(); i++) {
				Projectile temp = specialProjectiles.get(i);
				halos.add(new Circle((float)temp.getHitbox().getCenterX(), (float)temp.getHitbox().getCenterY(), 1.2f*temp.getSpriteWidth()));
			}
			for(int i=0; i<halos.size(); i++) {
				for(int j=0; j<Crawling.enemies.size(); j++) {
					float deltaX = halos.get(i).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(i).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
					float deltaY = halos.get(i).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(i).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(i).getRadius()*halos.get(i).getRadius())){
						Crawling.enemies.get(j).setRemove();
					}
				}
			}
		}
		if(itemID==88) { //lil x
			//movement of familiar
			if(onPickup) {
				x = 448;
				y = 256;
				angle = 45;
				onPickup=false;
			}
			spriteWidth = 64;
			spriteHeight = 64;
			float speed_1 = 3f;
			if (y + speed_1*Math.sin(Math.toRadians(angle)) <= Crawling.currentRoom.getTiledMap().getTileHeight() 
					|| y + speed_1*Math.sin(Math.toRadians(angle)) + spriteHeight >= Crawling.currentRoom.getPixelHeight() - Crawling.currentRoom.getTiledMap().getTileHeight())
			{
				 angle *= -1;
			}
			if (x + speed_1*Math.cos(Math.toRadians(angle)) <= Crawling.currentRoom.getTiledMap().getTileWidth() 
					|| x + speed_1*Math.cos(Math.toRadians(angle)) + spriteWidth >= Crawling.currentRoom.getPixelWidth() - Crawling.currentRoom.getTiledMap().getTileWidth())
			{
				angle = 180 - angle;
			}
			x += speed_1*Math.cos(Math.toRadians(angle));
			y += speed_1*Math.sin(Math.toRadians(angle));
			//effect of shadow surrounding familiar
			halos.clear();
			halos.add(new Circle(x+32, y+32, 144));
			for(int j=0; j<Crawling.enemies.size(); j++) {
				float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(0).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
				float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(0).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
				if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
					if(frameCounter==0) {
						Crawling.enemies.get(j).takeDamage(1);
					}
				}
			}
			for(int k=0; k<Crawling.bosses.size(); k++) {
				float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.bosses.get(k).getX(), Math.min(halos.get(0).getCenterX(), Crawling.bosses.get(k).getX() + Crawling.bosses.get(k).getSpriteWidth()));
				float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.bosses.get(k).getY(), Math.min(halos.get(0).getCenterY(), Crawling.bosses.get(k).getY() + Crawling.bosses.get(k).getSpriteHeight()));
				if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
					if(frameCounter==0) {
						Crawling.bosses.get(k).takeDamage(1);
					}
				}
			}
			float deltaX = halos.get(0).getCenterX() - Math.max(Protagonist.x, Math.min(halos.get(0).getCenterX(), Protagonist.x + Protagonist.SPRITEWIDTH));
			float deltaY = halos.get(0).getCenterY() - Math.max(Protagonist.y, Math.min(halos.get(0).getCenterY(), Protagonist.y + Protagonist.SPRITEHEIGHT));
			if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
				for(int k=0; k<Crawling.spells.size(); k++) {
					Crawling.spells.get(k).setDamageMultiplier(11, 1.6f);
				}
			}
			frameCounter++;
			frameCounter %= 5;
		}
		if(itemID==89) { //athame
			if(onPickup) {
				Protagonist.MAXHP -= 15;
				onPickup=false;
			}
			if(Protagonist.isHit) {
				timeSinceLastTrigger = System.currentTimeMillis();
			}
			halos.clear();
			if(System.currentTimeMillis()-timeSinceLastTrigger<1500) {
				frameCounter+=6;
				frameCounter%=360;
				halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, (float)(112+16f*Math.cos(Math.toRadians(frameCounter)))));
			}
			if(halos.size()>0) {
				for(int j=0; j<Crawling.enemies.size(); j++) {
					float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(0).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
					float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(0).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
						Crawling.enemies.get(j).takeDamage(2);
						if(Crawling.enemies.get(j).getDead() && Math.random()<0.17) {
							Crawling.tempPickups.add(new Pickup("shield", Crawling.enemies.get(j).getX(), Crawling.enemies.get(j).getY()));
						}
					}
				}
				for(int k=0; k<Crawling.bosses.size(); k++) {
					float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.bosses.get(k).getX(), Math.min(halos.get(0).getCenterX(), Crawling.bosses.get(k).getX() + Crawling.bosses.get(k).getSpriteWidth()));
					float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.bosses.get(k).getY(), Math.min(halos.get(0).getCenterY(), Crawling.bosses.get(k).getY() + Crawling.bosses.get(k).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
						Crawling.bosses.get(k).takeDamage(2);
					}
				}
			}
		}
		if(itemID==90) { // rights
			Protagonist.IFRAMES=3200;
		}
		if(itemID==91) { //immaculate heart
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.2;
				Protagonist.shield += 30;
				onPickup=false;
			}
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			if(Protagonist.isFiring && Math.random()<0.14) {
				Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Projectile.BASE_SPRITEWIDTH/2, Protagonist.y+Projectile.BASE_SPRITEHEIGHT/2, 0, 0, Protagonist.damage, Protagonist.range, Projectile.SPELL);
				ringProjectile.makeSpectral();
				ringProjectile.makePiercing();
				Crawling.spells.add(ringProjectile);
				specialProjectiles.add(ringProjectile);
			}
			for(int i=0; i<specialProjectiles.size(); i++) {
				Projectile temp = specialProjectiles.get(i);
				if(temp.getPolarRadius()<128) {
					temp.setPolarRadius(temp.getPolarRadius()+1);
				}
				for(int k=0; k<2; k++) {
					temp.iterateFrameCounter();
				}
				temp.setX((Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(temp.getFrameCounter())))*temp.getPolarRadius());
				temp.setY((Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(temp.getFrameCounter())))*temp.getPolarRadius());
			}
		}
		if(itemID==92) { //tiny planet
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.28;
				Protagonist.range = Protagonist.shotSpeed*600f;
				Protagonist.hasSpectral = true;
				onPickup = false;
			}
			if(Protagonist.isFiring) {
				frameCounter++;
				frameCounter %= 2;
			}
			//item interaction patch: lil lil pump, lil purpp, immaculate heart, saturn
			Item[] interactions = {null, null, null, null}; //lil lil pump, lil purpp, immaculate heart, saturn
			interactionProjectiles.clear();
			for(int i=0; i<Protagonist.obtainedItems.size(); i++) {
				if(Protagonist.obtainedItems.get(i).getID()==123) {
					interactions[0] = Protagonist.obtainedItems.get(i);
				}
				if(Protagonist.obtainedItems.get(i).getID()==124) {
					interactions[1] = Protagonist.obtainedItems.get(i);
				}
				if(Protagonist.obtainedItems.get(i).getID()==91) {
					interactions[2] = Protagonist.obtainedItems.get(i);
				}
				if(Protagonist.obtainedItems.get(i).getID()==93) {
					interactions[3] = Protagonist.obtainedItems.get(i);
				}
			}
			for(int i=0; i<4; i++) {
				if(interactions[i]!=null) {
					interactionProjectiles.addAll(interactions[i].getSpecialProjectiles());
				}
			}
			//polar motion of spells
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(!interactionProjectiles.contains(Crawling.spells.get(i))) {
					Projectile temp = Crawling.spells.get(i);
					if(!temp.isRotationSet()) {
						temp.setRotationOrientation(frameCounter);
					}
					if(temp.getPolarRadius()<256) {
						temp.setPolarRadius(temp.getPolarRadius()+1.28f);
					}
					temp.iterateFrameCounter();
					temp.iterateFrameCounter();
					temp.setX((Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(temp.getFrameCounter())))*temp.getPolarRadius());
					temp.setY((Protagonist.y+Protagonist.SPRITEHEIGHT/2)+(1-2*(temp.getRotationOrientation()))*((float)Math.sin(Math.toRadians(temp.getFrameCounter())))*temp.getPolarRadius());					
				}
			}
		}
		if(itemID==93) { //saturn
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			if(Crawling.roomSwitching && !Crawling.currentRoom.isCleared()) {
				for(int i=0; i<7; i++) {
					Projectile ringProjectile = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, Protagonist.x+Projectile.BASE_SPRITEWIDTH/2, Protagonist.y+Projectile.BASE_SPRITEHEIGHT/2, 0, 0, Protagonist.damage, Protagonist.range, Projectile.SPELL);
					ringProjectile.makePiercing();
					ringProjectile.makeSpectral();
					ringProjectile.setPolarRadius(160);
					ringProjectile.setFrameCounter(i*360/7);
					Crawling.spells.add(ringProjectile);
					specialProjectiles.add(ringProjectile);
				}
			}
			for(int i=0; i<specialProjectiles.size(); i++) {
				Projectile temp = specialProjectiles.get(i);
				for(int k=0; k<2; k++) {
					temp.iterateFrameCounter();
				}
				temp.setX((Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(temp.getFrameCounter())))*temp.getPolarRadius());
				temp.setY((Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(temp.getFrameCounter())))*temp.getPolarRadius());
			}
		}
		if(itemID==94) { //orb of light
			Protagonist.setFireRateMultiplier(6, 1);
			if(Protagonist.HP == Protagonist.MAXHP && !Protagonist.tookDamageThisRoom) {
				Protagonist.setFireRateMultiplier(6, 0.6f);
			}
		}
		if(itemID==95) { //norepi
			Protagonist.setFireRateMultiplier(7, 1-0.4f*Math.max(0, 1-(Protagonist.HP)/((float)(Protagonist.MAXHP))));
		}
		if(itemID==96) { //bloodlust 3
			if(Crawling.floorCleared) {
				statModifier=0;
			}
			if(Protagonist.isHit && statModifier < 7) {
				statModifier++;
			}
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setDamageMultiplier(12, 1+0.07f*statModifier);
			}
		}
		if(itemID==97) { //celtic cross
			if(Protagonist.isHit && Math.random()<0.15) {
				timeSinceLastTrigger = System.currentTimeMillis();
			}
			halos.clear();
			Protagonist.invulnerable = false;
			if(System.currentTimeMillis()-timeSinceLastTrigger<8000) {
				halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 48));
				Protagonist.invulnerable = true;
			}
		}
		if(itemID==98) { //polaroid
			if(Protagonist.isHit && Protagonist.HP<=20) {
				timeSinceLastTrigger = System.currentTimeMillis();
			}
			halos.clear();
			Protagonist.invulnerable = false;
			if(System.currentTimeMillis()-timeSinceLastTrigger<5000) {
				halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 48));
				Protagonist.invulnerable = true;
			}
		}
		if(itemID==99) { //the pact
			if(onPickup) {
				Protagonist.fireRateDelay /= 2.25f;
				Protagonist.MAXHP /= 2;
				Protagonist.shield+=30;
				onPickup=false;
			}
		}
		if(itemID==100) { //the wafer
			if(Protagonist.isHit) {
				Protagonist.lastDamageTaken = (int)(2f*Protagonist.lastDamageTaken/3f + 1);
			}
		}
		if(itemID==101) { //the mark
			if(onPickup) {
				Protagonist.MAXHP /= 2;
				Protagonist.damage = (int)(Protagonist.damage*1.5f+5);
				Protagonist.fireRateDelay /= 1.28;
				Protagonist.shield += 15;
				onPickup=false;
			}
		}
		if(itemID==102) { //the relic
			if(Crawling.clearRoomToggle) {
				statModifier++;
				statModifier%=6;
				if(statModifier==0) {
					int centerX = Crawling.currentRoom.getTiledMap().getWidth()/2;
					int centerY = Crawling.currentRoom.getTiledMap().getHeight()/2;
					boolean heartNotPlaced = true;
					int heartX = 0;
					int heartY = 0;
					int distFromCenter = 0;
					while (heartNotPlaced) {
						for (int i=0; i<Crawling.currentRoom.getTiledMap().getWidth(); i++) {
							for (int j=0; j<Crawling.currentRoom.getTiledMap().getHeight(); j++) {
								if (Math.abs(i-centerX)+Math.abs(j-centerY)==distFromCenter && !Crawling.currentRoom.getTiledMap().getTileProperty(Crawling.currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
									heartNotPlaced = false;
									heartX = i;
									heartY = j;
								}
							}
						}
						distFromCenter++;
					}
					Crawling.currentRoom.addPickup(new Pickup("shield", heartX*Crawling.currentRoom.getTiledMap().getTileWidth()+16, heartY*Crawling.currentRoom.getTiledMap().getTileHeight()+16));
				}
			}
		}
		if(itemID==103) { //crystal ball
			UI.setTreasureMap();
			if(Crawling.clearRoomToggle && Math.random()<0.09) {
				Protagonist.shield += 15;
			}
		}
		if(itemID==104) { //mitre
			Protagonist.hasMitre = 1;
		}
		if(itemID==105) { //ceremonial robes
			if(onPickup) {
				Protagonist.MAXHP -= 10;
				Protagonist.shield += 45;
				Protagonist.damage += 8;
				onPickup=false;
			}
		}
		if(itemID==106) { //tanooki
			if(Protagonist.isMoving || Protagonist.isAttacking) {
				timeSinceLastTrigger = System.currentTimeMillis();
			}
			halos.clear();
			Protagonist.invulnerable=false;
			if(System.currentTimeMillis()-timeSinceLastTrigger>2200) {
				halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 48));
				Protagonist.invulnerable=true;
			}
		}
		if(itemID==107) { //black lotus
			if(onPickup) {
				Protagonist.MAXHP += 10;
				Protagonist.HP += 10;
				Protagonist.shield += 20;
				onPickup=false;
			}
		}
		if(itemID==108) { //gimpy
			if(Protagonist.isHit && Math.random()<0.16) {
				Protagonist.shield += 20;
			}
		}
		if(itemID==109) { //rosary
			if(onPickup) {
				Protagonist.shield += 50;
				onPickup=false;
			}
		}
		if(itemID==110) { //blanket
			if(onPickup) {
				Protagonist.shield += 40;
				Protagonist.HP = Protagonist.MAXHP;
				onPickup=false;
			}
			if(Crawling.xCurrentLocation==Crawling.floor.getBossLocation()[0] && Crawling.yCurrentLocation==Crawling.floor.getBossLocation()[1] && Protagonist.isHit && !Protagonist.tookDamageThisRoom) {
				Protagonist.lastDamageTaken = 0;
			}
		}
		if(itemID==111) { //parasite
			for(int i=0; i<Crawling.spells.size(); i++) {
				if((Crawling.spells.get(i).getExpired() || Crawling.spells.get(i).getHitting()) && !specialProjectiles.contains(Crawling.spells.get(i))) {
					float xSource = Crawling.spells.get(i).getX()-3*Crawling.spells.get(i).getXIncrement();
					float ySource = Crawling.spells.get(i).getY()-3*Crawling.spells.get(i).getYIncrement();
					double shotDirection = Crawling.spells.get(i).getShotDirection();
					Projectile ringProjectile1 = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, xSource+Projectile.BASE_SPRITEWIDTH/2, ySource+Projectile.BASE_SPRITEHEIGHT/2, shotDirection+90, Protagonist.shotSpeed, Protagonist.damage/2, Protagonist.range/3, Projectile.SPELL);
					Crawling.spells.add(ringProjectile1);
					specialProjectiles.add(ringProjectile1);
					Projectile ringProjectile2 = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, xSource+Projectile.BASE_SPRITEWIDTH/2, ySource+Projectile.BASE_SPRITEHEIGHT/2, shotDirection-90, Protagonist.shotSpeed, Protagonist.damage/2, Protagonist.range/3, Projectile.SPELL);
					Crawling.spells.add(ringProjectile2);
					specialProjectiles.add(ringProjectile2);
				}
			}
		}
		if(itemID==112) { //empty vessel
			frameCounter++;
			frameCounter %= 2400;
			if(onPickup) {
				Protagonist.MAXHP -= 10;
				Protagonist.shield += 30;
				onPickup=false;
			}
			if(Crawling.roomSwitching) {
				if(Protagonist.HP <= Protagonist.MAXHP/3) {
					Protagonist.isFlying = true;
					effectToggle = true;
				}
				else {
					Protagonist.isFlying = false;
					effectToggle = false;
				}
			}
			halos.clear();
			Protagonist.invulnerable = false;
			if(effectToggle && !Crawling.currentRoom.isCleared()) {
				if(frameCounter<600) {
					halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 48));
					Protagonist.invulnerable = true;
				}
			}
		}
		if(itemID==113) { //stopwatch
			if(Protagonist.isHit) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).setSlow(1f);
				}
			}
		}
		if(itemID==114) { //common cold
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			specialProjectiles.removeIf(p -> p==null);
			specialProjectiles.removeAll(Crawling.garbageSpells);
			if(Protagonist.isFiring && Crawling.spells.size()>0 && Math.random()<0.25) {
				specialProjectiles.add(Crawling.spells.get(Crawling.spells.size()-1));
			}
			halos.clear();
			for(int i=0; i<specialProjectiles.size(); i++) {
				Projectile temp = specialProjectiles.get(i);
				halos.add(new Circle((float)temp.getHitbox().getCenterX(), (float)temp.getHitbox().getCenterY(), 0.5f*temp.getSpriteWidth()));
			}
			for(int i=0; i<specialProjectiles.size(); i++) {
				if(specialProjectiles.get(i).getHitting() && specialProjectiles.get(i).getTargetEnemy() != null) {
					specialProjectiles.get(i).getTargetEnemy().addPoison(3, Protagonist.damage/2, 0);
				}
			}
		}
		if(itemID==115) { //serpent's kiss
			if(onPickup) {
				Protagonist.MAXHP -= 10;
				onPickup=false;
			}
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			specialProjectiles.removeIf(p -> p==null);
			specialProjectiles.removeAll(Crawling.garbageSpells);
			if(Protagonist.isFiring && Crawling.spells.size()>0 && Math.random()<0.15) {
				specialProjectiles.add(Crawling.spells.get(Crawling.spells.size()-1));
			}
			halos.clear();
			for(int i=0; i<specialProjectiles.size(); i++) {
				Projectile temp = specialProjectiles.get(i);
				halos.add(new Circle((float)temp.getHitbox().getCenterX(), (float)temp.getHitbox().getCenterY(), 0.5f*temp.getSpriteWidth()));
			}
			for(int i=0; i<specialProjectiles.size(); i++) {
				if(specialProjectiles.get(i).getHitting() && specialProjectiles.get(i).getTargetEnemy() != null) {
					specialProjectiles.get(i).getTargetEnemy().addPoison(3, Protagonist.damage/2, 1);
					specialProjectiles.get(i).getTargetEnemy().setSerpentPoisoned();
				}
			}
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Crawling.enemies.get(i).getDead() && Crawling.enemies.get(i).getSerpentPoisoned()) {
					if(Math.random()<0.16) {
						Crawling.tempPickups.add(new Pickup("shield", Crawling.enemies.get(i).getX(), Crawling.enemies.get(i).getY()));
					}
				}
			}
			frameCounter++;
			frameCounter %= 2;
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Protagonist.hitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
					Crawling.enemies.get(i).setSerpentPoisoned();
					if(frameCounter==0) {
						if(Crawling.enemies.get(i).getHP()==1) {
							Crawling.enemies.get(i).setRemove();
							if(Math.random()<0.2) {
								Protagonist.shield+=15;
							}
						}
						else {
							Crawling.enemies.get(i).takeDamage(1);
						}
					}
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(Protagonist.hitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
					if(frameCounter==0) {
						Crawling.bosses.get(i).takeDamage(1);
					}
				}
			}
		}
		if(itemID==116) { //toxic shock
			if(Crawling.roomSwitching) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).addPoison(6, Protagonist.damage/4, 0);
				}
			}
		}
		if(itemID==117) { //mirror
			if(onPickup) {
				Protagonist.shotSpeed *= 2;
				Protagonist.range = 1000000f;
				onPickup=false;
			}
			for(int i=0; i<Crawling.spells.size(); i++) {
				Crawling.spells.get(i).setSpeed(Crawling.spells.get(i).getSpeed()-Protagonist.shotSpeed/60);
				if(Crawling.spells.get(i).getSpeed()<=(-1.6f)*Protagonist.shotSpeed) {
					Crawling.spells.get(i).setRemove();
				}
			}
		}
		if(itemID==118) { //midas touch
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(Crawling.enemies.get(i).getHitbox().intersects(Protagonist.hitbox)) {
					Crawling.enemies.get(i).takeDamage(1);
					Crawling.enemies.get(i).setSlow(1);
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(Crawling.bosses.get(i).getHitbox().intersects(Protagonist.hitbox)) {
					Crawling.bosses.get(i).takeDamage(1);
				}
			}
		}
		if(itemID==119) { //broken watch
			if(Protagonist.isHit) {
				for(int i=0; i<Crawling.enemies.size(); i++) {
					Crawling.enemies.get(i).setSlow(0.6f);
				}
			}
		}
		if(itemID==120) { //eye of belial
			if(onPickup) {
				Protagonist.MAXHP -= 10;
				onPickup=false;
			}
			Protagonist.hasPiercing = true;
			for(int i=0; i<Crawling.spells.size(); i++) {
				if(Crawling.spells.get(i).getHitEnemies().size()+Crawling.spells.get(i).getHitBosses().size()>0) {
					Crawling.spells.get(i).setDamageMultiplier(13, 2);
					Crawling.spells.get(i).setSpeed(Crawling.spells.get(i).getSpeed()-Protagonist.shotSpeed/60);
					if(Crawling.spells.get(i).getSpeed()<=(-1.6)*Protagonist.shotSpeed) {
						Crawling.spells.get(i).setRemove();
					}
				}
			}
		}
		if(itemID==121) { //maw of the void
			if(onPickup) {
				Protagonist.MAXHP -= 20;
				onPickup=false;
			}
			//charge aura
			if(Protagonist.isAttacking) {
				statModifier++;
			} else {
				if(statModifier>216) {
					timeSinceLastTrigger=System.currentTimeMillis();
				}
				statModifier=0;
			}
			if(statModifier>216) {
				Protagonist.weaponCharged = true;
			} else {
				Protagonist.weaponCharged = false;
			}
			//release aura
			halos.clear();
			if(System.currentTimeMillis()-timeSinceLastTrigger<1500) {
				frameCounter+=6;
				frameCounter%=360;
				halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, (float)(112+16f*Math.cos(Math.toRadians(frameCounter)))));
			}
			if(halos.size()>0) {
				for(int j=0; j<Crawling.enemies.size(); j++) {
					float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(0).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
					float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(0).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
						Crawling.enemies.get(j).takeDamage(Math.max(1, Protagonist.damage/10));
						if(Crawling.enemies.get(j).getDead() && Math.random()<0.08) {
							Crawling.tempPickups.add(new Pickup("shield", Crawling.enemies.get(j).getX(), Crawling.enemies.get(j).getY()));
						}
					}
				}
				for(int k=0; k<Crawling.bosses.size(); k++) {
					float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.bosses.get(k).getX(), Math.min(halos.get(0).getCenterX(), Crawling.bosses.get(k).getX() + Crawling.bosses.get(k).getSpriteWidth()));
					float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.bosses.get(k).getY(), Math.min(halos.get(0).getCenterY(), Crawling.bosses.get(k).getY() + Crawling.bosses.get(k).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
						if(Math.random()<0.5) {
							Crawling.bosses.get(k).takeDamage(1+Protagonist.damage/10);
						}
					}
				}
			}
		}
		if(itemID==122) { //lil zel
			//orbits
			spriteWidth = 40;
			spriteHeight = 64;
			int radius = 56;
			frameCounter++;
			frameCounter %= 240;
			x = (Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(1.5f*frameCounter)))*radius-spriteWidth/2;
			y = (Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(1.5f*frameCounter)))*radius-spriteHeight/2;
			//deals contact damage and blocks shots
			Rectangle familiarHitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(familiarHitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
					Crawling.enemies.get(i).takeDamage(2);
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
					Crawling.bosses.get(i).takeDamage(2);
				}
			}
			for(int i=0; i<Crawling.bullets.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bullets.get(i).getHitbox()) && Crawling.bullets.get(i).getProjectileID()==Projectile.BULLET) {
					Crawling.bullets.get(i).setRemove();
				}
			}
		}
		if(itemID==123) { //lil lil pump
			if(onPickup) {
				Protagonist.MAXHP -= 12;
				onPickup = false;
			}
			//orbits
			spriteWidth = 48;
			spriteHeight = 48;
			int radius = 48;
			frameCounter++;
			frameCounter %= 360;
			x = (Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(frameCounter)))*radius-spriteWidth/2;
			y = (Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(frameCounter)))*radius-spriteHeight/2;
			//deals contact damage
			Rectangle familiarHitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			for(int i=0; i<Crawling.enemies.size(); i++) {
				if(familiarHitbox.intersects(Crawling.enemies.get(i).getHitbox())) {
					Crawling.enemies.get(i).takeDamage(1);
				}
			}
			for(int i=0; i<Crawling.bosses.size(); i++) {
				if(familiarHitbox.intersects(Crawling.bosses.get(i).getHitbox())) {
					Crawling.bosses.get(i).takeDamage(1);
				}
			}
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			specialProjectiles.removeIf(p -> p==null);
			specialProjectiles.removeAll(Crawling.garbageSpells);
			//shoots copies of protagonist's spells
			if(Protagonist.isFiring) {
				Protagonist.shoot(x+spriteWidth/2, y+spriteHeight/3, Protagonist.shotDirection, container, delta);
			}
		}
		if(itemID==124) { //lil purpp
			//orbits
			spriteWidth = 36;
			spriteHeight = 48;
			int radius = 36;
			frameCounter++;
			frameCounter %= 360;
			x = (Protagonist.x+Protagonist.SPRITEWIDTH/2)+((float)Math.cos(Math.toRadians(frameCounter)))*radius-spriteWidth/2;
			y = (Protagonist.y+Protagonist.SPRITEHEIGHT/2)+((float)Math.sin(Math.toRadians(frameCounter)))*radius-spriteHeight/2;
			//targets enemies at close range
			specialProjectiles.removeIf(p -> p.toBeRemoved());
			specialProjectiles.removeIf(p -> p==null);
			specialProjectiles.removeAll(Crawling.garbageSpells);
			if(Crawling.bosses.size()>0) {
				Boss targetEnemy = Crawling.bosses.get(0);
				double dis_x = x+spriteWidth/2-targetEnemy.getHitbox().getCenterX();
				double dis_y = y+spriteHeight/2-targetEnemy.getHitbox().getCenterY();
				double targetDistance = Math.sqrt(Math.pow(x+spriteWidth/2-targetEnemy.getHitbox().getCenterX(), 2) + Math.pow(y+spriteHeight/2-targetEnemy.getHitbox().getCenterY(), 2));
				for(int i=0; i<Crawling.bosses.size(); i++) {
					double newDistance = Math.sqrt(Math.pow(x+spriteWidth/2-Crawling.bosses.get(i).getHitbox().getCenterX(), 2) + Math.pow(y+spriteHeight/2-Crawling.bosses.get(i).getHitbox().getCenterY(), 2));
					if(newDistance<targetDistance) {
						targetDistance = newDistance;
						dis_x = x+spriteWidth/2-Crawling.bosses.get(i).getHitbox().getCenterX();
						dis_y = y+spriteHeight/2-Crawling.bosses.get(i).getHitbox().getCenterY();
						targetEnemy = Crawling.bosses.get(i);
					}
				}
				double angleBetween;
				if (dis_x > 0)
				{
					angleBetween = Math.toDegrees(Math.atan(dis_y / dis_x));
				}
				else
				{
					angleBetween = 180 + Math.toDegrees(Math.atan(dis_y / dis_x));
				}
				if (targetDistance <= 144 && (System.currentTimeMillis()-timeSinceLastBullet > 300 
						|| timeSinceLastBullet == 0))
				{
					Projectile temp = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
							x+spriteWidth/2, y+spriteHeight/2, angleBetween+180, 5f, 5+Protagonist.damage/2, 160, Projectile.SPELL);
					Crawling.spells.add(temp);
					specialProjectiles.add(temp);
					timeSinceLastBullet = System.currentTimeMillis();
				}
			}
			if(Crawling.enemies.size()>0) {
				Enemy targetEnemy = Crawling.enemies.get(0);
				double dis_x = x+spriteWidth/2-targetEnemy.getHitbox().getCenterX();
				double dis_y = y+spriteHeight/2-targetEnemy.getHitbox().getCenterY();
				double targetDistance = Math.sqrt(Math.pow(x+spriteWidth/2-targetEnemy.getHitbox().getCenterX(), 2) + Math.pow(y+spriteHeight/2-targetEnemy.getHitbox().getCenterY(), 2));
				for(int i=0; i<Crawling.enemies.size(); i++) {
					double newDistance = Math.sqrt(Math.pow(x+spriteWidth/2-Crawling.enemies.get(i).getHitbox().getCenterX(), 2) + Math.pow(y+spriteHeight/2-Crawling.enemies.get(i).getHitbox().getCenterY(), 2));
					if(newDistance<targetDistance) {
						targetDistance = newDistance;
						dis_x = x+spriteWidth/2-Crawling.enemies.get(i).getHitbox().getCenterX();
						dis_y = y+spriteHeight/2-Crawling.enemies.get(i).getHitbox().getCenterY();
						targetEnemy = Crawling.enemies.get(i);
					}
				}
				double angleBetween;
				if (dis_x > 0)
				{
					angleBetween = Math.toDegrees(Math.atan(dis_y / dis_x));
				}
				else
				{
					angleBetween = 180 + Math.toDegrees(Math.atan(dis_y / dis_x));
				}
				if (targetDistance <= 144 && (System.currentTimeMillis()-timeSinceLastBullet > 300 
						|| timeSinceLastBullet == 0))
				{
					Projectile temp = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
							x+spriteWidth/2, y+spriteHeight/2, angleBetween+180, 5f, 5+Protagonist.damage/2, 160, Projectile.SPELL);
					Crawling.spells.add(temp);
					specialProjectiles.add(temp);
					timeSinceLastBullet = System.currentTimeMillis();
				}
			}
		}
		if(itemID==125) { //apple
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.2;
				onPickup=false;
			}
			if(Protagonist.isFiring && Math.random()<0.086) {
				Crawling.spells.get(Crawling.spells.size()-1).setDamageMultiplier(14, 5f);
				Crawling.spells.get(Crawling.spells.size()-1).makePiercing();
			}
		}
		if(itemID==126) { //poison brain
			halos.clear();
			if(Protagonist.isAttacking) {
				halos.add(new Circle(Protagonist.x+Protagonist.SPRITEWIDTH/2, Protagonist.y+Protagonist.SPRITEHEIGHT/2, 56));
			}
			if(halos.size()>0) {
				for(int j=0; j<Crawling.enemies.size(); j++) {
					float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.enemies.get(j).getX(), Math.min(halos.get(0).getCenterX(), Crawling.enemies.get(j).getX() + Crawling.enemies.get(j).getSpriteWidth()));
					float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.enemies.get(j).getY(), Math.min(halos.get(0).getCenterY(), Crawling.enemies.get(j).getY() + Crawling.enemies.get(j).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
						Crawling.enemies.get(j).takeDamage(Math.max(1, Protagonist.damage/10));
					}
				}
				for(int k=0; k<Crawling.bosses.size(); k++) {
					float deltaX = halos.get(0).getCenterX() - Math.max(Crawling.bosses.get(k).getX(), Math.min(halos.get(0).getCenterX(), Crawling.bosses.get(k).getX() + Crawling.bosses.get(k).getSpriteWidth()));
					float deltaY = halos.get(0).getCenterY() - Math.max(Crawling.bosses.get(k).getY(), Math.min(halos.get(0).getCenterY(), Crawling.bosses.get(k).getY() + Crawling.bosses.get(k).getSpriteHeight()));
					if((deltaX*deltaX + deltaY*deltaY) < (halos.get(0).getRadius()*halos.get(0).getRadius())){
						Crawling.bosses.get(k).takeDamage(Math.max(1, Protagonist.damage/10));
					}
				}
			}
		}
		if(itemID==127) { //squeezy
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.3f;
				Protagonist.shield += 30;
				onPickup = false;
			}
		}
		if(itemID==128) { //dolly
			if(onPickup) {
				Protagonist.fireRateDelay /= 1.2f;
				Protagonist.range += 96f;
				for(int i=0; i<3; i++) {
					if(Math.random()<0.5) {
						Protagonist.HP += 15;
					} else {
						Protagonist.shield += 15;
					}
				}
				onPickup=false;
			}
		}
		if(itemID==129) { //caffeine pill
			if(onPickup) {
				Protagonist.speed += 0.05f;
				Protagonist.SPRITEWIDTH *= 0.75f;
				Protagonist.SPRITEHEIGHT *= 0.75f;
				onPickup=false;
			}
		}
		if(itemID==130) { //binky
			if(onPickup) {
				Protagonist.shield += 15;
				Protagonist.range -= 128f;
				Protagonist.fireRateDelay /= 1.3f;
				Protagonist.SPRITEWIDTH *= 0.75f;
				Protagonist.SPRITEHEIGHT *= 0.75f;
				onPickup=false;
			}
		}
		if(itemID==131) { //yum heart
			if(Crawling.clearRoomToggle) {
				statModifier++;
				statModifier%=6;
				if(statModifier==0) {
					int centerX = Crawling.currentRoom.getTiledMap().getWidth()/2;
					int centerY = Crawling.currentRoom.getTiledMap().getHeight()/2;
					boolean heartNotPlaced = true;
					int heartX = 0;
					int heartY = 0;
					int distFromCenter = 0;
					while (heartNotPlaced) {
						for (int i=0; i<Crawling.currentRoom.getTiledMap().getWidth(); i++) {
							for (int j=0; j<Crawling.currentRoom.getTiledMap().getHeight(); j++) {
								if (Math.abs(i-centerX)+Math.abs(j-centerY)==distFromCenter && !Crawling.currentRoom.getTiledMap().getTileProperty(Crawling.currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
									heartNotPlaced = false;
									heartX = i;
									heartY = j;
								}
							}
						}
						distFromCenter++;
					}
					Crawling.currentRoom.addPickup(new Pickup("heart", heartX*Crawling.currentRoom.getTiledMap().getTileWidth()+16, heartY*Crawling.currentRoom.getTiledMap().getTileHeight()+16));
				}
			}
		}
		if(itemID==132) { //scapular
			if(Protagonist.HP<20 && !Protagonist.tookDamageThisRoom && Protagonist.isHit) {
				Protagonist.shield += 10;
			}
		}
		if(itemID==133) { //raw liver
			if(onPickup) {
				Protagonist.MAXHP += 25;
				Protagonist.HP += 25;
				onPickup=false;
			}
		}
		if(itemID==134) { //stem cells
			if(onPickup) {
				Protagonist.MAXHP += 15;
				Protagonist.HP += 30;
				onPickup = false;
			}
		}
		if(itemID==135) { //placenta
			if(onPickup) {
				Protagonist.MAXHP += 15;
				Protagonist.HP += 15;
				onPickup=false;
			}
			if(Math.random()<0.00008) {
				Protagonist.HP += 10;
			}
		}
		if(itemID==136) { //brittle heart
			if(onPickup) {
				Protagonist.MAXHP += 120;
				Protagonist.HP = Protagonist.MAXHP;
				onPickup=false;
			}
			if(Protagonist.HP <= Protagonist.MAXHP - 20) {
				Protagonist.MAXHP = Protagonist.HP;
			}
		}
		if(itemID==137) { //the pin
			if(onPickup) {
				Protagonist.range += 80f;
				Protagonist.shotSpeed += 0.6f;
				Protagonist.speed += 0.06f;
				onPickup=false;
			}
		}
		if(itemID==138) { //mini mushroom
			if(onPickup) {
				Protagonist.range += 96f;
				Protagonist.speed += 0.06f;
				Protagonist.SPRITEWIDTH *= 0.75f;
				Protagonist.SPRITEHEIGHT *= 0.75f;
				onPickup=false;
			}
		}
		if(itemID==139) { //blood bag
			if(onPickup) {
				Protagonist.MAXHP += 15;
				Protagonist.HP += 60;
				Protagonist.speed += 0.05f;
				onPickup=false;
			}
		}
		if(itemID==140) { //cancer
			if(onPickup) {
				Protagonist.shield += 40;
				onPickup=false;
			}
			if(Protagonist.tookDamageThisRoom) {
				if(Protagonist.isHit) {
					Protagonist.lastDamageTaken = (int)(2f*Protagonist.lastDamageTaken/3f + 1);
				}
			}
		}
		if(itemID==141) { //berserker
			if(Protagonist.isHit) {
				timeSinceLastTrigger = System.currentTimeMillis();
			}
			if(System.currentTimeMillis()-timeSinceLastTrigger<4000) {
				Protagonist.setFireRateMultiplier(10, 0.6f);
				for(int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setDamageMultiplier(15, 2);
				}
			} else {
				Protagonist.setFireRateMultiplier(10, 1);
				for(int i=0; i<Crawling.spells.size(); i++) {
					Crawling.spells.get(i).setDamageMultiplier(15, 1);
				}
			}
		}
	}
	public static void renderFamiliars(GameContainer container, StateBasedGame game, Graphics g) {
		for(int i=0; i<Protagonist.obtainedItems.size(); i++) {
			boolean isFamiliar = false;
			for(int k=0; k<Item.familiarItems.length; k++) {
				if(Protagonist.obtainedItems.get(i).getID()==Item.familiarItems[k]) {
					isFamiliar = true;
				}
			}
			if(isFamiliar) {
				Protagonist.obtainedItems.get(i).render(container, game, g);
			}
		}
	}
	public static void renderHalos(Graphics g) {
		for(int i=0; i<Protagonist.obtainedItems.size(); i++) {
			//golden aura for divine damage/buff
			boolean isHalo = false;
			for(int j=0; j<Item.haloItems.length; j++) {
				if(Protagonist.obtainedItems.get(i).getID()==Item.haloItems[j]) {
					isHalo=true;
				}
			}
			if(isHalo) {
				g.setColor(new Color(255, 225, 80, 120));
				for(int j=0; j<Protagonist.obtainedItems.get(i).getHalos().size(); j++) {
					Circle tempCircle = Protagonist.obtainedItems.get(i).getHalos().get(j);
					g.fillOval(tempCircle.getCenterX()-tempCircle.getRadius(), tempCircle.getCenterY()-tempCircle.getRadius(), tempCircle.getRadius()*2, tempCircle.getRadius()*2);
				}
			}
			//dark purple aura for shadow damage/buff
			boolean isShadow = false;
			for(int j=0; j<Item.shadowItems.length; j++) {
				if(Protagonist.obtainedItems.get(i).getID()==Item.shadowItems[j]) {
					isShadow=true;
				}
			}
			if(isShadow) {
				g.setColor(new Color(144, 40, 144, 120));
				for(int j=0; j<Protagonist.obtainedItems.get(i).getHalos().size(); j++) {
					Circle tempCircle = Protagonist.obtainedItems.get(i).getHalos().get(j);
					g.fillOval(tempCircle.getCenterX()-tempCircle.getRadius(), tempCircle.getCenterY()-tempCircle.getRadius(), tempCircle.getRadius()*2, tempCircle.getRadius()*2);
				}
			}
			//white aura for protective shield
			boolean isProt = false;
			for(int j=0; j<Item.protItems.length; j++) {
				if(Protagonist.obtainedItems.get(i).getID()==Item.protItems[j]) {
					isProt=true;
				}
			}
			if(isProt) {
				g.setColor(new Color(256, 224, 224, 120));
				for(int j=0; j<Protagonist.obtainedItems.get(i).getHalos().size(); j++) {
					Circle tempCircle = Protagonist.obtainedItems.get(i).getHalos().get(j);
					g.fillOval(tempCircle.getCenterX()-tempCircle.getRadius(), tempCircle.getCenterY()-tempCircle.getRadius(), tempCircle.getRadius()*2, tempCircle.getRadius()*2);
				}
			}
			//green aura for poison damage
			boolean isPoison = false;
			for(int j=0; j<Item.poisonItems.length; j++) {
				if(Protagonist.obtainedItems.get(i).getID()==Item.poisonItems[j]) {
					isPoison=true;
				}
			}
			if(isPoison) {
				g.setColor(new Color(140, 216, 128, 120));
				for(int j=0; j<Protagonist.obtainedItems.get(i).getHalos().size(); j++) {
					Circle tempCircle = Protagonist.obtainedItems.get(i).getHalos().get(j);
					g.fillOval(tempCircle.getCenterX()-tempCircle.getRadius(), tempCircle.getCenterY()-tempCircle.getRadius(), tempCircle.getRadius()*2, tempCircle.getRadius()*2);
				}
			}
		}
	}
	public boolean toBeRemoved() {
		return removeMe;
	}
	public int getID() {
		return itemID;
	}
	public Image getSprite() {
		return sprite;
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
	public int getSpriteWidth() {
		return spriteWidth;
	}
	public int getSpriteHeight() {
		return spriteHeight;
	}
	public ArrayList<Projectile> getSpecialProjectiles() {
		return specialProjectiles;
	}
	public ArrayList<Circle> getHalos() {
		return halos;
	}
}
