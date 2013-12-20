package aggroforce.world.storage;

import java.util.HashMap;

import aggroforce.world.segment.Segment;
import aggroforce.world.util.SegmentPos;

public class SegLoader {

	private HashMap<SegmentPos,Segment> loadedSegs = new HashMap<SegmentPos, Segment>();
	public SegLoader(){

	}

	public boolean addSegment(int x, int y, Segment seg){
		if(!loadedSegs.containsKey(new SegmentPos(x,y))){
			loadedSegs.put(new SegmentPos(x,y), seg);
			return true;
		}else{
			return false;
		}
	}
	public boolean isSegmentLoadedAt(int x, int y){
		return loadedSegs.containsKey(new SegmentPos(x,y));
	}
	public Segment getSegmentAt(int x, int y){
		return this.isSegmentLoadedAt(x, y)?loadedSegs.get(new SegmentPos(x,y)):null;
	}
}
