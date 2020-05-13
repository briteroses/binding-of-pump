package slickgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.awt.Rectangle;
import java.util.stream.IntStream;

/** Boss behaviors
 *  lucifer - lil uzi vert
 *  			??
 *  			??
 *  			??
 *  0 - lil yachty
 * 		default state: moves slowly toward player with erratic offset, periodically shoots ring of bullets
 * 		attack 1: marks player position, charges for short period, then dashes at player position
 * 		attack 2: stands still, shoots spiral of bullets with periodic 5-bullet shotgun at player
 *  1 - post malone
 *  	default state: bounces off walls
 *  	when dead: splits into three post malone enemies, which split into more post malone enemies
 *  2 - travis scott
 *  	default state: bounces off walls
 *  	attack 1: continuously shoots ether in cross formation, then shoots 5-bullet shotgun at player
 *  	attack 2: spawns random enemies
 *  3 - trippie redd
 *  	default state: periodically dashes at player
 *  	attack 1: shoots two 8-shot rings of bullets
 *  	attack 2: spawns spider and/or spider husk
 *  4 - playboi carti
 *  	default state: always moves in large oval around room
 *  	attack 1: shoots cross laser
 *  	attack 2: shoots 3-shot shotgun, then 4-shot shotgun
 *  5 - 21 savage
 *  6 - cardi b
 *  7 - quavo
 *  8 - offset
 *  9 - takeoff
 */

public class Boss {
	private String type;
	
	private Image sprite;
	private int spriteWidth;
	private int spriteHeight;
	private float x;
	private float y;
	private Rectangle hitbox;
	
	private int HP;
	private int MAXHP;
	private float speed = 1f;
	private double angle = 30 + 30*Math.random();
	
	private boolean removeMe;
	private int contactDamage;
	
	private int frameCounter = 0; //helper variable for timing attacks
	
	private boolean isMoving;
	private boolean isAttacking;
	private boolean isChargingMovement;
	private long timeSinceLastMovement;
	private long timeSinceLastBullet = System.currentTimeMillis();
	private long timeSinceLastAttack = System.currentTimeMillis();
	private long timeSinceChargeStart;
	private double targetAngle;
	private boolean targetAngleChosen = false; //helper variable for yachty charge attack
	private int[] sequence; //choosing between attack patterns
	
