package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.awt.Rectangle;
import java.util.ArrayList;
/**
 * enemyType:
 * 0: Bounces off walls, shoots 3 projectiles
 * 1: Chases but does not fly, shoots 8 vamp projectiles around it
 * 2: Stationary, shoots ether beam
 * 3: Chases but does not fly, shoots projectile
 * 4: Bounces off walls, shoots 8 projectiles around it
 * 5, 6, 7: Post Malone minion (helper entity for Post Malone boss)
 */
public class Enemy {
	private String enemyType;
	
	private Image sprite;
	private int spriteWidth;
	private int spriteHeight;
	private Rectangle hitbox;
	
	private float x;
	private float y;
	private double angle = 30 + 30*Math.random() + 90*((int)(4*Math.random()));
	
	private int HP;
	private long fireRateDelay;
	private float shotSpeed;
	private float speed;
	private int damage;
	private float range;
	
	private boolean isDead; //proxy for removal
	private boolean deathActionTaken;
	private boolean removeMe;
	private int contactDamage;
	private long timeSinceLastBullet = 0;
	private int frameCounter = 0;
	private boolean isTakingDamage = false;
	private boolean isSerpentPoisoned = false;
	private ArrayList<int[]> poisons = new ArrayList<int[]>();
	
	private boolean isSlowed;
	private float slowFactor;
	private long timeSinceLastSlow;

	private static final long SLOW_DURATION = 3000;
	
