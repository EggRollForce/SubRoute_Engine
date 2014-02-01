package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public class Stone extends Block {

	Icon icon;

	public Stone() {
		super(2);
	}

	@Override
	public void registerTexTiles(TileRegister reg) {
		icon = reg.loadNewIcon("resource/textures/blocks/stone.png");
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		return icon;
	}
}
