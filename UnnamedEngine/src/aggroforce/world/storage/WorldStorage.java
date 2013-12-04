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
				ibuf.put(segStorage[i][j].getDisplayListID());
			}
		}
		ibuf.flip();
	}

	public void render(){
		GL11.glCallLists(this.ibuf);
	}

	@Override
	public int getBlockAt(int x, int y, int z) {
		return 0;
	}
}
