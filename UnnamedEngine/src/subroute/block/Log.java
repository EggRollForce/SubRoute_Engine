package subroute.block;

import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class Log extends Block{

	Icon[] icon = new Icon[2];
	public Log(){
		super(5);
	}
	@Override
	public void registerTexTiles(TileRegister reg) {
		icon[0]=reg.loadNewIcon("resource/textures/blocks/log-top.png");
		icon[1]=reg.loadNewIcon("resource/textures/blocks/log_side.png");


	}
	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		switch(side.getOrdinal()){
		case 0:
			return icon[0];
		case 1:
			return icon[0];
		default:
			return icon[1];
	}
	}
}
