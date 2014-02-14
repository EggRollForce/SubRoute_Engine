package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public class Sand extends Block{
	Icon icon;
	public Sand(){
		super(7);
	}

	@Override
	public void registerTexTiles(TileRegister reg) {
		icon = reg.loadNewIcon(("resource/textures/blocks/sand.png"));
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		// TODO Auto-generated method stub
		return icon;
	}
}
