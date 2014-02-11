 package aggroforce.world.segment;

import aggroforce.block.Block;
import aggroforce.render.Renderer;
import aggroforce.texture.Icon;
import aggroforce.util.Side;
import aggroforce.world.storage.ISegmentAccess;
import aggroforce.world.storage.IWorldAccess;
import aggroforce.world.storage.WorldStorage;

public class Segment implements ISegmentAccess{

	public int segx;
	public int segy;

	private Renderer render;

	public short[][][] blockStorage = new short[16][16][1024];

	private boolean update = false;

	private IWorldAccess world;


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
		if(y<0||y>=1024){
			return 0;
		}
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

	public void renderUpdate(){
		if(this.render!=null){
			render.setUpdated(true);
			this.renderBlocks(render);
			System.out.println("Updated segment");
		}else{
			System.out.println("Renderer not initialized!");
		}
	}

	public void renderBlocks(Renderer rb){
		this.update = false;
		this.render = rb;
		rb.reset();
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				for(int k=0;k<1024;k++){
					int x = i+16*segx;
					int z = j+16*segy;
					Block blk = Block.blocks[this.blockStorage[i][j][k]];
					if(blk!=null&&blk.renderType()!=-1){
						if(!blk.shouldRenderInPass(0)){
							rb = WorldStorage.rendererpass2;
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.UP)){
							Icon icon = blk.getIconForSide(this, x, k, z, Side.UP);
							rb.setNormal(0, 1f, 0);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), icon.getEndU(), icon.getEndV());
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), icon.getStartU(), icon.getEndV());
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), icon.getStartU(), icon.getStartV());
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), icon.getEndU(), icon.getStartV());
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.SOUTH)){
							Icon icon = blk.getIconForSide(this, x, k, z, Side.SOUTH);
							rb.setNormal(1f, 0, 0);
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), icon.getEndU(), icon.getStartV());
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), icon.getEndU(), icon.getEndV());
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), icon.getStartU(), icon.getEndV());
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), icon.getStartU(), icon.getStartV());

						}
						if(blk.shouldRenderSide(this, x, k, z, Side.NORTH)){
							Icon icon = blk.getIconForSide(this, x, k, z, Side.NORTH);
							rb.setNormal(-1f, 0f, 0f);
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), icon.getStartU(), icon.getStartV());
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), icon.getEndU(), icon.getStartV());
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), icon.getEndU(), icon.getEndV());
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), icon.getStartU(), icon.getEndV());
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.WEST)){
							Icon icon = blk.getIconForSide(this, x, k, z, Side.WEST);
							rb.setNormal(0f, 0f, -1f);
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), icon.getEndU(), icon.getEndV());
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), icon.getStartU(), icon.getEndV());
							rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), icon.getStartU(), icon.getStartV());
							rb.addVertexUV(i+(16*segx), k, j+(16*segy), icon.getEndU(), icon.getStartV());
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.EAST)){
							Icon icon = blk.getIconForSide(this, x, k, z, Side.EAST);
							rb.setNormal(0f, 0f, 1f);
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), icon.getStartU(), icon.getEndV());
							rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), icon.getStartU(), icon.getStartV());
							rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), icon.getEndU(), icon.getStartV());
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), icon.getEndU(), icon.getEndV());
						}
						if(blk.shouldRenderSide(this, x, k, z, Side.DOWN)){
							Icon icon = blk.getIconForSide(this, x, k, z, Side.DOWN);
							rb.setNormal(0, -1f, 0);
							rb.addVertexUV(i+1+(16*segx), k-1, j+1+(16*segy), icon.getStartU(), icon.getEndV());
							rb.addVertexUV(i+1+(16*segx), k-1, j+(16*segy), icon.getStartU(), icon.getStartV());
							rb.addVertexUV(i+(16*segx), k-1, j+(16*segy), icon.getEndU(), icon.getStartV());
							rb.addVertexUV(i+(16*segx), k-1, j+1+(16*segy), icon.getEndU(), icon.getEndV());
						}
						if(!blk.shouldRenderInPass(0)){
							rb = render;
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
						if(k<(h-2)){
							this.blockStorage[i][j][k] = 2;
						}else{
							this.blockStorage[i][j][k] = 1;
						}
					}else{
						this.blockStorage[i][j][k] = 0;
					}
				}
			}
		}
	}

	@Override
	public boolean setBlockAt(int x, int y, int z, int id) {
		this.updateNeeded();
		this.world.updateNeeded();
		if(Math.signum(y)==-1||y>=1024){
			return false;
		}
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		if(sx==this.segx&&sy==this.segy){
			int dx = Math.signum(x)==-1?((x+1)%16)+15:(x%16);
			int dz = Math.signum(z)==-1?((z+1)%16)+15:(z%16);
			this.blockStorage[dx][dz][y] = (short) id;
			System.out.println("Set block at x:"+x+" y:"+y+" z:"+z+" to id:"+id );
			return true;
		}else{
			return this.world.setBlockAt(x, y, z, id);
		}
	}
	public Renderer getRenderer(){
		return this.render;
	}

	@Override
	public void updateNeeded() {
		update = true;
	}

	@Override
	public boolean getIsUpdateNeeded() {
		return update;
	}

	@Override
	public boolean blockExistsAt(int x, int y, int z) {
		return this.getBlockIdAt(x, y, z)!=0;
	}
}
