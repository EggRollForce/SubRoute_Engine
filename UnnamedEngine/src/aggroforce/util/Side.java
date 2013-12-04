package aggroforce.util;

public enum Side {
	DOWN(0),
	UP(1),
	NORTH(2),
	SOUTH(3),
	EAST(4),
	WEST(5);

	private int ordinal;
	private Side(int ord){
		this.ordinal=ord;
	}

	public int getOrdinal(){
		return this.ordinal;
	}

}
