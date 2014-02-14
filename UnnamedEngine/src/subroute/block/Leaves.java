package subroute.block;

import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class Leaves extends Block{

	Icon icon;
	public Leaves(){
		super(6);
	}

	@Override
	public void registerTexTiles(TileRegister reg) {
		icon = reg.loadNewIcon(("resource/textures/blocks/leaves.png"));
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		// TODO Auto-generated method stub
		return icon;
	}
}