	public Enemy(String type, float x, float y) throws SlickException {
		this.enemyType = type;
		this.x = x;
		this.y = y;
		fireRateDelay = 800;
		shotSpeed = 4.5f + 0.2f*Crawling.currentLevel;
		speed = 1f + 0.04f*Crawling.currentLevel;
		damage = 10;
		range = 1250f;
		isDead = false;
		deathActionTaken = false;
		removeMe = false;
		contactDamage = 10;
		isSlowed = false;
		if (enemyType.equals("0"))
		{
			this.sprite = new Image("images/enemies/mobs.png").getSubImage(188, 148, 28, 25);
			this.spriteWidth = 56;
			this.spriteHeight = 50;
			this.HP = 10+Crawling.currentLevel*10;
		}
		if (enemyType.equals("1"))
		{
			this.sprite = new Image("images/enemies/mobs.png").getSubImage(143, 48, 28, 33); 
			this.spriteWidth = 47;
			this.spriteHeight = 55;
			this.HP = 20+Crawling.currentLevel*10;
		}
		if (enemyType.equals("2"))
		{
			this.sprite = new Image("images/enemies/mobs.png").getSubImage(49, 208, 30, 28);
			this.spriteWidth = 60;
			this.spriteHeight = 56;
			this.HP = 20+Crawling.currentLevel*10;
		}
		if (enemyType.equals("3"))
		{
			this.sprite = new Image("images/enemies/mobs.png").getSubImage(14, 164, 35, 23);
			this.spriteWidth = 54;
			this.spriteHeight = 36;
			this.HP = 20+Crawling.currentLevel*10;
		}
		if (enemyType.equals("4"))
		{
			this.sprite = new Image("images/enemies/mobs.png").getSubImage(10, 202, 28, 32);
			this.spriteWidth = 56;
			this.spriteHeight = 64;
			this.HP = 20+Crawling.currentLevel*10;
		}
		if (enemyType.equals("5")) {
			this.sprite = new Image("images/enemies/post.png");
			this.spriteWidth = 96;
			this.spriteHeight = 96;
			this.HP = 30+Crawling.currentLevel*10;
		}
		if (enemyType.equals("6")) {
			this.sprite = new Image("images/enemies/post.png");
			this.spriteWidth = 72;
			this.spriteHeight = 72;
			this.HP = 25+Crawling.currentLevel*10;
		}
		if (enemyType.equals("7")) {
			this.sprite = new Image("images/enemies/post.png");
			this.spriteWidth = 56;
			this.spriteHeight = 56;
			this.HP = 20+Crawling.currentLevel*10;
		}
		hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
	}
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(isTakingDamage) {
			sprite.draw((int)x, (int)y, (int)spriteWidth, (int)spriteHeight, Color.red);
		}
		else if (poisons.size()>0) {
			sprite.draw((int)x, (int)y, (int)spriteWidth, (int)spriteHeight, Color.green);
		}
		else if (System.currentTimeMillis()-timeSinceLastSlow<SLOW_DURATION) {
			sprite.draw((int)x, (int)y, (int)spriteWidth, (int)spriteHeight, Color.gray);
		} 
		else {
			sprite.draw((int)x, (int)y, (int)spriteWidth, (int)spriteHeight);
		}
	}
	public void update(GameContainer container, StateBasedGame game, int delta, Crawling crawling) throws SlickException {
		//enemies must use second overload of isTerrainCollide
		if(isTakingDamage) {
			isTakingDamage=false;
		}
		
		frameCounter++;
		frameCounter %= 60;
		
		this.takePoisonDamage();
		
		this.movement(container);
		this.attack();
		this.deathAction();
		
		//ENEMY TAKES DAMAGE FROM SPELL: ALREADY CODED IN PROJECTILE CLASS
	}
	public void movement(GameContainer container) {
		hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
		Rectangle pHitbox = new Rectangle((int)Protagonist.x, (int)Protagonist.y, Protagonist.SPRITEWIDTH, Protagonist.SPRITEWIDTH);
		double dis_x = (double)pHitbox.getCenterX() - hitbox.getCenterX();
		double dis_y = (double)pHitbox.getCenterY() - hitbox.getCenterY();
		double angleBetween;
		if (dis_x > 0)
		{
			angleBetween = Math.toDegrees(Math.atan(dis_y / dis_x));
		}
		else
		{
			angleBetween = 180 + Math.toDegrees(Math.atan(dis_y / dis_x));
		}
		double distance = Math.sqrt(Math.pow(dis_x, 2) + Math.pow(dis_y, 2));
		
		if(isSlowed) {
			timeSinceLastSlow = System.currentTimeMillis();
			isSlowed = false;
		}
		Input input = container.getInput();
		float walkspeed = speed;
		if (System.currentTimeMillis()-timeSinceLastSlow<SLOW_DURATION) {
			walkspeed -= speed*slowFactor;
		}
		if (input.isKeyDown(Input.KEY_SPACE) && Protagonist.slowmodeCharge > 0) {
			walkspeed /= Protagonist.slowmodeFactor;
		}
		if (enemyType.equals("0"))
		{
			float speed_1 = (float)3*walkspeed;
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
		}
		if (enemyType.equals("1"))
		{
			float speed_0 = (float)(1.2*walkspeed);
			if (distance < 320f) {
				if (Protagonist.x > x && !Crawling.currentRoom.isTerrainCollide(x+speed_0, y, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					x += speed_0;
				}
				else if (Protagonist.x < x && !Crawling.currentRoom.isTerrainCollide(x-speed_0, y, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					x -= speed_0;
				}
				if (Protagonist.y > y && !Crawling.currentRoom.isTerrainCollide(x, y+speed_0, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					y += speed_0;
				}
				else if (Protagonist.y < y && !Crawling.currentRoom.isTerrainCollide(x, y-speed_0, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					y -= speed_0;
				}
			}
		}
		if (enemyType.equals("2")) {
			//no movement
		}
		if (enemyType.equals("3"))
		{
			float speed_0 = (float)(1.5*walkspeed);
			if (distance < 896f) {
				if (Protagonist.x > x && !Crawling.currentRoom.isTerrainCollide(x+speed_0, y, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					x += speed_0;
				}
				else if (Protagonist.x < x && !Crawling.currentRoom.isTerrainCollide(x-speed_0, y, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					x -= speed_0;
				}
				if (Protagonist.y > y && !Crawling.currentRoom.isTerrainCollide(x, y+speed_0, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					y += speed_0;
				}
				else if (Protagonist.y < y && !Crawling.currentRoom.isTerrainCollide(x, y-speed_0, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
				{
					y -= speed_0;
				}
			}
		}
		if (enemyType.equals("4"))
		{
			float speed_1 = 2*walkspeed;
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
		}
		if (enemyType.equals("5"))
		{
			float speed_1 = (float)4.5*walkspeed;
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
		}
		if (enemyType.equals("6"))
		{
			float speed_1 = (float)4*walkspeed;
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
		}
		if (enemyType.equals("7"))
		{
			float speed_1 = (float)3*walkspeed;
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
		}
	}

	public void attack() throws SlickException {
		Rectangle pHitbox = new Rectangle((int)Protagonist.x, (int)Protagonist.y, Protagonist.SPRITEWIDTH, Protagonist.SPRITEWIDTH);
		double dis_x = (double)pHitbox.getCenterX() - hitbox.getCenterX();
		double dis_y = (double)pHitbox.getCenterY() - hitbox.getCenterY();
		double angleBetween;
		if (dis_x > 0)
		{
			angleBetween = Math.toDegrees(Math.atan(dis_y / dis_x));
		}
		else
		{
			angleBetween = 180 + Math.toDegrees(Math.atan(dis_y / dis_x));
		}
		float projShotSpeed = shotSpeed;
		if(System.currentTimeMillis()-timeSinceLastSlow<SLOW_DURATION) {
			projShotSpeed -= shotSpeed*slowFactor;
		}
		double distance = Math.sqrt(Math.pow(dis_x, 2) + Math.pow(dis_y, 2));
		if (projShotSpeed > 0.05f)
		{
		if (enemyType.equals("0"))
		{	
			if (distance <= 224 && (System.currentTimeMillis()-timeSinceLastBullet > 1.5*fireRateDelay 
					|| timeSinceLastBullet == 0))
			{
				for (int i = -1; i < 2; i++)
				{
					Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
							x+spriteWidth/4, y+spriteHeight/3, angleBetween + i*30, projShotSpeed*0.7f, damage, (long)1.3*range, Projectile.BULLET));
				}
				timeSinceLastBullet = System.currentTimeMillis();
			}
		}
		if (enemyType.equals("1"))
		{
			if (distance < 450f) {
				if ((System.currentTimeMillis()-timeSinceLastBullet > fireRateDelay || timeSinceLastBullet == 0))
				{
					for (int i = 0; i < 8; i++)
					{
						Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
								x+spriteWidth/4, y+spriteHeight/3, 45*i, projShotSpeed, damage, range/5, Projectile.BULLET));
					}
					timeSinceLastBullet = System.currentTimeMillis();
				}
			}
		}
		if (enemyType.equals("2"))
		{	
			if (distance <= 320f)
			{
				Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
					x+spriteWidth/4, y+spriteHeight/3, angleBetween, projShotSpeed*0.75f, damage, range*4, Projectile.ETHER));					

			}
		}
		if (enemyType.equals("3"))
		{	
			if (distance <= 256 && (System.currentTimeMillis()-timeSinceLastBullet > fireRateDelay 
					|| timeSinceLastBullet == 0))
			{
				Projectile newVamp = new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
						x+spriteWidth/4, y+spriteHeight/3, angleBetween, projShotSpeed, damage, range, Projectile.VAMP);
				newVamp.setBasisEnemy(this);
				Crawling.bullets.add(newVamp);
				timeSinceLastBullet = System.currentTimeMillis();
			}
		}
		if (enemyType.equals("4"))
		{
			if ((System.currentTimeMillis()-timeSinceLastBullet > fireRateDelay || timeSinceLastBullet == 0))
			{
				for (int i = 0; i < 8; i++)
				{
					Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
							x+spriteWidth/4, y+spriteHeight/3, 45*i, projShotSpeed, damage, range/5, Projectile.BULLET));
				}
				timeSinceLastBullet = System.currentTimeMillis();
			}
		}
		}
	}
	public void deathAction() throws SlickException {
		if(isDead) {
			removeMe = true;
		}
		if(this.HP <= 0 && !deathActionTaken) {
			if (enemyType.equals("0") && Math.random()<0.5)
			{
				this.enemyType = "3";
				this.sprite = new Image("images/enemies/mobs.png").getSubImage(14, 164, 35, 23);
				this.spriteWidth = 63;
				this.spriteHeight = 42;
				this.HP = 40;
				boolean spawnedEnemyNotPlaced = true;
				int distFromDeath = 0;
				outerloop:
				while (spawnedEnemyNotPlaced) {
					for (int i=Crawling.currentRoom.getTiledMap().getWidth()-1; i>=0; i--) {
						for (int j=Crawling.currentRoom.getTiledMap().getHeight()-1; j>=0; j--) {
							if (Math.abs(i-(int)((this.x+this.spriteWidth/2)/Crawling.currentRoom.getTiledMap().getTileWidth()))+Math.abs(j-(int)((this.y+this.spriteHeight/2)/Crawling.currentRoom.getTiledMap().getTileHeight()))==distFromDeath && !Crawling.currentRoom.getTiledMap().getTileProperty(Crawling.currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
								spawnedEnemyNotPlaced = false;
								this.x = i*Crawling.currentRoom.getTiledMap().getTileWidth();
								this.y = j*Crawling.currentRoom.getTiledMap().getTileHeight();
								break outerloop;
							}
						}
					}
					distFromDeath++;
				}
			}
			else if (enemyType.equals("5"))
			{
				boolean spawnedEnemyNotPlaced = true;
				int distFromDeath = 0;
				float newX = 0;
				float newY = 0;
				outerloop:
				while (spawnedEnemyNotPlaced) {
					for (int i=Crawling.currentRoom.getTiledMap().getWidth()-1; i>=0; i--) {
						for (int j=Crawling.currentRoom.getTiledMap().getHeight()-1; j>=0; j--) {
							if (Math.abs(i-(int)((this.x+this.spriteWidth/2)/Crawling.currentRoom.getTiledMap().getTileWidth()))+Math.abs(j-(int)((this.y+this.spriteHeight/2)/Crawling.currentRoom.getTiledMap().getTileHeight()))==distFromDeath && !Crawling.currentRoom.getTiledMap().getTileProperty(Crawling.currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
								spawnedEnemyNotPlaced = false;
								newX = i*Crawling.currentRoom.getTiledMap().getTileWidth();
								newY = j*Crawling.currentRoom.getTiledMap().getTileHeight();
								break outerloop;
							}
						}
					}
					distFromDeath++;
				}
				Crawling.enemies.add(new Enemy("6", newX, newY));
				Crawling.enemies.add(new Enemy("6", newX, newY));
				Crawling.enemies.add(new Enemy("6", newX, newY));
				isDead = true;
				deathActionTaken = true;
			}
			else if (enemyType.equals("6"))
			{
				boolean spawnedEnemyNotPlaced = true;
				int distFromDeath = 0;
				float newX = 0;
				float newY = 0;
				outerloop:
				while (spawnedEnemyNotPlaced) {
					for (int i=Crawling.currentRoom.getTiledMap().getWidth()-1; i>=0; i--) {
						for (int j=Crawling.currentRoom.getTiledMap().getHeight()-1; j>=0; j--) {
							if (Math.abs(i-(int)((this.x+this.spriteWidth/2)/Crawling.currentRoom.getTiledMap().getTileWidth()))+Math.abs(j-(int)((this.y+this.spriteHeight/2)/Crawling.currentRoom.getTiledMap().getTileHeight()))==distFromDeath && !Crawling.currentRoom.getTiledMap().getTileProperty(Crawling.currentRoom.getTiledMap().getTileId(i, j, 0), "blocked", "false").equals("true")) {
								spawnedEnemyNotPlaced = false;
								newX = i*Crawling.currentRoom.getTiledMap().getTileWidth();
								newY = j*Crawling.currentRoom.getTiledMap().getTileHeight();
								break outerloop;
							}
						}
					}
					distFromDeath++;
				}
				Crawling.enemies.add(new Enemy("7", newX, newY));
				Crawling.enemies.add(new Enemy("7", newX, newY));
				isDead = true;
				deathActionTaken = true;
			}
			else
			{
				isDead = true;
			}
		}
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int HP) {
		this.HP = HP;
	}
	public void takeDamage(int damage) {
		this.HP -= damage;
		isTakingDamage = true;
	}
	public boolean isTakingDamage() {
		return isTakingDamage;
	}
	public void addPoison(int ticks, int damage, int isSerpent) {
		//isSerpent should be 0 for non-serpent poison, 1 for serpent poison
		int[] poisonPiece = {ticks, damage, isSerpent};
		poisons.add(poisonPiece);
	}
	public void takePoisonDamage() {
		for(int i=0; i<poisons.size(); i++) {
			if(frameCounter==0) {
				this.HP -= poisons.get(i)[1];
				poisons.get(i)[0]--;
				isTakingDamage = true;
			}
		}
		poisons.removeIf(p -> p[0]<=0);
	}
	public boolean getSerpentPoisoned() {
		return isSerpentPoisoned;
	}
	public void setSerpentPoisoned() {
		isSerpentPoisoned = true;
	}
	public boolean getDead() {
		return isDead;
	}
	public boolean toBeRemoved() {
		return removeMe;
	}
	public void setRemove() {
		removeMe = true;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getSpriteWidth() {
		return spriteWidth;
	}
	public int getSpriteHeight() {
		return spriteHeight;
	}
	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}
	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
	public Rectangle getHitbox() {
		return hitbox;
	}
	public int getContactDamage() {
		return contactDamage;
	}
	public void setSlow(float factor) {
		isSlowed = true;
		slowFactor = factor;
	}
}
