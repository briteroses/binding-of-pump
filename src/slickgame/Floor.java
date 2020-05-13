package slickgame;

import java.io.File;
import org.newdawn.slick.*;

public class Floor {
	private int[][] floorLayout;
	private Room[][] floorMap;
	private int floorNumber;
	private int floorSize;
	private int xStartLocation;
	private int yStartLocation;
	private int xBossLocation;
	private int yBossLocation;
	private int xLowerBound;
	private int xUpperBound;
	private int yLowerBound;
	private int yUpperBound;
	//maximum square size for the floor map, followed by boundaries for the starter room
	static final int CAPACITY = 15;
	private static final int STARTER_CAPACITY = 5;
	//for each of three floors: floor number, floor size ranges between (FLOORNAME_MIN_ROOMS) and (FLOORNAME_MIN_ROOMS+FLOORNAME_RANGE)
	private static final int FIRST = 1;
	private static final int FIRST_MIN_ROOMS = 8;
	private static final int FIRST_RANGE = 2;
	private static final int FIRSTX = 2;
	private static final int FIRSTX_MIN_ROOMS = 9;
	private static final int FIRSTX_RANGE = 3;
	private static final int SECOND = 3;
	private static final int SECOND_MIN_ROOMS = 10;
	private static final int SECOND_RANGE = 3;
	private static final int SECONDX = 4;
	private static final int SECONDX_MIN_ROOMS = 12;
	private static final int SECONDX_RANGE = 4;
	private static final int THIRD = 5;
	private static final int THIRD_MIN_ROOMS = 15;
	private static final int THIRD_RANGE = 4;
	private static final int THIRDX = 6;
	private static final int THIRDX_MIN_ROOMS = 18;
	private static final int THIRDX_RANGE = 4;
	private static final int SEVEN = 7;
	private static final int SEVEN_MIN_ROOMS = 22;
	private static final int SEVEN_RANGE = 5;
	//ratio of snake room length to floor size ranges between (MIN_SNAKE_RATIO) and (MIN_SNAKE_RATIO+SNAKE_RANGE)
	private static final double MIN_SNAKE_RATIO = 2/5.0;
	private static final double SNAKE_RANGE = 1/5.0;
	//in method generateBranches(), chance that a branch is created at each iteration of the while loop
	private static final double BRANCH_TRIGGER_CHANCE = 0.3;
	//# of special rooms for each floor
	private int specialRoomAmount = 2;
	public static final int NUMBER_OF_FLOORS = 7;
	