	public Boss(String type, int x, int y) throws SlickException {
		this.type = type;
		this.x = x;
		this.y = y;
		removeMe = false;
		if(type.equals("lucifer")) {
			sprite = new Image("images/enemies/lucifer.png");
			spriteWidth = 169;
			spriteHeight = 200;
			hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			MAXHP = (int)(4500*((10+Protagonist.damage)/20.0)*(0.5+175.0/Protagonist.fireRateDelay));
			HP = MAXHP;
			contactDamage = 15;
			sequence = new int[]{0, 0};
		}
		if(type.equals("0")) {
			sprite = new Image("images/enemies/yachty1.png");
			spriteWidth = 169;
			spriteHeight = 200;
			hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			MAXHP = Crawling.currentLevel*400+800;
			HP = MAXHP;
			contactDamage = 15;
			timeSinceLastMovement = System.currentTimeMillis()-(long)(2000*Math.random());
			timeSinceLastAttack = System.currentTimeMillis()-(long)(2000*Math.random());
		}
		if(type.equals("1")) {
			sprite = new Image("images/enemies/post.png");
			spriteWidth = 169;
			spriteHeight = 200;
			hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			MAXHP = 450;
			HP = MAXHP;
			contactDamage = 20;
		}
		if(type.equals("2")) {
			sprite = new Image("images/enemies/travis.png");
			spriteWidth = 169;
			spriteHeight = 200;
			hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			MAXHP = Crawling.currentLevel*450+700;
			HP = MAXHP;
			contactDamage = 15;
			sequence = new int[]{0, 0};
		}
		if(type.equals("3")) {
			sprite = new Image("images/enemies/trippie.png");
			spriteWidth = 169;
			spriteHeight = 200;
			hitbox = new Rectangle((int)x, (int)y, spriteWidth, spriteHeight);
			MAXHP = Crawling.currentLevel*450+700;
			HP = MAXHP;
			contactDamage = 15;
			sequence = new int[]{0, 0};
		}
	}
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		sprite.draw(x, y, spriteWidth, spriteHeight);
		//boss HP bar
		g.setColor(Color.white);
		g.drawString("BOSS", 256, 80);
		g.setColor(Color.red);
		g.fillRect(304, 82, 400, 15);
		g.setColor(Color.green);
		g.fillRect(304, 82, 400*HP/MAXHP, 15);
	}
	public void update(GameContainer container, StateBasedGame game, int delta, Crawling crawling) throws SlickException {

		this.movement(container);
		this.attack();
		this.deathAction();
		frameCounter++;
		frameCounter %= 600;
		
		hitbox = new Rectangle((int)x, (int)y, (int)spriteWidth, (int)spriteHeight);
		
		//boss dies
		if(HP <= 0) {
			removeMe = true;
		}
	}
	public void movement(GameContainer container) throws SlickException {
		Rectangle pHitbox = new Rectangle((int)Protagonist.x, (int)Protagonist.y, Protagonist.SPRITEWIDTH, Protagonist.SPRITEWIDTH);
		double dis_x = (double)pHitbox.getCenterX() - hitbox.getCenterX();
		double dis_y = (double)pHitbox.getCenterY() - hitbox.getCenterY();
		double angleBetween;
		double distance;
		if (dis_x > 0)
		{
			angleBetween = Math.toDegrees(Math.atan(dis_y / dis_x));
		}
		else
		{
			angleBetween = 180 + Math.toDegrees(Math.atan(dis_y / dis_x));
		}
		distance = Math.sqrt(Math.pow(dis_x, 2) + Math.pow(dis_y, 2));
		if(!isChargingMovement) {
			timeSinceChargeStart = System.currentTimeMillis();
		}
		Input input = container.getInput();
		float walkspeed = speed;
		if (input.isKeyDown(Input.KEY_SPACE)) {
			walkspeed /= 2;
		}
		if (type.equals("lucifer")) {
			if(sequence[0]==2) {
				if(frameCounter<300) {
					x = 64+2.4f*frameCounter;
				}
				if(frameCounter>=300) {
					x = 727-2.4f*(frameCounter-300);
				}
				y = 64;
			}
		}
		if (type.equals("0"))
		{
			if(!isAttacking) {
				if(System.currentTimeMillis()-timeSinceLastMovement>7000) {
					if(!targetAngleChosen) {
						targetAngle = angleBetween;
						targetAngleChosen = true;
					}
					isChargingMovement = true;
					isMoving = true;
					sprite = new Image("images/enemies/yachty2.png");
					if (System.currentTimeMillis()-timeSinceChargeStart > 1200) {
						sprite = new Image("images/enemies/yachty1.png");
						if (y + 30*walkspeed*Math.sin(Math.toRadians(targetAngle)) <= Crawling.currentRoom.getTiledMap().getTileHeight() 
								|| y + 30*walkspeed*Math.sin(Math.toRadians(targetAngle)) + spriteHeight >= Crawling.currentRoom.getPixelHeight() - Crawling.currentRoom.getTiledMap().getTileHeight())
						{
							 targetAngle *= -1;
						}
						if (x + 30*walkspeed*Math.cos(Math.toRadians(targetAngle)) <= Crawling.currentRoom.getTiledMap().getTileWidth() 
								|| x + 30*walkspeed*Math.cos(Math.toRadians(targetAngle)) + spriteWidth >= Crawling.currentRoom.getPixelWidth() - Crawling.currentRoom.getTiledMap().getTileWidth())
						{
							targetAngle = 180 - targetAngle;
						}
						x += 10*walkspeed*Math.cos(Math.toRadians(targetAngle));
						y += 10*walkspeed*Math.sin(Math.toRadians(targetAngle));
						if (System.currentTimeMillis()-timeSinceChargeStart > 4000) {
							timeSinceLastMovement = System.currentTimeMillis()+(long)(1000*Math.random());
							timeSinceLastBullet = System.currentTimeMillis();
							timeSinceLastAttack = System.currentTimeMillis()+(long)(1000*Math.random());
							isMoving = false;
							isChargingMovement = false;
							targetAngleChosen = false;
						}
					}
				}
				else {
					isChargingMovement = false;
					isMoving = false;
					double offsetAngle = System.currentTimeMillis();
					float speed_0 = (float)(2*walkspeed*Math.cos(System.currentTimeMillis()/1000));
					float xMovement = (float) (0.6f*walkspeed*Math.cos(Math.toRadians(angleBetween)) + speed_0*Math.cos(Math.toRadians(offsetAngle)));
					float yMovement = (float) (0.6f*walkspeed*Math.sin(Math.toRadians(angleBetween)) + speed_0*Math.sin(Math.toRadians(offsetAngle)));
					if (!Crawling.currentRoom.isTerrainCollide(x+xMovement, y, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
					{
						x += xMovement;
					}
					if (!Crawling.currentRoom.isTerrainCollide(x, y+yMovement, Room.ENEMY_COLLIDE, spriteWidth, spriteHeight))
					{
						y += yMovement;
					}
				}
			}
		}
		if (type.equals("1")) {
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
		if (type.equals("2")) {
			float speed_1 = (float)1.6*walkspeed;
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
		if (type.equals("3")) {
			if(frameCounter % 100 == 65) {
				targetAngle = 45*(angleBetween/45);
			}
			if(frameCounter % 100 > 65) {
				float speed_0 = 6.4f;
				if (!Crawling.currentRoom.isTerrainCollide((float)(x+speed_0*Math.cos(Math.toRadians(targetAngle))), (float)(y+speed_0*Math.sin(Math.toRadians(targetAngle))), Room.ENEMY_COLLIDE, spriteWidth, spriteHeight)) {
					x += speed_0*Math.cos(Math.toRadians(targetAngle));
					y += speed_0*Math.sin(Math.toRadians(targetAngle));
				}
			}
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
		double distance = Math.sqrt(Math.pow(dis_x, 2) + Math.pow(dis_y, 2));
		if (type.equals("lucifer")) {
			//switch between three phases based on HP
			if(((double)HP)/MAXHP > 2.0/3.0) {
				sequence[0]=0;
			} else if(((double)HP)/MAXHP > 1.0/3.0) {
				sequence[0]=1;
			} else if (((double)HP)/MAXHP < 1.0/3.0) {
				sequence[0]=2;
			}
			if(sequence[0]==0) {
				for(int i=0; i<2; i++) {
					Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
							x+spriteWidth/2, y+spriteHeight/2, 360*Math.random(), 1.5f, 10, 1000, Projectile.VAMP));
				}
			}
			if(sequence[0]==1) {
				if (sequence[1]==0) {
					Crawling.bullets.clear();
					sequence[1]=1;
				}
				if(sequence[1]==1) {
					if (System.currentTimeMillis() - timeSinceLastBullet > 30) {
						Crawling.bullets.add(new Projectile(24, 24, 
								x+spriteWidth/2, y+spriteHeight/2, System.currentTimeMillis()/8, 6f, 10, 1000, Projectile.ETHER));
						Crawling.bullets.add(new Projectile(24, 24, 
								x+spriteWidth/2, y+spriteHeight/2, -System.currentTimeMillis()/7, 6f, 10, 1000, Projectile.ETHER));
						timeSinceLastBullet = System.currentTimeMillis();
					}
					if (frameCounter % 60 == 0) {
						for(int i = 0; i < 30; i++) {
							Crawling.bullets.add(new Projectile(16, 16, 
									x+spriteWidth/2, y+spriteHeight/2, 12*i, 3.6f, 15, 1000, Projectile.BULLET));
						}
					}
					int offset = (int)(12*Math.random());
					if (frameCounter % 60 == 30) {
						for(int i = 0; i < 30; i++) {
							Crawling.bullets.add(new Projectile(16, 16, 
									x+spriteWidth/2, y+spriteHeight/2, 12*i+offset, 3.6f, 15, 1000, Projectile.BULLET));
						}
					}
				}
			}
			if(sequence[0]==2) {
				if(sequence[1]==1 && Crawling.bullets.size()<10) {
					sequence[1]=2;
				}
				if(sequence[1]==2) {
					if(frameCounter % 200 == 0) {
						for (int i = 0; i < 90; i++)
						{
							Crawling.bullets.add(new Projectile(24, 24, 
									x+spriteWidth/2, y+spriteHeight/2, 4*i, 5, 10, 1000, Projectile.ETHER));
						}
					}
					if((angleBetween>60 && angleBetween<120)) {
						if(System.currentTimeMillis()-timeSinceLastAttack>1500) {
							int numberOfShots = 15+(int)(10*Math.random());
							for(int i=0; i<numberOfShots; i++){
								Crawling.bullets.add(new Projectile(32, 32, x+spriteWidth/2, y+spriteHeight/2, 90-20+(int)(40*Math.random()), (float)(6.5f*(0.75+0.5*Math.random())), 10, 1000, Projectile.BULLET));
							}
							timeSinceLastAttack = System.currentTimeMillis();
						}
					} else {
						if(System.currentTimeMillis()-timeSinceLastAttack>1500) {
							for(int i = -2; i < 3; i++) {
								Crawling.bullets.add(new Projectile(24, 24, 
										x+spriteWidth/2, y+spriteHeight/2, angleBetween + 18*i, 6.5f, 10, 1000, Projectile.BULLET));
							}
							timeSinceLastAttack = System.currentTimeMillis();
						}
					}
				}
			}
		}
		if (type.equals("0")) {
			if(!isMoving) {
				if (System.currentTimeMillis()-timeSinceLastAttack>7000) {
					isAttacking = true;
					sprite = new Image("images/enemies/yachty3.png");
					if (System.currentTimeMillis() - timeSinceLastBullet > 150) {
						for (int i = 0; i < 6; i++)
						{
							Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
									x+spriteWidth/2, y+spriteHeight/2, 60*i+System.currentTimeMillis()/18, 4.5f, 10, 1000, Projectile.BULLET));
						}
						timeSinceLastBullet = System.currentTimeMillis();
					}
					if (frameCounter % 100 == 0) {
						for(int i = -2; i < 3; i++) {
							Crawling.bullets.add(new Projectile(24, 24, 
									x+spriteWidth/2, y+spriteHeight/2, angleBetween + 18*i, 6.5f, 15, 1000, Projectile.BULLET));
						}
					}
					if(System.currentTimeMillis()-timeSinceLastAttack>15000) {
						sprite = new Image("images/enemies/yachty1.png");
						timeSinceLastMovement = System.currentTimeMillis()+(long)(1000*Math.random());
						timeSinceLastBullet = System.currentTimeMillis();
						timeSinceLastAttack = System.currentTimeMillis()+(long)(1000*Math.random());
						isAttacking = false;
					}
				}
				else {
					isAttacking = false;
					if (System.currentTimeMillis() - timeSinceLastBullet > 1200)
					{
						for (int i = 0; i < 20; i++)
						{
							Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
									x+spriteWidth/2, y+spriteHeight/2, 18*i, 5, 10, 1000, Projectile.BULLET));
						}
						timeSinceLastBullet = System.currentTimeMillis();
					}
				}
			}
		}
		if (type.equals("2")) {
			if (System.currentTimeMillis()-timeSinceLastAttack>3500-200*Crawling.currentLevel && IntStream.of(sequence).sum()==0) {
				sequence[(int)(sequence.length*Math.random())]=1;
				targetAngle = 45*((int)(2*Math.random()));
			}
			if(sequence[0]==1) {
				sprite = new Image("images/enemies/travis2.png");
				for(int i=0; i<360; i+=90) {
					Crawling.bullets.add(new Projectile(Projectile.BASE_SPRITEWIDTH, Projectile.BASE_SPRITEHEIGHT, 
						(int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()), i+targetAngle, 6f, 12, 1000, Projectile.ETHER));
				}
				if(System.currentTimeMillis()-timeSinceLastAttack>5000-200*Crawling.currentLevel) {
					sprite = new Image("images/enemies/travis1.png");
					for(int i = -2; i < 3; i++) {
						Crawling.bullets.add(new Projectile(24, 24, 
								(int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()), angleBetween + 18*i, 7.5f, 15, 1000, Projectile.BULLET));
					}
					timeSinceLastAttack = System.currentTimeMillis();
					sequence[0]=2;
				}
			}
			if(sequence[0]==2) {
				if(System.currentTimeMillis()-timeSinceLastAttack>600) {
					for(int i = 0; i < 4; i++) {
						Crawling.bullets.add(new Projectile(24, 24, 
								(int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()), angleBetween + 18*i-27, 7.5f, 15, 1000, Projectile.BULLET));
					}
					timeSinceLastAttack = System.currentTimeMillis();
					sequence[0]=0;
				}
			}
			if(sequence[1]==1) {
				sprite = new Image("images/enemies/travis2.png");
				if(System.currentTimeMillis()-timeSinceLastAttack>4500-200*Crawling.currentLevel) {
					sprite = new Image("images/enemies/travis1.png");
					Crawling.enemies.add(new Enemy(Integer.toString((int)(5*Math.random())), (int)(hitbox.getCenterX()), (int)(hitbox.getCenterY())));
//					Crawling.enemies.add(new Enemy(Integer.toString((int)(5*Math.random())), 448, 192));
					timeSinceLastAttack = System.currentTimeMillis();
					sequence[1]=0;
				}
			}
		}
		if (type.equals("3")) {
			if (System.currentTimeMillis()-timeSinceLastAttack>5000-250*Crawling.currentLevel && IntStream.of(sequence).sum()==0) {
				sequence[(int)(sequence.length*Math.random())]=1;
				targetAngle = 45*((int)(2*Math.random()));
			}
			if(sequence[0]==1) {
				isAttacking=true;
				if(System.currentTimeMillis()-timeSinceLastAttack>6000-250*Crawling.currentLevel) {
					for(int i = 0; i < 8; i++) {
						Crawling.bullets.add(new Projectile(32, 32, 
								(int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()), 45*i, 5f, 15, 1000, Projectile.BULLET));
					}
					timeSinceLastAttack = System.currentTimeMillis();
					sequence[0]=2;
				}
			}
			if(sequence[0]==2) {
				if(System.currentTimeMillis()-timeSinceLastAttack>750) {
					for(int i = 0; i < 8; i++) {
						Crawling.bullets.add(new Projectile(32, 32, 
								(int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()), 45*i+22.5f, 5f, 15, 1000, Projectile.BULLET));
					}
					timeSinceLastAttack = System.currentTimeMillis();
					sequence[0]=0;
					isAttacking=false;
				}
			}
			if(sequence[1]==1) {
				Enemy newSpider = new Enemy("3", (int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()));
				newSpider.setHP(40+Crawling.currentLevel*20);
				Crawling.enemies.add(newSpider);
				if(Math.random()<0.5) {
					Crawling.enemies.add(new Enemy("0", (int)(hitbox.getCenterX()), (int)(hitbox.getCenterY())));
				}
				timeSinceLastAttack = System.currentTimeMillis();
				sequence[1]=0;
			}
		}
	}
	public void deathAction() throws SlickException {
		if (this.HP <= 0) {
		if (type.equals("1"))
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
			Crawling.enemies.add(new Enemy("5", newX, newY));
			Crawling.enemies.add(new Enemy("5", newX, newY));
			Crawling.enemies.add(new Enemy("5", newX, newY));
		}
		}
	}
	public void takeDamage(int damage) {
		this.HP -= damage;
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
	public boolean toBeRemoved() {
		return removeMe;
	}
	public int getContactDamage() {
		return contactDamage;
	}
	public Rectangle getHitbox() {
		return hitbox;
	}
}
