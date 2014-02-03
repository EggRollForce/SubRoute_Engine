package aggroforce.world.storage;

public interface IWorldAccess {

	public int getBlockIdAt(int x, int y, int z);

	public boolean setBlockAt(int x, int y, int z, int id);

	public void updateNeeded();

	public boolean getIsUpdateNeeded();

}
