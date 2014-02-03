package aggroforce.world.storage;

import java.util.ArrayList;

import aggroforce.render.Renderer;
import aggroforce.world.WorldLoader;
import aggroforce.world.segment.Segment;

public class WorldStorage implements IWorldAccess{

	private static ArrayList<Renderer> rnders = new ArrayList<Renderer>();
	private int MAX_SEGMENTS_RADIUS = 5;
	private SegLoader segLoad = new SegLoader();
	private int startx, starty;
	private boolean needsUpdate = false;
	private static WorldStorage instance;

	public WorldStorage(WorldLoader wl){
		if(instance == null){
			instance = this;
			startx = -this.MAX_SEGMENTS_RADIUS;
			starty = -this.MAX_SEGMENTS_RADIUS;
			for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
				for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
					segLoad.addSegment(wl.generateSegment(startx+i, starty+j).setWorld(this));
				}
			}
			for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
				for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
					Segment seg = segLoad.getSegmentAt(startx+i, starty+j);
					if(seg != null){
						Renderer render = new Renderer();
						seg.renderBlocks(render);
						rnders.add(render);
						System.out.println("Sucessfully loaded segment at x:"+(startx+i)+" y:"+(starty+j));
					}else{
						this.printSegErr(startx+i, starty+j);
					}
				}
			}
		}
	}

	private void printSegErr(int x, int y){
		System.out.println("Could not get Segment at x:"+x+" y:"+y);
	}

	@Override
	public int getBlockIdAt(int x, int y, int z) {
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		Segment seg = segLoad.getSegmentAt(sx, sy);
		if(seg!=null){
			return seg.getBlockIdAt(x, y, z);
		}
		return 0;
	}

	public static ArrayList<Renderer> getRenderers(){
		if(instance.segLoad.checkForUpdate()){
			instance.updated();
			System.out.println("All renderers updated");
		}
		return rnders;
	}

	public static WorldStorage getInstance(){
		return instance;
	}


	@Override
	public boolean setBlockAt(int x, int y, int z, int id) {
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		Segment seg = segLoad.getSegmentAt(sx, sy);
		if(seg!=null){
			return seg.setBlockAt(x, y, z, id);
		}
		return false;
	}

	@Override
	public void updateNeeded() {
		this.needsUpdate = true;
	}

	@Override
	public boolean getIsUpdateNeeded() {
		return this.needsUpdate;
	}

	private void updated(){
		this.needsUpdate = false;
	}
}
