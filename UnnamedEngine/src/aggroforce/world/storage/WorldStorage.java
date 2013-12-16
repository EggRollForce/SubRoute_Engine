package aggroforce.world.storage;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import aggroforce.world.WorldLoader;
import aggroforce.world.segment.Segment;

public class WorldStorage implements IWorldAccess{

	private int MAX_SEGMENTS_RADIUS = 15;
	private Segment[][] segStorage = new Segment[this.MAX_SEGMENTS_RADIUS*2][this.MAX_SEGMENTS_RADIUS*2];
	private int startx, starty;
	private IntBuffer ibuf = BufferUtils.createIntBuffer((this.MAX_SEGMENTS_RADIUS*2)*(this.MAX_SEGMENTS_RADIUS*2));

	public WorldStorage(WorldLoader wl){
		startx = -this.MAX_SEGMENTS_RADIUS;
		starty = -this.MAX_SEGMENTS_RADIUS;
		for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
			for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
				segStorage[i][j] = wl.generateSegment(startx+i, starty+j);
			}
		}

		for(int i = 0; i < this.MAX_SEGMENTS_RADIUS*2; i++){
			for(int j = 0; j < this.MAX_SEGMENTS_RADIUS*2; j++){
				segStorage[i][j].setupDisplayList(this);
				ibuf.put(segStorage[i][j].getDisplayListID());
			}
		}
		ibuf.flip();
	}

	public void render(){
		GL11.glCallLists(this.ibuf);
	}

	@Override
	public int getBlockIdAt(int x, int y, int z) {
		Segment seg;
		if(this.getSegmentForCoords(x, z)!=null){
			seg = this.getSegmentForCoords(x, z).getSegment();
			seg.getBlockIdAt(x%16, y, z%16);
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
		public Segment getSegment(){
			if(x>=0&&x<segStorage.length&&y>=0&&y<segStorage[0].length){
				return segStorage[x][y];
			}else{
				return null;
			}
		}
		public int worldToSegmentX(int x){
			return x-this.x*16;
		}
		public int worldToSegmentZ(int z){
			return z-this.y*16;
		}
	}
}
