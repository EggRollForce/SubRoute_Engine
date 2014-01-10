package aggroforce.world.storage;

import java.util.HashMap;
import aggroforce.world.segment.Segment;
import aggroforce.world.util.SegmentPos;

public class SegLoader {

	private HashMap<SegmentPos,Segment> loadedSegs = new HashMap<SegmentPos, Segment>();
	public SegLoader(){

	}

	public boolean addSegment(int x, int y, Segment seg){
		SegmentPos segpos = new SegmentPos(x,y);
		if(!loadedSegs.containsKey(segpos)){
			loadedSegs.put(segpos, seg);
			return true;
		}else{
			return false;
		}
	}
	public boolean isSegmentLoadedAt(int x, int y){
		SegmentPos pos = new SegmentPos(x,y);
		return loadedSegs.containsKey(pos);
	}
	public Segment getSegmentAt(int x, int y){
		if(this.isSegmentLoadedAt(x, y)){
			return loadedSegs.get(new SegmentPos(x,y));
		}else{
			return null;
		}
	}
}
