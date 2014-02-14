package subroute.util;

public enum Side{
	DOWN(0,0,-1,0),
	UP(1,0,1,0),
	NORTH(2,1,0,0),
	SOUTH(3,-1,0,0),
	EAST(4,0,0,1),
	WEST(5,0,0,-1),
	NONE(-1,0,0,0);

	private int ordinal;
	private int[] pos;
	private Side(int ord, int x, int y, int z){
		this.pos = new int[] {x,y,z};
		this.ordinal=ord;
	}

	public int getOrdinal(){
		return this.ordinal;
	}

	public int getX(){return pos[0];}
	public int getY(){return pos[1];}
	public int getZ(){return pos[2];}

	public static Side[] getValidSides(){
		return new Side[] {DOWN,UP,NORTH,SOUTH,EAST,WEST};
	}

	public static Side[] getFaces(){
		return new Side[] {NORTH,SOUTH,EAST,WEST};
	}
}
