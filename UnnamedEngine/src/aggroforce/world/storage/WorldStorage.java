package aggroforce.world.storage;

import aggroforce.render.RenderEngine;
import aggroforce.world.WorldLoader;
import aggroforce.world.segment.Segment;

public class WorldStorage implements IWorldAccess{

	private int MAX_SEGMENTS_RADIUS = 15;
	private Segment[][] segStorage = new Segment[this.MAX_SEGMENTS_RADIUS*2][this.MAX_SEGMENTS_RADIUS*2];
	private SegLoader segLoad = new SegLoader();
	private int startx, starty;

	public WorldStorage(WorldLoader wl){
		startx = -this.MAX_SEGMENTS_RADIUS;
		starty = -this.MAX_SEGMENTS_RADIUS;
		for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
			for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
				segLoad.addSegment(startx+i, starty+j, wl.generateSegment(startx+i, starty+j));
			}
		}
		for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
			for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
				Segment seg = segLoad.getSegmentAt(startx+i, starty+j);
				if(seg != null){
					seg.setupDisplayList(RenderEngine.renderBlocks);
					System.out.println("Sucessfully loaded segment at x:"+(startx+i)+" y:"+(starty+j));
				}else{
					this.printSegErr(startx+i, starty+j);
				}
			}
		}
	}

	private void printSegErr(int x, int y){
		System.out.println("Could not get Segment at x:"+x+" y:"+y);
	}

	@Override
	public int getBlockIdAt(int x, int y, int z) {
		int sx = x%16;
		int sy = z%16;
		if(segLoad.isSegmentLoadedAt(sx, sy)){
			Segment seg = segLoad.getSegmentAt(sx, sy);
			return seg.getBlockIdAt(x, y, z);
		}
		return 0;
	}
}
