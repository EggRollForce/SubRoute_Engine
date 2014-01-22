package aggroforce.world.util;

public class SegmentPos {

	public int x,y;
	public SegmentPos(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SegmentPos){
			SegmentPos seg = (SegmentPos)obj;
			return this.x==seg.x&&this.y==seg.y;
		}else{
			return false;
		}
	}

	@Override
	public int hashCode(){
		return (this.x<<16)|this.y;
	}

}
