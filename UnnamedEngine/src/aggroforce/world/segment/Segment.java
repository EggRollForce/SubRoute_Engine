package aggroforce.world.segment;

import aggroforce.block.Block;
import aggroforce.render.RenderBlocks;
import aggroforce.util.Side;
import aggroforce.world.storage.ISegmentAccess;
import aggroforce.world.storage.IWorldAccess;

public class Segment implements ISegmentAccess{

	public int segx;
	public int segy;

	public short[][][] blockStorage = new short[16][16][1024];

	private static final float guard = 0.0001f;

	public IWorldAccess world;


	public Segment(int x, int y, short[][] heightData){
		this.segx = x;
		this.segy = y;
		this.generateBlocks(heightData);
	}

	public Segment setWorld(IWorldAccess world){
		this.world = world;
		return this;
	}

	@Override
	public int getBlockIdAt(int x, int y, int z){
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		if(sx==this.segx&&sy==this.segy){
			int dx = Math.signum(x)==-1?((x+1)%16)+15:(x%16);
			int dz = Math.signum(z)==-1?((z+1)%16)+15:(z%16);
			return this.blockStorage[dx][dz][y];
		}else{
			return this.world.getBlockIdAt(x, y, z);
		}
	}

	public boolean checkInBounds(int x, int y){
		boolean xin = x>=(segx*16)&&x<(segx*16)+(16);
		boolean yin = y>=(segy*16)&&y<(segy*16)+(16);
		return yin&&xin;
	}

	public void setupDisplayList(RenderBlocks rb){
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				for(int k=0;k<1024;k++){
					int x = i+16*segx;
					int z = j+16*segy;
					Block blk = Block.blocks[this.blockStorage[i][j][k]];
					if(blk.renderType()!=-1){
						if(blk.shouldRenderSide(this, x, k, z, Side.UP)){
							rb.setNormal(0, 1f, 0);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), 1f/16f - guard, 1f/16f - guard);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), 0 + guard, 1f/16f - guard);
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), 0 + guard, 0 + guard);
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), 1f/16f - guard, 0 + guard);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.SOUTH)){
							rb.setNormal(1f, 0, 0);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), 2f/16f - guard, 0 + guard);
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), 2f/16f - guard, 1f/16f - guard);
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), 1f/16f + guard, 1f/16f - guard);
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), 1f/16f + guard, 0 + guard);

						}
						if(blk.shouldRenderSide(this, x, k, z, Side.NORTH)){
							rb.setNormal(-1f, 0f, 0f);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), 1f/16f + guard, 0 + guard);
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), 2f/16f - guard, 0 + guard);
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), 2f/16f - guard, 1f/16f - guard);
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), 1f/16f + guard, 1f/16f - guard);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.WEST)){
							rb.setNormal(0f, 0f, -1f);
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), 2f/16f - guard, 1f/16f - guard);
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), 1f/16f + guard, 1f/16f - guard);
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), 1f/16F + guard, 0f + guard);
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), 2f/16f - guard, 0f + guard);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.EAST)){
							rb.setNormal(0f, 0f, 1f);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), 1f/16f + guard, 0 + guard);
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), 1f/16f + guard, 1f/16f - guard);
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), 2f/16f - guard, 1f/16f - guard);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), 2f/16f - guard, 0 + guard);
						}
					}
				}
			}
		}
	}
	public void generateBlocks(short[][] heightData){
		for(int i = 0; i<16; i++){
			for(int j = 0; j<16; j++){
				int h = heightData[i][j];
				for(int k = 0; k<1024; k++){
					if(k<h){
						this.blockStorage[i][j][k] = 1;
					}else{
						this.blockStorage[i][j][k] = 0;
					}
				}
			}
		}
	}
}
