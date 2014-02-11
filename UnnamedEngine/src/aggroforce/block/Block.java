package aggroforce.block;

import aggroforce.phys.util.AABB;
import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public abstract class Block {

	protected static AABB bounding = new AABB(0,0,0,1,1,1);
	public static Block[] blocks = new Block[256];

	public int id;

	private static int nextId = 0;

	public boolean shouldRenderSide(IWorldAccess wld, int x, int y, int z, Side side) {
		int id;
		switch(side.getOrdinal()){
			case 1:
				id = wld.getBlockIdAt(x, y+1, z);
				return !blocks[id].isSolidOpaque();
			case 0:
				id = wld.getBlockIdAt(x, y-1, z);
				return !blocks[id].isSolidOpaque();
			case 2:
				id = wld.getBlockIdAt(x+1, y, z);
				return !blocks[id].isSolidOpaque();
			case 3:
				id = wld.getBlockIdAt(x-1, y, z);
				return !blocks[id].isSolidOpaque();
			case 4:
				id = wld.getBlockIdAt(x, y, z+1);
				return !blocks[id].isSolidOpaque();
			case 5:
				id = wld.getBlockIdAt(x, y, z-1);
				return !blocks[id].isSolidOpaque();
		}
		return false;
	}

	public boolean shouldRenderInPass(int pass){
		return pass == 0;
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

	public AABB getBoudingBox(IWorldAccess wld, int x, int y, int z){
		return bounding;
	}

	//!!CAUTION!! WET FLOOR
	public float getSlipperyness(){
		return 0f;
	}

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
