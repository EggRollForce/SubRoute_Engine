package subroute.world.sector;

public class SubSector {

	private short[] blockData;
	private boolean empty = true;

	public boolean isEmpty(){
		return empty;
	}

	public short getDataAt(byte x, byte y, byte z){
		if(empty)
			return 0;
		int i = (x&0b1111)+((y&0b1111)<<4)+((z&0b1111)<<8);
		return blockData[i];
	}

	public void setDataAt(byte x, byte y, byte z, short data){
		if(empty){
			empty = false;
			if(blockData==null){
				blockData = new short[16*16*16];
			}
		}
		int i = (x&0b1111)+((y&0b1111)<<4)+((z&0b1111)<<8);
		blockData[i]=data;
	}
}
