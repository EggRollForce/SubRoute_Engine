package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

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

}
