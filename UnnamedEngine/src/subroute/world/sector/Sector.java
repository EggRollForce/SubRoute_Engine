 package subroute.world.sector;

import subroute.block.Block;
import subroute.render.IRenderable;
import subroute.render.Renderer;
import subroute.texture.Icon;
import subroute.util.Side;
import subroute.world.storage.ISegmentAccess;
import subroute.world.storage.IWorldAccess;

public class Sector implements ISegmentAccess,IRenderable{

	public int segx;
	public int segy;

	private Renderer render;

	private static final int subsecs = 64;

	public SubSector[] subStor = new SubSector[subsecs];

	private boolean update = false;

	private IWorldAccess world;


	public Sector(int x, int y, short[][] heightData){
		this.segx = x;
		this.segy = y;
		for(int i = 0; i < subsecs; i++){
			subStor[i] = new SubSector();
		}
		this.generateBlocks(heightData);
	}

	public Sector setWorld(IWorldAccess world){
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
			byte dx = (byte)(x&0b1111);
			byte dz = (byte)(z&0b1111);
			return this.subStor[((int)Math.floor((y/1024f)*subsecs))].getDataAt(dx, (byte)(y&0b1111), dz);
		}else{
			return this.world.getBlockIdAt(x, y, z);
		}
	}

	public boolean checkInBounds(int x, int y){
		boolean xin = x>=(segx*16)&&x<(segx*16)+(16);
		boolean yin = y>=(segy*16)&&y<(segy*16)+(16);
		return yin&&xin;
	}

	public void renderBlocks(Renderer rb){
		this.update = false;
		this.render = rb;
		rb.reset();
		for(byte i=0;i<16;i++){
			for(byte j=0;j<16;j++){
				for(short k=0;k<1024;k++){
					int x = i+16*segx;
					int z = j+16*segy;
					SubSector sec = this.subStor[((int)Math.floor((k/1024f)*subsecs))];
					if(!sec.isEmpty()){
						int id = sec.getDataAt(i,(byte)(k&0b1111),j);
						Block blk = Block.blocks[id];
						if(blk!=null&&blk.renderType()!=-1){
							if(blk.shouldRenderSide(this, x, k, z, Side.UP)){
								Icon icon = blk.getIconForSide(this, x, k, z, Side.UP);
								rb.setNormal(0, 1f, 0);
								rb.addVertexUV(i+1+(16*segx), k+1, j+1+(16*segy), icon.getEndU(), icon.getEndV());
								rb.addVertexUV(i+(16*segx), k+1, j+1+(16*segy), icon.getStartU(), icon.getEndV());
								rb.addVertexUV(i+(16*segx), k+1, j+(16*segy), icon.getStartU(), icon.getStartV());
								rb.addVertexUV(i+1+(16*segx), k+1, j+(16*segy), icon.getEndU(), icon.getStartV());
							}
							if(blk.shouldRenderSide(this, x, k, z, Side.SOUTH)){
								Icon icon = blk.getIconForSide(this, x, k, z, Side.SOUTH);
								rb.setNormal(1f, 0, 0);
								rb.addVertexUV(i+(16*segx), k+1, j+1+(16*segy), icon.getEndU(), icon.getStartV());
								rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), icon.getEndU(), icon.getEndV());
								rb.addVertexUV(i+(16*segx), k, j+(16*segy), icon.getStartU(), icon.getEndV());
								rb.addVertexUV(i+(16*segx), k+1, j+(16*segy), icon.getStartU(), icon.getStartV());

							}
							if(blk.shouldRenderSide(this, x, k, z, Side.NORTH)){
								Icon icon = blk.getIconForSide(this, x, k, z, Side.NORTH);
								rb.setNormal(-1f, 0f, 0f);
								rb.addVertexUV(i+1+(16*segx), k+1, j+1+(16*segy), icon.getStartU(), icon.getStartV());
								rb.addVertexUV(i+1+(16*segx), k+1, j+(16*segy), icon.getEndU(), icon.getStartV());
								rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), icon.getEndU(), icon.getEndV());
								rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), icon.getStartU(), icon.getEndV());
							}
							if(blk.shouldRenderSide(this, x, k, z, Side.WEST)){
								Icon icon = blk.getIconForSide(this, x, k, z, Side.WEST);
								rb.setNormal(0f, 0f, -1f);
								rb.addVertexUV(i+(16*segx), k, j+(16*segy), icon.getEndU(), icon.getEndV());
								rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), icon.getStartU(), icon.getEndV());
								rb.addVertexUV(i+1+(16*segx), k+1, j+(16*segy), icon.getStartU(), icon.getStartV());
								rb.addVertexUV(i+(16*segx), k+1, j+(16*segy), icon.getEndU(), icon.getStartV());
							}
							if(blk.shouldRenderSide(this, x, k, z, Side.EAST)){
								Icon icon = blk.getIconForSide(this, x, k, z, Side.EAST);
								rb.setNormal(0f, 0f, 1f);
								rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), icon.getStartU(), icon.getEndV());
								rb.addVertexUV(i+(16*segx), k+1, j+1+(16*segy), icon.getStartU(), icon.getStartV());
								rb.addVertexUV(i+1+(16*segx), k+1, j+1+(16*segy), icon.getEndU(), icon.getStartV());
								rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), icon.getEndU(), icon.getEndV());
							}
							if(blk.shouldRenderSide(this, x, k, z, Side.DOWN)){
								Icon icon = blk.getIconForSide(this, x, k, z, Side.DOWN);
								rb.setNormal(0, -1f, 0);
								rb.addVertexUV(i+1+(16*segx), k, j+1+(16*segy), icon.getStartU(), icon.getEndV());
								rb.addVertexUV(i+1+(16*segx), k, j+(16*segy), icon.getStartU(), icon.getStartV());
								rb.addVertexUV(i+(16*segx), k, j+(16*segy), icon.getEndU(), icon.getStartV());
								rb.addVertexUV(i+(16*segx), k, j+1+(16*segy), icon.getEndU(), icon.getEndV());
							}
						}
					}
				}
			}
		}
	}
	public void generateBlocks(short[][] heightData){
		for(byte i = 0; i<16; i++){
			for(byte j = 0; j<16; j++){
				int h = heightData[i][j];
				for(short k = 0; k<1024; k++){
					if(k<h){
						if(k<(h-2)){
							this.subStor[((int)Math.floor((k/1024f)*subsecs))].setDataAt(i, (byte)(k&0b1111), j, (short) 2);
						}else{
							this.subStor[((int)Math.floor((k/1024f)*subsecs))].setDataAt(i, (byte)(k&0b1111), j, (short) 1);
						}
					}
				}
			}
		}

	}

	@Override
	public boolean setBlockAt(int x, int y, int z, int id) {
		if(render!=null){
			this.render.markForUpdate(true);
		}
		if(Math.signum(y)==-1||y>=1024){
			return false;
		}
		int sx = (int)Math.floor((x)/16d);
		int sy = (int)Math.floor((z)/16d);
		if(sx==this.segx&&sy==this.segy){
			byte dx = (byte)(x&0b1111);
			byte dz = (byte)(z&0b1111);
			byte dy = (byte)(y&0b1111);
			this.subStor[((int)Math.floor((y/1024f)*subsecs))].setDataAt(dx, dy, dz, (short)id);
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
	public boolean blockExistsAt(int x, int y, int z) {
		return this.getBlockIdAt(x, y, z)!=0;
	}

	@Override
	public boolean isMarkedForUpdate() {
		return update;
	}

	@Override
	public void renderTo(Renderer r) {
		this.renderBlocks(r);
	}
}
