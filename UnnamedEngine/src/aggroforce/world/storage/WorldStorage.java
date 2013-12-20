package aggroforce.world.storage;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import aggroforce.world.WorldLoader;
import aggroforce.world.segment.Segment;

public class WorldStorage implements IWorldAccess{

	private int MAX_SEGMENTS_RADIUS = 15;
	private Segment[][] segStorage = new Segment[this.MAX_SEGMENTS_RADIUS*2][this.MAX_SEGMENTS_RADIUS*2];
	private SegLoader segLoad = new SegLoader();
	private int startx, starty;
	private IntBuffer ibuf = BufferUtils.createIntBuffer((this.MAX_SEGMENTS_RADIUS*2)*(this.MAX_SEGMENTS_RADIUS*2));

	public WorldStorage(WorldLoader wl){
		startx = -this.MAX_SEGMENTS_RADIUS;
		starty = -this.MAX_SEGMENTS_RADIUS;
		for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
			for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
				assert segLoad.addSegment(startx+i, starty+j, wl.generateSegment(startx+i, starty+j));
			}
		}
		for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
			for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
				Segment seg = segLoad.getSegmentAt(startx+i, starty+j);
				assert (seg != null);
				if(seg != null){
				seg.setupDisplayList(this);
				ibuf.put(seg.getDisplayListID());
				}
			}
		}
		ibuf.flip();
	}

	public void render(){
		GL11.glCallLists(this.ibuf);
	}

	@Override
	public int getBlockIdAt(int x, int y, int z) {
		int sx = x%16;
		int sy = z%16;
		if(segLoad.isSegmentLoadedAt(sx, sy)){
			Segment seg = segLoad.getSegmentAt(sx, sy);
			return seg.getBlockIdAt(Math.signum(sx)==-1?sx+15:sx, z, Math.signum(sy)==-1?sy+15:sy);
		}
		return -1;
	}

	public SegmentCoords getSegmentForCoords(int x, int z){
		int sx = (x)%16;
		int sy = (z)%16;
		if(sx>=0&&sx<this.segStorage.length&&sy>=0&&sy<this.segStorage[0].length){
			return new SegmentCoords(sx,sy);
		}
		return null;
	}

	private class SegmentCoords{
		int x,y;

		public SegmentCoords(int x, int y){
			this.x = x;
			this.y = y;
		}

		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
		public int worldToSegmentX(int x){
			return x-this.x*16;
		}
		public int worldToSegmentZ(int z){
			return z-this.y*16;
		}
	}
}
