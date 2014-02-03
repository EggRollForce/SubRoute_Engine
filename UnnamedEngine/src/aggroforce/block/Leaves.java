package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

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
