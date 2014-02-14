package subroute.block;

import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class Air extends Block {

	public Air() {
		super(0);
	}

	@Override
	public boolean isSolidOpaque(){
		return false;
	}

	@Override
	public int renderType(){
		return -1;
	}

	@Override
	public void registerTexTiles(TileRegister reg) {

	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		return null;
	}

}
