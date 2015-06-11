package subroute.block;

import subroute.phys.util.AABB;
import subroute.phys.util.AABB.Alignment;
import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class Scrap extends Block {

	AABB bbox = new AABB(Alignment.ACTUAL_COORDS,1,0.5,1);
	Icon icon;
	protected Scrap() {
		super(3);
	}

	@Override
	public void registerTexTiles(TileRegister reg) {
		icon = reg.loadNewIcon("resource/textures/blocks/scrap_metal.png");
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		return icon;
	}

	@Override
	public AABB getBoundingBox(IWorldAccess wld, int x, int y, int z) {

		return this.bbox.setPosition(x, y, z);
	}


}
