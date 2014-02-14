package subroute.block;

import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

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
