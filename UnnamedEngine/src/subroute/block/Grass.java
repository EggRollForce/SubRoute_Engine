package subroute.block;

import subroute.texture.Icon;
import subroute.texture.TileRegister;
import subroute.util.Side;
import subroute.world.storage.IWorldAccess;

public class Grass extends Block {

	public Icon[] icons = new Icon[3];

	public Grass(){
		super(1);
	}

	@Override
	public void registerTexTiles(TileRegister reg) {
		icons[0]=reg.loadNewIcon("resource/textures/blocks/Grass-Top.png");
		icons[1]=reg.loadNewIcon("resource/textures/blocks/Grass-Side.png");
		icons[2]=reg.loadNewIcon("resource/textures/blocks/Dirt.png");
	}

	@Override
	public Icon getIconForSide(IWorldAccess wld, int x, int y, int z, Side side) {
		switch(side.getOrdinal()){
			case 0:
				return icons[2];
			case 1:
				return icons[0];
			default:
				if(wld.getBlockIdAt(x, y+1, z)==1){
					return icons[2];
				}else{
					return icons[1];
				}
		}
	}
}