	public Floor(int level) throws SlickException {
		floorNumber = level;
		floorLayout = new int[CAPACITY][CAPACITY];
		floorMap = new Room[CAPACITY][CAPACITY];
		floorSize = 10; //default if unexpected level value is passed
		double roll = Math.random();
		if (roll<0.1) {
			specialRoomAmount=4;
		} else if (roll<0.4) {
			specialRoomAmount=3;
		} else {
			specialRoomAmount=2;
		}
		if(level==FIRST) {
			floorSize = (int)(FIRST_MIN_ROOMS+Math.random()*(FIRST_RANGE+1));
		}
		if(level==FIRSTX) {
			floorSize = (int)(FIRSTX_MIN_ROOMS+Math.random()*(FIRSTX_RANGE+1));
		}
		if(level==SECOND) {
			floorSize = (int)(SECOND_MIN_ROOMS+Math.random()*(SECOND_RANGE+1));
		}
		if(level==SECONDX) {
			floorSize = (int)(SECONDX_MIN_ROOMS+Math.random()*(SECONDX_RANGE+1));
		}
		if(level==THIRD) {
			floorSize = (int)(THIRD_MIN_ROOMS+Math.random()*(THIRD_RANGE+1));
		}
		if(level==THIRDX) {
			floorSize = (int)(THIRDX_MIN_ROOMS+Math.random()*(THIRDX_RANGE+1));
		}
		if(level==SEVEN) {
			floorSize = (int)(SEVEN_MIN_ROOMS+Math.random()*(SEVEN_RANGE+1));
		}
		if(level<=6) {
			this.generateLayout(floorSize-specialRoomAmount);
		}
		if(level==7) {
			this.generateLayout(floorSize-3); //three bosses generated
		}
		this.placeRooms();
		this.findBounds();
	}
	public void generateLayout(int floorSize) {
		int snakeSize = (int)(floorSize*(MIN_SNAKE_RATIO+Math.random()*SNAKE_RANGE));
		this.generateMainSnake(snakeSize);
		this.generateBranches(floorSize-snakeSize);
		if(floorNumber<=6) {
			this.generateSpecialRooms(specialRoomAmount);
		}
		if(floorNumber==7) {
			this.generateSpecialRooms(3); //three bosses generated
		}
	}
	//marks non-null cells in a large snaking path randomly placed on floor
	public void generateMainSnake(int snakeSize) {
		int markerCounter = 0;
		xStartLocation = (int)(STARTER_CAPACITY+Math.random()*STARTER_CAPACITY);
		yStartLocation = (int)(STARTER_CAPACITY+Math.random()*STARTER_CAPACITY);
		int markerX = xStartLocation;
		int markerY = yStartLocation;
		floorLayout[markerX][markerY] = 1;
		markerCounter++;
		double directionSelector = 0;
		int tunnelFactor = 0;
		while(markerCounter<snakeSize) {
			if(tunnelFactor<=-1) {
				tunnelFactor = (int) (3.0*Math.random());
				directionSelector = 4.0*Math.random();
			}
			tunnelFactor--;
			if(directionSelector>3.0) {
				if(markerX==0) {
					continue;
				}
				else {
				markerX--;
				floorLayout[markerX][markerY] = 1;
				markerCounter++;
				}
			}
			else if(directionSelector>2.0) {
				if(markerY==CAPACITY-1) {
					continue;
				}
				else {
				markerY++;
				floorLayout[markerX][markerY] = 1;
				markerCounter++;
				}
			}
			else if(directionSelector>1.0) {
				if(markerX==CAPACITY-1) {
					continue;
				}
				else {
				markerX++;
				floorLayout[markerX][markerY] = 1;
				markerCounter++;
				}
			}
			else {
				if(markerY==0) {
					continue;
				}
				else {
				markerY--;
				floorLayout[markerX][markerY] = 1;
				markerCounter++;
				}
			}
		}
	}
	//marks non-null cells that branch off from the main pathway (snake)
	public void generateBranches(int branchesSize) {
		int markerCounter = 0;
		int markerX = xStartLocation;
		int markerY = yStartLocation;
		double directionSelector = 0;
		double branchSelector = 1.0;
		int tunnelFactor = 0;
		while(markerCounter<branchesSize) {
			if(branchSelector<BRANCH_TRIGGER_CHANCE) {
				if((markerX==0 || floorLayout[markerX-1][markerY]==1) && (markerY==CAPACITY-1 || floorLayout[markerX][markerY+1]==1) && (markerX==CAPACITY-1 || floorLayout[markerX+1][markerY]==1) && (markerY==0 || floorLayout[markerX][markerY-1]==1)) {
					branchSelector = 1.0;
					continue;
				}
				else { //ALMOST exact same code as while loop of method generateSnake()
					if(tunnelFactor<=-1) {
						tunnelFactor = (int) (3.0*Math.random());
						directionSelector = 4.0*Math.random();
					}
					tunnelFactor--;
					if(directionSelector>3.0) {
						if(markerX==0 || floorLayout[markerX-1][markerY]==1) {
							continue;
						}
						else {
						markerX--;
						floorLayout[markerX][markerY] = 1;
						markerCounter++;
						}
					}
					else if(directionSelector>2.0) {
						if(markerY==CAPACITY-1 || floorLayout[markerX][markerY+1]==1) {
							continue;
						}
						else {
						markerY++;
						floorLayout[markerX][markerY] = 1;
						markerCounter++;
						}
					}
					else if(directionSelector>1.0) {
						if(markerX==CAPACITY-1 || floorLayout[markerX+1][markerY]==1) {
							continue;
						}
						else {
						markerX++;
						floorLayout[markerX][markerY] = 1;
						markerCounter++;
						}
					}
					else {
						if(markerY==0 || floorLayout[markerX][markerY-1]==1) {
							continue;
						}
						else {
						markerY--;
						floorLayout[markerX][markerY] = 1;
						markerCounter++;
						}
					}
				}
			}
			else { //IF BRANCH CHANCE IS NOT TRIGGERED
				directionSelector = 4.0*Math.random();
				if(directionSelector>3.0) {
					if(markerX==0 || floorLayout[markerX-1][markerY]==0) {
						continue;
					}
					else {
					markerX--;
					}
				}
				else if(directionSelector>2.0) {
					if(markerY==CAPACITY-1 || floorLayout[markerX][markerY+1]==0) {
						continue;
					}
					else {
					markerY++;
					}
				}
				else if(directionSelector>1.0) {
					if(markerX==CAPACITY-1 || floorLayout[markerX+1][markerY]==0) {
						continue;
					}
					else {
					markerX++;
					}
				}
				else {
					if(markerY==0 || floorLayout[markerX][markerY-1]==0) {
						continue;
					}
					else {
					markerY--;
					}
				}
			}
			branchSelector = Math.random();
		}
	}
	public void generateSpecialRooms(int specialRoomCount) {
		int markerCounter = 0;
		int maxDistFromStart = 0;
		for (int i=0; i<CAPACITY; i++) {
			for (int j=0; j<CAPACITY; j++) {
				if(floorLayout[i][j]==1 && Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation)>maxDistFromStart) {
					maxDistFromStart = Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation);
				}
			}
		}
		outerloop:
		while(markerCounter<specialRoomCount) {
			int dist = (int)((maxDistFromStart+2)*Math.random());
			innerloop:
			for(int i=1; i<CAPACITY-1; i++) {
				for(int j=1; j<CAPACITY-1; j++) {
					if(dist == Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation)-1 && floorLayout[i][j]==0
							&& floorLayout[i-1][j]+floorLayout[i+1][j]+floorLayout[i][j-1]+floorLayout[i][j+1]==1) {
						floorLayout[i][j]=2;
						markerCounter++;
						if(markerCounter>=specialRoomCount) {
							break outerloop;
						}
						break innerloop;
					}
				}
			}
		}
	}
	//finds all non-null layout cells and assigns a random room w/ correct corresponding doorway format to each cell on map
	public void placeRooms() throws SlickException {
		int topIndex;
		int rightIndex;
		int bottomIndex;
		int leftIndex;
		String roomDirectory;
		int roomNumber;
		floorSize = -1; //ensures floorSize value is correct
		//place all rooms (special rooms are replaced later)
		for(int i=0; i<CAPACITY; i++) {
			for(int j=0; j<CAPACITY; j++) {
				if(floorLayout[i][j]>=1) {
					topIndex = 0; rightIndex = 0; bottomIndex = 0; leftIndex = 0;
					if(i!=0 && floorLayout[i-1][j]>=1) {
						topIndex = 1;
					}
					if(j!=CAPACITY-1 && floorLayout[i][j+1]>=1) {
						rightIndex = 1;
					}
					if(i!=CAPACITY-1 && floorLayout[i+1][j]>=1) {
						bottomIndex = 1;
					}
					if(j!=0 && floorLayout[i][j-1]>=1) {
						leftIndex = 1;
					}
					if(i==xStartLocation && j==yStartLocation) {
						floorMap[i][j] = new Room("tiles/" + topIndex + rightIndex + bottomIndex + leftIndex + "first" + "/start.tmx");
					}
					else {
						roomDirectory = "tiles/" + topIndex + rightIndex + bottomIndex + leftIndex + "first";
						roomNumber = (int)(Math.random()*((new File(roomDirectory).list().length-1)/2));
						floorMap[i][j] = new Room(roomDirectory + "/" + roomNumber + ".tmx");
					}
					floorSize++;
				}
			}
		}
		//place boss room
		if(floorNumber<=6) {
			int dist = 0;
			xBossLocation = 0;
			yBossLocation = 0;
			for (int i=1; i<CAPACITY-1; i++) {
				for (int j=1; j<CAPACITY-1; j++) {
					if (floorLayout[i][j]==2 && Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation)>dist) {
						dist = Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation);
						xBossLocation = i;
						yBossLocation = j;
					}
				}
			}
			topIndex = 0; rightIndex = 0; bottomIndex = 0; leftIndex = 0;
			if(xBossLocation!=0 && floorLayout[xBossLocation-1][yBossLocation]>=1) {
				topIndex = 1;
			}
			if(yBossLocation!=CAPACITY-1 && floorLayout[xBossLocation][yBossLocation+1]>=1) {
				rightIndex = 1;
			}
			if(xBossLocation!=CAPACITY-1 && floorLayout[xBossLocation+1][yBossLocation]>=1) {
				bottomIndex = 1;
			}
			if(yBossLocation!=0 && floorLayout[xBossLocation][yBossLocation-1]>=1) {
				leftIndex = 1;
			}
			if(floorNumber % 2 == 0) {
				floorMap[xBossLocation][yBossLocation] = new Room("tiles/specialrooms/" + topIndex + rightIndex + bottomIndex + leftIndex + "firstboss.tmx");
			}
		}
		if(floorNumber==7) {
			int dist = 0;
			xBossLocation = 0;
			yBossLocation = 0;
			for (int i=1; i<CAPACITY-1; i++) {
				for (int j=1; j<CAPACITY-1; j++) {
					if (floorLayout[i][j]==2 && Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation)>dist) {
						dist = Math.abs(i-xStartLocation)+Math.abs(j-yStartLocation);
						xBossLocation = i;
						yBossLocation = j;
					}
				}
			}
			for (int i=1; i<CAPACITY-1; i++) {
				for (int j=1; j<CAPACITY-1; j++) {
					if(floorLayout[i][j]==2) {
						topIndex = 0; rightIndex = 0; bottomIndex = 0; leftIndex = 0;
						if(i!=0 && floorLayout[i-1][j]>=1) {
							topIndex = 1;
						}
						if(j!=CAPACITY-1 && floorLayout[i][j+1]>=1) {
							rightIndex = 1;
						}
						if(i!=CAPACITY-1 && floorLayout[i+1][j]>=1) {
							bottomIndex = 1;
						}
						if(j!=0 && floorLayout[i][j-1]>=1) {
							leftIndex = 1;
						}
						floorMap[i][j] = new Room("tiles/specialrooms/" + topIndex + rightIndex + bottomIndex + leftIndex + "firstboss.tmx");
					}
				}
			}
		}
		floorLayout[xBossLocation][yBossLocation]=3;
	}
	public void findBounds() {
		xLowerBound = CAPACITY;
		for(int i=CAPACITY-1; i>=0; i--) {
			for(int j=0; j<CAPACITY; j++) {
				if(floorLayout[i][j]>=1) {
					xLowerBound = i;
				}
			}
		}
		xUpperBound = 0;
		for(int i=0; i<CAPACITY; i++) {
			for(int j=0; j<CAPACITY; j++) {
				if(floorLayout[i][j]>=1) {
					xUpperBound = i;
				}
			}
		}
		yLowerBound = CAPACITY;
		for(int j=CAPACITY-1; j>=0; j--) {
			for(int i=0; i<CAPACITY; i++) {
				if(floorLayout[i][j]>=1) {
					yLowerBound = j;
				}
			}
		}
		yUpperBound = 0;
		for(int j=0; j<CAPACITY; j++) {
			for(int i=0; i<CAPACITY; i++) {
				if(floorLayout[i][j]>=1) {
					yUpperBound = j;
				}
			}
		}
	}
	// helper class for method placeRooms()
	public String getFloorName() {
		if(floorNumber==1) {
			return "first";
		}
		if(floorNumber==2) {
			return "second";
		}
		if(floorNumber==3) {
			return "third";
		}
		else {
			return "";
		}
	}
	public Room getRoom(int x, int y) {
		return floorMap[x][y];
	}
	public int[][] getLayout(){
		return floorLayout;
	}
	public Room setRoom(Room room, int x, int y) {
		floorMap[x][y] = room;
		return room;
	}
	public int getFloorSize() {
		return floorSize;
	}
	public int[] getStarterLocation(){
		int[] values = {xStartLocation, yStartLocation};
		return values;
	}
	public int[] getBossLocation(){
		int[] values = {xBossLocation, yBossLocation};
		return values;
	}
	public int[] getBounds() {
		int[] values = {xLowerBound, xUpperBound, yLowerBound, yUpperBound};
		return values;
	}
}
