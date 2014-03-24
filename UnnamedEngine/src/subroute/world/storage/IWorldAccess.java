package subroute.world.storage;

public interface IWorldAccess {

	public int getBlockIdAt(int x, int y, int z);

	public boolean setBlockAt(int x, int y, int z, int id);

	public boolean blockExistsAt(int x, int y, int z);
}
