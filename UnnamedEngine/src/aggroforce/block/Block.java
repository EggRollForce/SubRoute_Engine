package aggroforce.block;

import java.util.HashMap;

import aggroforce.util.Side;

public abstract class Block {

	public static HashMap<String,Block> blockMap  = new HashMap<String,Block>();

	public int id;

	public boolean shouldRenderSide(int x, int y, int z, Side side) {
		return false;
	}
}
