package aggroforce.block;

import aggroforce.texture.Icon;
import aggroforce.texture.TileRegister;
import aggroforce.util.Side;
import aggroforce.world.storage.IWorldAccess;

public class Glass extends Block {

	Icon icon;

	public Glass(){
		super(8);
	}
	@Override
	public void registerTexTiles(TileRegister reg) {
		this.icon = reg.loadNewIcon("resource/textures/blocks/glass.png");
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		return icon;
	}

	@Override
	public boolean isSolidOpaque(){
		return false;
	}
	@Override
	public boolean shouldRenderSide(IWorldAccess wld, int x, int y, int z, Side side) {
		return true;
	}

	@Override
	public boolean shouldRenderInPass(int pass){
		return pass == 1;
	}

}
