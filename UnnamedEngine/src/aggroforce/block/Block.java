package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public abstract class Block {

	public static Block[] blocks = new Block[256];

	public int id;

	public boolean shouldRenderSide(IWorldAccess wld, int x, int y, int z, Side side) {
		switch(side.getOrdinal()){
			case 1:
				return wld.getBlockIdAt(x, y+1, z)==0;
			case 0:
				return wld.getBlockIdAt(x, y-1, z)==0;
			case 2:
				return wld.getBlockIdAt(x+1, y, z)==0;
			case 3:
				return wld.getBlockIdAt(x-1, y, z)==0;
			case 4:
				return wld.getBlockIdAt(x, y, z+1)==0;
			case 5:
				return wld.getBlockIdAt(x, y, z-1)==0;
		}
		return false;
	}

	public boolean isSolidOpaque(){
		return true;
	}

	public int renderType(){
		return 0;
	}

	public String getName(){
		return this.toString();
	}
	protected Block(int id) {
		if(id<blocks.length&&blocks[id]==null){
			blocks[id]=this;
		}else{
			System.err.println("Block id "+id+" is already occupied by "+this.getName());
		}
	}

	public abstract void registerTexTiles(TileRegister reg);

	public abstract Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side);

	public static void setupBlocks(){
		new Air();
		new Grass();
		new Stone();
		new Scrap();

		for(Block blk : blocks){
			if(blk!=null){
				blk.registerTexTiles(new TileRegister());
			}
		}
	}
}
