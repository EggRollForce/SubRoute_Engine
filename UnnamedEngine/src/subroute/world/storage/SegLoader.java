package subroute.world.storage;

import java.util.ArrayList;
import java.util.HashMap;

import subroute.world.sector.Sector;

public class SegLoader {

	private HashMap<Long,Sector> loadedSegs = new HashMap<Long,Sector>();

	public boolean addSegment(Sector seg){
		if(!isSegmentLoadedAt(seg)){
			loadedSegs.put(serializePosition(seg.segx,seg.segy),seg);
			return true;
		}else{
			return false;
		}
	}
	public ArrayList<Sector> getLoadedSegments(){
		return (ArrayList<Sector>) loadedSegs.values();
	}
	public boolean isSegmentLoadedAt(Sector seg){
		return this.isSegmentLoadedAt(seg.segx, seg.segy);
	}
	private long serializePosition(int x, int y){
		return (x)+(((long)y)<<32);
	}
	public boolean isSegmentLoadedAt(int x, int y){
		return loadedSegs.get(serializePosition(x,y))!=null;
	}
	public Sector getSegmentAt(int x, int y){
		if(isSegmentLoadedAt(x,y)){
			return loadedSegs.get(serializePosition(x,y));
		}
		return null;
	}
}
