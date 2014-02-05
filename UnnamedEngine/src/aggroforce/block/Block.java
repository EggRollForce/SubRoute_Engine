package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public abstract class Block {

	public static Block[] blocks = new Block[256];

	public int id;

	private static int nextId = 0;

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
	protected Block(){
		this(Block.getNextId());
	}
	protected Block(int id) {
		if(id<blocks.length&&blocks[id]==null){
			blocks[id]=this;
			nextId = id+1;
		}else{
			System.err.println("Block id "+id+" is already occupied by "+this.getName());
		}
	}

	protected static int getNextId(){
		return nextId++;
	}
	public abstract void registerTexTiles(TileRegister reg);

	public abstract Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side);

	public static void setupBlocks(){
		new Air();
		new Grass();
		new Stone();
		new Scrap();
		new RefinedMetal();
		new Log();
		new Leaves();
		new Sand();
		new Glass();

		for(Block blk : blocks){
			if(blk!=null){
				blk.registerTexTiles(new TileRegister());
			}
		}
	}
}
