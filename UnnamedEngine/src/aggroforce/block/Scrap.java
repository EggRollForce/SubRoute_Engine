package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public class Scrap extends Block {

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

}
