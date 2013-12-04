package aggroforce.block;

import aggroforce.util.Side;

public class DefaultBlock extends Block{

	@Override
	public boolean shouldRenderSide(int x, int y, int z, Side side) {
		return false;
	}

}
