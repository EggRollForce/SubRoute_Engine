package subroute.world.storage;

import java.util.ArrayList;

import subroute.world.segment.Segment;

public class SegLoader {

	private ArrayList<Segment> loadedSegs = new ArrayList<Segment>();
	public SegLoader(){

	}

	public boolean addSegment(Segment seg){
		if(!loadedSegs.contains(seg)){
			loadedSegs.add(seg);
			return true;
		}else{
			return false;
		}
	}
	public ArrayList<Segment> getLoadedSegments(){
		return this.loadedSegs;
	}
	public boolean isSegmentLoadedAt(Segment seg){;
		return loadedSegs.contains(seg);
	}
	public Segment getSegmentAt(int x, int y){
		for(Segment seg : this.loadedSegs){
			if(x==seg.segx&&y==seg.segy){
				return seg;
			}
		}
		return null;
	}
}
