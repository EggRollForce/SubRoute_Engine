package subroute.block;

import subroute.phys.util.AABB;
import subroute.phys.util.AABB.Alignment;
import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class RefinedMetal extends Block {

	Icon icon;
	AABB bb = new AABB(Alignment.BOTTOM_CENTER,0,0,0,0.2,1,0.2);

	public RefinedMetal(){
		super(4);
	}
	@Override
	public void registerTexTiles(TileRegister reg) {
		icon = reg.loadNewIcon("resource/textures/blocks/refined_metal.png");
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		return icon;
	}

	@Override
	public float getSlipperyness(){
		return 4f;
	}

	@Override
	public AABB getBoundingBox(IWorldAccess wld, int x, int y, int z){
		return bb.setPosition(x+0.5D, y+0.5D, z+0.5D);
	}

}
