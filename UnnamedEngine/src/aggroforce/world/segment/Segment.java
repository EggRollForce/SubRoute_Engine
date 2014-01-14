package aggroforce.world.segment;

import aggroforce.block.Block;
import aggroforce.render.RenderBlocks;
import aggroforce.util.Side;
import aggroforce.world.storage.ISegmentAccess;

public class Segment implements ISegmentAccess{

	public int segx;
	public int segy;

	public short[][][] blockStorage = new short[16][16][1024];


	public Segment(int x, int y, short[][] heightData){
		this.segx = x;
		this.segy = y;
		this.generateBlocks(heightData);
	}

	@Override
	public int getBlockIdAt(int x, int y, int z){
		int dx = Math.signum(x)==-1?(x%16)+15:(x%16);
		int dz = Math.signum(z)==-1?(z%16)+15:(z%16);
		return this.blockStorage[dx][dz][y];
	}

	public void setupDisplayList(RenderBlocks rb){
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				for(int k=0;k<1024;k++){
					int x = i+(int)(16*segx*Math.signum(segx));
					int z = j+(int)(16*segy*Math.signum(segy));
					Block blk = Block.blocks[this.blockStorage[i][j][k]];
					if(blk.renderType()!=-1){
						if(blk.shouldRenderSide(this, x, k, z, Side.UP)){
							rb.setNormal(0, 1f, 0);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), 1f/16f, 1f/16f);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), 0, 1f/16f);
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), 0, 0);
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), 1f/16f, 0);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.SOUTH)){
							rb.setNormal(1f, 0, 0);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), 2f/16f, 0);
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), 2f/16f, 1f/16f);
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), 1f/16f, 1f/16f);
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), 1f/16f, 0);

						}
						if(blk.shouldRenderSide(this, x, k, z, Side.NORTH)){
							rb.setNormal(-1f, 0f, 0f);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), 1f/16f, 0);
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), 2f/16f, 0);
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), 2f/16f, 1f/16f);
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), 1f/16f, 1f/16f);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.WEST)){
							rb.setNormal(0f, 0f, -1f);
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), 2f/16f, 1f/16f);
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), 1f/16f, 1f/16f);
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), 1f/16f, 0f);
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), 2f/16f, 0f);
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.EAST)){
							rb.setNormal(0f, 0f, 1f);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), 1f/16f, 0);
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), 1f/16f, 1f/16f);
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), 2f/16f, 1f/16f);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), 2f/16f, 0);
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
