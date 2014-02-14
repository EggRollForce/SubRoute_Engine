package subroute.block;

import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class RefinedMetal extends Block {

	Icon icon;

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

}
